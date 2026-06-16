package com.webstudio.query.manager.queryPage.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.webstudio.layered.architecture.application.exceptions.ApplicationBusinessRuleException;
import com.webstudio.query.manager._base.application.filter.FilterCriteria;
import com.webstudio.query.manager._base.application.filter.FilterOperator;
import com.webstudio.query.manager._base.application.filter.Filters;
import com.webstudio.query.manager._base.application.filter.RestrictedFiltrableField;
import com.webstudio.query.manager._base.application.result.QueryResultRow;
import com.webstudio.query.manager._base.application.result.SelectableField;
import com.webstudio.query.manager._base.application.sort.SortDirection;
import com.webstudio.query.manager._base.application.sort.SortOptions;
import com.webstudio.query.manager.queryPage.application.port.QueryPage;
import com.webstudio.query.manager.queryPage.application.port.QueryPageRepository;

class QueryPageUseCaseInteractorTest {

	enum Field implements RestrictedFiltrableField<String> {
		NAME;

		@Override
		public Class<String> getFieldType() {
			return String.class;
		}

		@Override
		public Set<FilterOperator> getAllowedOperators() {
			return Set.of(FilterOperator.EQUAL, FilterOperator.CONTAINS);
		}
	}

	enum SortField {
		NAME;
	}

	enum SelectField implements SelectableField<String> {
		NAME;

		@Override
		public Class<String> getFieldType() {
			return String.class;
		}
	}

	/** Records the query it receives and returns a preset page. */
	static final class FakeRepository implements QueryPageRepository<Field, SortField, SelectField> {
		QueryPage<Field, SortField, SelectField> received;
		Page<QueryResultRow<SelectField>> toReturn = new Page<>(List.of(), 0, 10, 0L, 0);

		@Override
		public Page<QueryResultRow<SelectField>> findPage(QueryPage<Field, SortField, SelectField> query) {
			this.received = query;
			return this.toReturn;
		}
	}

	/** Identity-mapping interactor, so orchestration can be asserted directly. */
	static class TestInteractor
			extends
				QueryPageUseCaseInteractor<Field, SortField, SelectField, Field, SortField, SelectField, SelectField> {
		TestInteractor(QueryPageRepository<Field, SortField, SelectField> repository) {
			super(repository);
		}

		@Override
		protected QueryPage<Field, SortField, SelectField> toRepositoryQuery(
				QueryPage<Field, SortField, SelectField> inputQuery) {
			return inputQuery;
		}

		@Override
		protected Page<QueryResultRow<SelectField>> toInteractorOutputPage(
				Page<QueryResultRow<SelectField>> repositoryFoundPage) {
			return repositoryFoundPage;
		}
	}

	private final FakeRepository repository = new FakeRepository();
	private final TestInteractor interactor = new TestInteractor(repository);

	private QueryPage<Field, SortField, SelectField> query(Integer page, Integer size) {
		return new QueryPage<>(page, size, SortOptions.of(SortField.NAME, SortDirection.ASC),
				Filters.and(List.of(FilterCriteria.equal(Field.NAME, "john"))), List.of(SelectField.NAME));
	}

	@Test
	void rejectsNullPage() {
		QueryPage<Field, SortField, SelectField> input = query(null, 10);

		assertThrows(ApplicationBusinessRuleException.class, () -> interactor.execute(input));
	}

	@Test
	void rejectsNegativePage() {
		QueryPage<Field, SortField, SelectField> input = query(-1, 10);

		assertThrows(ApplicationBusinessRuleException.class, () -> interactor.execute(input));
	}

	@Test
	void rejectsNullSize() {
		QueryPage<Field, SortField, SelectField> input = query(0, null);

		assertThrows(ApplicationBusinessRuleException.class, () -> interactor.execute(input));
	}

	@ParameterizedTest
	@ValueSource(ints = {0, -5})
	void rejectsNonPositiveSize(int size) {
		QueryPage<Field, SortField, SelectField> input = query(0, size);

		assertThrows(ApplicationBusinessRuleException.class, () -> interactor.execute(input));
	}

	@Test
	void rejectsSizeAboveMaxSize() {
		QueryPage<Field, SortField, SelectField> input = query(0, 101);

		assertThrows(ApplicationBusinessRuleException.class, () -> interactor.execute(input));
	}

	@Test
	void honorsOverriddenMaxSize() {
		TestInteractor cappedInteractor = new TestInteractor(repository) {
			@Override
			protected int maxSize() {
				return 5;
			}
		};
		QueryPage<Field, SortField, SelectField> input = query(0, 6);

		assertThrows(ApplicationBusinessRuleException.class, () -> cappedInteractor.execute(input));
	}

	@Test
	void rejectsForbiddenFilterOperator() {
		QueryPage<Field, SortField, SelectField> input = new QueryPage<>(0, 10,
				SortOptions.of(SortField.NAME, SortDirection.ASC),
				Filters.and(List.of(FilterCriteria.greaterThan(Field.NAME, "john"))), List.of(SelectField.NAME));

		assertThrows(ApplicationBusinessRuleException.class, () -> interactor.execute(input));
	}

	@Test
	void delegatesMappedQueryToRepositoryAndReturnsMappedPage() {
		QueryResultRow<SelectField> row = new QueryResultRow<>(Map.of(SelectField.NAME, "john"));
		Page<QueryResultRow<SelectField>> expected = new Page<>(List.of(row), 0, 10, 1L, 1);
		repository.toReturn = expected;

		QueryPage<Field, SortField, SelectField> input = query(0, 10);

		Page<QueryResultRow<SelectField>> output = interactor.execute(input);

		assertSame(input, repository.received);
		assertSame(expected, output);
		assertEquals(List.of(row), output.getPageListItem());
	}
}
