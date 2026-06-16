package com.webstudio.query.manager.queryAll.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.webstudio.layered.architecture.application.exceptions.ApplicationBusinessRuleException;
import com.webstudio.query.manager._base.application.filter.FilterCriteria;
import com.webstudio.query.manager._base.application.filter.FilterOperator;
import com.webstudio.query.manager._base.application.filter.Filters;
import com.webstudio.query.manager._base.application.filter.RestrictedFiltrableField;
import com.webstudio.query.manager._base.application.result.QueryResultRow;
import com.webstudio.query.manager._base.application.result.SelectableField;
import com.webstudio.query.manager._base.application.sort.SortDirection;
import com.webstudio.query.manager._base.application.sort.SortOptions;
import com.webstudio.query.manager.queryAll.application.port.QueryAll;
import com.webstudio.query.manager.queryAll.application.port.QueryAllRepository;

class QueryAllUseCaseInteractorTest {

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

	/** Records the query it receives and returns a preset result. */
	static final class FakeRepository implements QueryAllRepository<Field, SortField, SelectField> {
		QueryAll<Field, SortField, SelectField> received;
		List<QueryResultRow<SelectField>> toReturn = List.of();

		@Override
		public List<QueryResultRow<SelectField>> findAll(QueryAll<Field, SortField, SelectField> query) {
			this.received = query;
			return this.toReturn;
		}
	}

	/** Identity-mapping interactor, so orchestration can be asserted directly. */
	static final class TestInteractor
			extends
				QueryAllUseCaseInteractor<Field, SortField, SelectField, Field, SortField, SelectField, SelectField> {
		TestInteractor(QueryAllRepository<Field, SortField, SelectField> repository) {
			super(repository);
		}

		@Override
		protected QueryAll<Field, SortField, SelectField> toRepositoryQuery(
				QueryAll<Field, SortField, SelectField> inputQuery) {
			return inputQuery;
		}

		@Override
		protected List<QueryResultRow<SelectField>> toInteractorOutputList(
				List<QueryResultRow<SelectField>> repositoryFoundList) {
			return repositoryFoundList;
		}
	}

	private final FakeRepository repository = new FakeRepository();
	private final TestInteractor interactor = new TestInteractor(repository);

	private QueryAll<Field, SortField, SelectField> query(SortOptions<SortField> sortBy, Filters<Field> filters) {
		return new QueryAll<>(sortBy, filters, List.of(SelectField.NAME));
	}

	@Test
	void rejectsNullSortBy() {
		QueryAll<Field, SortField, SelectField> input = query(null, Filters.empty());

		assertThrows(ApplicationBusinessRuleException.class, () -> interactor.execute(input));
	}

	@Test
	void rejectsNullFilters() {
		QueryAll<Field, SortField, SelectField> input = query(SortOptions.empty(), null);

		assertThrows(ApplicationBusinessRuleException.class, () -> interactor.execute(input));
	}

	@Test
	void rejectsForbiddenFilterOperator() {
		Filters<Field> filters = Filters.and(List.of(FilterCriteria.greaterThan(Field.NAME, "john")));
		QueryAll<Field, SortField, SelectField> input = query(SortOptions.empty(), filters);

		assertThrows(ApplicationBusinessRuleException.class, () -> interactor.execute(input));
	}

	@Test
	void delegatesMappedQueryToRepositoryAndReturnsMappedOutput() {
		QueryResultRow<SelectField> row = new QueryResultRow<>(Map.of(SelectField.NAME, "john"));
		repository.toReturn = List.of(row);

		Filters<Field> filters = Filters.and(List.of(FilterCriteria.equal(Field.NAME, "john")));
		QueryAll<Field, SortField, SelectField> input = query(SortOptions.of(SortField.NAME, SortDirection.ASC),
				filters);

		List<QueryResultRow<SelectField>> output = interactor.execute(input);

		assertSame(input, repository.received);
		assertEquals(List.of(row), output);
	}
}
