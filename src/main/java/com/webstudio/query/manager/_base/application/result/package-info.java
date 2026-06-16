/**
 * Type-safe representation of query result rows.
 *
 * <p>
 * Output columns are modelled as enums implementing
 * {@link com.webstudio.query.manager._base.application.result.SelectableField},
 * and each row is a
 * {@link com.webstudio.query.manager._base.application.result.QueryResultRow}
 * whose {@code get(SelectableField)} accessor returns values already cast to
 * the field's declared type.
 */
package com.webstudio.query.manager._base.application.result;
