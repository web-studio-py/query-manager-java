package com.webstudio.query.manager.queryAll.application.port;

import java.util.List;

import com.webstudio.query.manager._base.application.filter.FiltrableField;
import com.webstudio.query.manager._base.application.result.QueryResultRow;
import com.webstudio.query.manager._base.application.result.SelectableField;

/**
 * Output port through which a {@code QueryAllUseCaseInteractor} retrieves all
 * rows matching a query.
 *
 * <p>
 * Adapters in the infrastructure layer implement this port (for example against
 * a database) using the repository-side field enums. The interactor maps the
 * client query to these field types before calling {@link #findAll(QueryAll)}.
 *
 * @param <TFilterableField>
 *            the repository enum type identifying the filterable fields
 * @param <TSortableField>
 *            the repository enum type identifying the sortable fields
 * @param <TSelectableField>
 *            the repository enum type identifying the selectable fields
 */
public interface QueryAllRepository<TFilterableField extends Enum<TFilterableField> & FiltrableField<?>, TSortableField extends Enum<TSortableField>, TSelectableField extends Enum<TSelectableField> & SelectableField<?>> {

	/**
	 * Returns all rows matching the given query.
	 *
	 * @param query
	 *            the query to execute
	 * @return the matching rows; never {@code null}
	 */
	List<QueryResultRow<TSelectableField>> findAll(QueryAll<TFilterableField, TSortableField, TSelectableField> query);

}
