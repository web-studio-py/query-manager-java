/**
 * Type-safe building blocks for filter expressions.
 *
 * <p>
 * A filter is a tree of
 * {@link com.webstudio.query.manager._base.application.filter.FilterNode}s
 * rooted in a
 * {@link com.webstudio.query.manager._base.application.filter.Filters}
 * instance. Leaves are
 * {@link com.webstudio.query.manager._base.application.filter.FilterCriteria}
 * (a field, a
 * {@link com.webstudio.query.manager._base.application.filter.FilterOperator}
 * and its value(s)) and internal nodes are
 * {@link com.webstudio.query.manager._base.application.filter.FilterGroup}s
 * combined with a
 * {@link com.webstudio.query.manager._base.application.filter.LogicalOperator}.
 * Fields are modelled as enums implementing
 * {@link com.webstudio.query.manager._base.application.filter.FiltrableField}
 * or
 * {@link com.webstudio.query.manager._base.application.filter.RestrictedFiltrableField},
 * the latter enforced by
 * {@link com.webstudio.query.manager._base.application.filter.FilterOperatorRestrictionValidator}.
 */
package com.webstudio.query.manager._base.application.filter;
