package com.webstudio.query.manager.queryPage.application.port;

import com.webstudio.query.manager._base.application.filter.FiltrableField;
import com.webstudio.query.manager._base.application.result.QueryResultRow;
import com.webstudio.query.manager._base.application.result.SelectableField;
import com.webstudio.query.manager.queryPage.application.Page;

/**
 * Output port through which a {@code QueryPageUseCaseInteractor} retrieves a
 * single page of rows.
 *
 * <p>
 * Adapters in the infrastructure layer implement this port using the
 * repository-side field enums. The interactor maps the validated client query
 * to these field types before calling {@link #findPage(QueryPage)}.
 *
 * @param <TFilterableField>
 *            the repository enum type identifying the filterable fields
 * @param <TSortableField>
 *            the repository enum type identifying the sortable fields
 * @param <TSelectableField>
 *            the repository enum type identifying the selectable fields
 */
public interface QueryPageRepository<TFilterableField extends Enum<TFilterableField> & FiltrableField<?>, TSortableField extends Enum<TSortableField>, TSelectableField extends Enum<TSelectableField> & SelectableField<?>> {

	/**
	 * Returns the page of rows matching the given query.
	 *
	 * @param query
	 *            the query to execute
	 * @return the matching page; never {@code null}
	 */
	Page<QueryResultRow<TSelectableField>> findPage(
			QueryPage<TFilterableField, TSortableField, TSelectableField> query);

}
