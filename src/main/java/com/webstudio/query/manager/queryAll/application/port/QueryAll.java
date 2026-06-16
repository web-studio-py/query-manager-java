package com.webstudio.query.manager.queryAll.application.port;

import java.util.List;

import com.webstudio.query.manager._base.application.filter.Filters;
import com.webstudio.query.manager._base.application.filter.FiltrableField;
import com.webstudio.query.manager._base.application.result.SelectableField;
import com.webstudio.query.manager._base.application.sort.SortOptions;

/**
 * Immutable input describing an unpaged query: which rows to return, in what
 * order, and which fields to select.
 *
 * <p>
 * It is consumed by {@link QueryAllRepository#findAll(QueryAll)} and by
 * {@code QueryAllUseCaseInteractor}, which validates and maps it before
 * delegating to the repository.
 *
 * @param <TFilterableField>
 *            the enum type identifying the filterable fields
 * @param <TSortableField>
 *            the enum type identifying the sortable fields
 * @param <TSelectableField>
 *            the enum type identifying the selectable (output) fields
 * @param sortBy
 *            the ordering to apply
 * @param filters
 *            the filter expression restricting the rows
 * @param selectedFields
 *            the fields to include in each result row
 */
public record QueryAll<TFilterableField extends Enum<TFilterableField> & FiltrableField<?>, TSortableField extends Enum<TSortableField>, TSelectableField extends Enum<TSelectableField> & SelectableField<?>>(
		SortOptions<TSortableField> sortBy, Filters<TFilterableField> filters, List<TSelectableField> selectedFields) {

}
