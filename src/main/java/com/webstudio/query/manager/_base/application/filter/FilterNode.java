package com.webstudio.query.manager._base.application.filter;

/**
 * A node in a filter expression tree.
 *
 * <p>
 * This sealed interface has exactly two implementations:
 * {@link FilterCriteria}, a leaf that matches a single field against a
 * {@link FilterOperator}, and {@link FilterGroup}, an internal node that
 * combines other nodes with a {@link LogicalOperator}. Together they form the
 * recursive structure held by a {@link Filters} instance.
 *
 * @param <TField>
 *            the enum type identifying the filterable fields of a query
 */
public sealed interface FilterNode<TField extends Enum<TField>> permits FilterCriteria, FilterGroup {
}
