package com.webstudio.query.manager.queryAll.application;

import java.util.List;

import com.webstudio.layered.architecture.application.UseCaseInteractor;
import com.webstudio.layered.architecture.application.exceptions.ApplicationBusinessRuleException;
import com.webstudio.query.manager._base.application.filter.FilterOperatorRestrictionValidator;
import com.webstudio.query.manager._base.application.filter.Filters;
import com.webstudio.query.manager._base.application.filter.FiltrableField;
import com.webstudio.query.manager._base.application.filter.RestrictedFiltrableField;
import com.webstudio.query.manager._base.application.result.QueryResultRow;
import com.webstudio.query.manager._base.application.result.SelectableField;
import com.webstudio.query.manager._base.application.sort.SortOptions;
import com.webstudio.query.manager.queryAll.application.port.QueryAll;
import com.webstudio.query.manager.queryAll.application.port.QueryAllRepository;

/**
 * Base use case that executes an unpaged query and returns all matching rows.
 *
 * <p>
 * The {@link #execute(QueryAll)} template is {@code final} and runs a fixed
 * pipeline:
 * <ol>
 * <li>validate the input ({@code sortBy} and {@code filters} must be non-null,
 * and every filter operator must be allowed by its
 * {@link RestrictedFiltrableField});</li>
 * <li>map the client query to a repository query via
 * {@link #toRepositoryQuery(QueryAll)};</li>
 * <li>delegate to the {@link QueryAllRepository};</li>
 * <li>map the repository rows to the interactor output via
 * {@link #toInteractorOutputList(List)}.</li>
 * </ol>
 * Subclasses supply the two mapping steps and may keep their own constructor;
 * the validation and orchestration are inherited.
 *
 * <p>
 * The seven type parameters fall into three groups, decoupling the API the
 * client speaks from the field model the repository understands and the fields
 * exposed in the output:
 * <ul>
 * <li><b>Input</b> ({@code TInput*}) — the field enums used in the incoming
 * {@link QueryAll}; the filterable input fields are
 * {@link RestrictedFiltrableField} so operator restrictions can be
 * enforced;</li>
 * <li><b>Repository</b> ({@code TRepository*}) — the field enums understood by
 * the {@link QueryAllRepository};</li>
 * <li><b>Output</b> ({@code TInteractorOutputSelectableField}) — the selectable
 * fields of the rows returned to the caller.</li>
 * </ul>
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
public abstract class QueryAllUseCaseInteractor<TInputFilterableField extends Enum<TInputFilterableField> & RestrictedFiltrableField<?>, TInputSortableField extends Enum<TInputSortableField>, TInputSelectableField extends Enum<TInputSelectableField> & SelectableField<?>, TRepositoryFilterableField extends Enum<TRepositoryFilterableField> & FiltrableField<?>, TRepositorySortableField extends Enum<TRepositorySortableField>, TRepositorySelectableField extends Enum<TRepositorySelectableField> & SelectableField<?>, TInteractorOutputSelectableField extends Enum<TInteractorOutputSelectableField> & SelectableField<?>>
		implements
			UseCaseInteractor<QueryAll<TInputFilterableField, TInputSortableField, TInputSelectableField>, List<QueryResultRow<TInteractorOutputSelectableField>>> {

	private final QueryAllRepository<TRepositoryFilterableField, TRepositorySortableField, TRepositorySelectableField> repository;

	/**
	 * Creates the interactor backed by the given repository.
	 *
	 * @param repository
	 *            the repository the use case delegates to
	 */
	protected QueryAllUseCaseInteractor(
			QueryAllRepository<TRepositoryFilterableField, TRepositorySortableField, TRepositorySelectableField> repository) {
		this.repository = repository;
	}

	/**
	 * Validates and runs the query, returning all matching rows.
	 *
	 * @param inputQuery
	 *            the client query
	 * @return the matching rows mapped to the output field model
	 * @throws ApplicationBusinessRuleException
	 *             if {@code sortBy} or {@code filters} is {@code null}, or a filter
	 *             uses an operator not allowed by its field
	 */
	@Override
	public final List<QueryResultRow<TInteractorOutputSelectableField>> execute(
			QueryAll<TInputFilterableField, TInputSortableField, TInputSelectableField> inputQuery) {

		this.ensureSortIsValid(inputQuery.sortBy());
		this.ensureFiltersIsValid(inputQuery.filters());

		QueryAll<TRepositoryFilterableField, TRepositorySortableField, TRepositorySelectableField> repositoryQuery = this
				.toRepositoryQuery(inputQuery);

		List<QueryResultRow<TRepositorySelectableField>> repositoryFoundList = this.repository.findAll(repositoryQuery);

		List<QueryResultRow<TInteractorOutputSelectableField>> interactorOutputList = this
				.toInteractorOutputList(repositoryFoundList);

		return interactorOutputList;
	}

	/**
	 * Maps the validated client query to a repository query.
	 *
	 * @param inputQuery
	 *            the validated client query
	 * @return the equivalent query expressed in the repository field model
	 */
	protected abstract QueryAll<TRepositoryFilterableField, TRepositorySortableField, TRepositorySelectableField> toRepositoryQuery(
			QueryAll<TInputFilterableField, TInputSortableField, TInputSelectableField> inputQuery);

	/**
	 * Maps the rows returned by the repository to the interactor output rows.
	 *
	 * @param repositoryFoundList
	 *            the rows returned by the repository
	 * @return the rows expressed in the output field model
	 */
	protected abstract List<QueryResultRow<TInteractorOutputSelectableField>> toInteractorOutputList(
			List<QueryResultRow<TRepositorySelectableField>> repositoryFoundList);

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
