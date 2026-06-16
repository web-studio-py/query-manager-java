package com.webstudio.query.manager.queryPage.application.port;

import java.util.List;

import com.webstudio.query.manager._base.application.filter.Filters;
import com.webstudio.query.manager._base.application.filter.FiltrableField;
import com.webstudio.query.manager._base.application.result.SelectableField;
import com.webstudio.query.manager._base.application.sort.SortOptions;

/**
 * Immutable input describing a paged query: a single page of rows, with
 * ordering and field selection.
 *
 * <p>
 * It is consumed by {@link QueryPageRepository#findPage(QueryPage)} and by
 * {@code QueryPageUseCaseInteractor}, which validates {@code page} and
 * {@code size} before delegating to the repository.
 *
 * @param <TFilterableField>
 *            the enum type identifying the filterable fields
 * @param <TSortableField>
 *            the enum type identifying the sortable fields
 * @param <TSelectableField>
 *            the enum type identifying the selectable (output) fields
 * @param page
 *            the zero-based page index
 * @param size
 *            the maximum number of rows per page
 * @param sortBy
 *            the ordering to apply
 * @param filters
 *            the filter expression restricting the rows
 * @param selectedFields
 *            the fields to include in each result row
 */
public record QueryPage<TFilterableField extends Enum<TFilterableField> & FiltrableField<?>, TSortableField extends Enum<TSortableField>, TSelectableField extends Enum<TSelectableField> & SelectableField<?>>(
		Integer page, Integer size, SortOptions<TSortableField> sortBy, Filters<TFilterableField> filters,
		List<TSelectableField> selectedFields) {

}
