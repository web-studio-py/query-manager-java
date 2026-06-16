package com.webstudio.query.manager.queryPage.application;

import com.webstudio.layered.architecture.application.UseCaseInteractor;
import com.webstudio.layered.architecture.application.exceptions.ApplicationBusinessRuleException;
import com.webstudio.query.manager._base.application.filter.FilterOperatorRestrictionValidator;
import com.webstudio.query.manager._base.application.filter.Filters;
import com.webstudio.query.manager._base.application.filter.FiltrableField;
import com.webstudio.query.manager._base.application.filter.RestrictedFiltrableField;
import com.webstudio.query.manager._base.application.result.QueryResultRow;
import com.webstudio.query.manager._base.application.result.SelectableField;
import com.webstudio.query.manager._base.application.sort.SortOptions;
import com.webstudio.query.manager.queryPage.application.port.QueryPage;
import com.webstudio.query.manager.queryPage.application.port.QueryPageRepository;

/**
 * Base use case that executes a paged query and returns a single {@link Page}
 * of rows.
 *
 * <p>
 * The {@link #execute(QueryPage)} template is {@code final} and runs a fixed
 * pipeline:
 * <ol>
 * <li>validate the input ({@code page} must be non-null and {@code >= 0};
 * {@code size} must be non-null, {@code > 0} and {@code <= }
 * {@link #maxSize()}; {@code sortBy} and {@code filters} must be non-null, and
 * every filter operator must be allowed by its
 * {@link RestrictedFiltrableField});</li>
 * <li>map the client query to a repository query via
 * {@link #toRepositoryQuery(QueryPage)};</li>
 * <li>delegate to the {@link QueryPageRepository};</li>
 * <li>map the repository page to the interactor output via
 * {@link #toInteractorOutputPage(Page)}.</li>
 * </ol>
 * Subclasses supply the two mapping steps and may override {@link #maxSize()}
 * to change the page-size ceiling; the validation and orchestration are
 * inherited.
 *
 * <p>
 * The seven type parameters mirror those of {@code QueryAllUseCaseInteractor},
 * split into <b>Input</b> ({@code TInput*}), <b>Repository</b>
 * ({@code TRepository*}) and <b>Output</b>
 * ({@code TInteractorOutputSelectableField}) groups so the client-facing API is
 * decoupled from the repository field model and the output fields.
 *
 * @param <TInputFilterableField>
 *            the input enum identifying filterable fields (operator-restricted)
 * @param <TInputSortableField>
 *            the input enum identifying sortable fields
 * @param <TInputSelectableField>
 *            the input enum identifying selectable fields
 * @param <TRepositoryFilterableField>
 *            the repository enum identifying filterable fields
 * @param <TRepositorySortableField>
 *            the repository enum identifying sortable fields
 * @param <TRepositorySelectableField>
 *            the repository enum identifying selectable fields
 * @param <TInteractorOutputSelectableField>
 *            the enum identifying the fields of the output rows
 */
public abstract class QueryPageUseCaseInteractor<TInputFilterableField extends Enum<TInputFilterableField> & RestrictedFiltrableField<?>, TInputSortableField extends Enum<TInputSortableField>, TInputSelectableField extends Enum<TInputSelectableField> & SelectableField<?>, TRepositoryFilterableField extends Enum<TRepositoryFilterableField> & FiltrableField<?>, TRepositorySortableField extends Enum<TRepositorySortableField>, TRepositorySelectableField extends Enum<TRepositorySelectableField> & SelectableField<?>, TInteractorOutputSelectableField extends Enum<TInteractorOutputSelectableField> & SelectableField<?>>
		implements
			UseCaseInteractor<QueryPage<TInputFilterableField, TInputSortableField, TInputSelectableField>, Page<QueryResultRow<TInteractorOutputSelectableField>>> {

	private final QueryPageRepository<TRepositoryFilterableField, TRepositorySortableField, TRepositorySelectableField> repository;

	/**
	 * Creates the interactor backed by the given repository.
	 *
	 * @param repository
	 *            the repository the use case delegates to
	 */
	protected QueryPageUseCaseInteractor(
			QueryPageRepository<TRepositoryFilterableField, TRepositorySortableField, TRepositorySelectableField> repository) {
		this.repository = repository;
	}

	/**
	 * Validates and runs the query, returning the requested page.
	 *
	 * @param inputQuery
	 *            the client query
	 * @return the matching page mapped to the output field model
	 * @throws ApplicationBusinessRuleException
	 *             if {@code page}, {@code size}, {@code sortBy} or {@code filters}
	 *             is invalid, or a filter uses an operator not allowed by its field
	 */
	@Override
	public final Page<QueryResultRow<TInteractorOutputSelectableField>> execute(
			QueryPage<TInputFilterableField, TInputSortableField, TInputSelectableField> inputQuery) {
		this.ensurePageIsValid(inputQuery.page());
		this.ensureSizeIsValid(inputQuery.size());
		this.ensureSortIsValid(inputQuery.sortBy());
		this.ensureFiltersIsValid(inputQuery.filters());
		QueryPage<TRepositoryFilterableField, TRepositorySortableField, TRepositorySelectableField> repositoryQuery = toRepositoryQuery(
				inputQuery);
		Page<QueryResultRow<TRepositorySelectableField>> foundPage = repository.findPage(repositoryQuery);
		Page<QueryResultRow<TInteractorOutputSelectableField>> interactorOutputPage = toInteractorOutputPage(foundPage);
		return interactorOutputPage;
	}

	/**
	 * Returns the maximum allowed page size. Defaults to {@code 100}; override to
	 * change the ceiling.
	 *
	 * @return the maximum page size
	 */
	protected int maxSize() {
		return 100;
	}

	/**
	 * Maps the validated client query to a repository query.
	 *
	 * @param inputQuery
	 *            the validated client query
	 * @return the equivalent query expressed in the repository field model
	 */
	protected abstract QueryPage<TRepositoryFilterableField, TRepositorySortableField, TRepositorySelectableField> toRepositoryQuery(
			QueryPage<TInputFilterableField, TInputSortableField, TInputSelectableField> inputQuery);

	/**
	 * Maps the page returned by the repository to the interactor output page.
	 *
	 * @param repositoryFoundPage
	 *            the page returned by the repository
	 * @return the page expressed in the output field model
	 */
	protected abstract Page<QueryResultRow<TInteractorOutputSelectableField>> toInteractorOutputPage(
			Page<QueryResultRow<TRepositorySelectableField>> repositoryFoundPage);

	private void ensurePageIsValid(Integer page) {
		this.ensurePageIsNotNull(page);
		this.ensurePageIsGreaterThanOrEqualToZero(page);
	}

	private void ensurePageIsNotNull(Integer page) {
		if (page == null) {
			throw new ApplicationBusinessRuleException("Page number cannot be null");
		}
	}

	private void ensurePageIsGreaterThanOrEqualToZero(Integer page) {
		if (page != null && page < 0) {
			throw new ApplicationBusinessRuleException("Page number cannot be negative");
		}
	}

	private void ensureSizeIsValid(Integer size) {
		this.ensureSizeIsNotNull(size);
		this.ensureSizeIsGreaterThanZero(size);
		this.ensureSizeIsLessThanOrEqualToMaxSize(size);
	}

	private void ensureSizeIsNotNull(Integer size) {
		if (size == null) {
			throw new ApplicationBusinessRuleException("Page size cannot be null");
		}
	}

	private void ensureSizeIsGreaterThanZero(Integer size) {
		if (size != null && size <= 0) {
			throw new ApplicationBusinessRuleException("Page size must be greater than zero");
		}
	}

	private void ensureSizeIsLessThanOrEqualToMaxSize(Integer size) {
		if (size != null && size > maxSize()) {
			throw new ApplicationBusinessRuleException(String.format("Page size cannot be greater than %d", maxSize()));
		}
	}

	private void ensureSortIsValid(SortOptions<TInputSortableField> sortBy) {
		this.ensureSortByIsNotNull(sortBy);
	}

	private void ensureSortByIsNotNull(SortOptions<TInputSortableField> sortBy) {
		if (sortBy == null) {
			throw new ApplicationBusinessRuleException("Sort by cannot be null");
		}
	}

	private void ensureFiltersIsValid(Filters<TInputFilterableField> filters) {
		this.ensureFiltersIsNotNull(filters);
		this.ensureFilterOperatorsAreAllowed(filters);
	}

	private void ensureFiltersIsNotNull(Filters<TInputFilterableField> filters) {
		if (filters == null) {
			throw new ApplicationBusinessRuleException("Filters cannot be null");
		}
	}

	private void ensureFilterOperatorsAreAllowed(Filters<TInputFilterableField> filters) {
		FilterOperatorRestrictionValidator.validate(filters);
	}

}
