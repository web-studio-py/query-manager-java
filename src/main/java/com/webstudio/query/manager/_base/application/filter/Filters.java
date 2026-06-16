package com.webstudio.query.manager._base.application.filter;

import java.util.List;

/**
 * The root of a filter expression applied to a query.
 *
 * <p>
 * A {@code Filters} instance wraps an optional {@link FilterNode} tree. It is
 * created through the {@link #and(List)}, {@link #or(List)} and
 * {@link #empty()} factory methods, which apply the following normalization:
 * <ul>
 * <li>a {@code null} or empty list yields an {@link #empty() empty} filter (no
 * root);</li>
 * <li>a single node is unwrapped and used directly as the root, with no
 * surrounding group;</li>
 * <li>two or more nodes are wrapped in a {@link FilterGroup} with the requested
 * connective.</li>
 * </ul>
 *
 * <p>
 * Example:
 *
 * <pre>{@code
 * Filters<UserField> filters = Filters.and(List.of(FilterCriteria.equal(UserField.STATUS, Status.ACTIVE),
 * 		FilterCriteria.greaterThan(UserField.AGE, 18)));
 * }</pre>
 *
 * @param <TNode>
 *            the enum type identifying the filterable fields of a query
 */
public class Filters<TNode extends Enum<TNode>> {

	private final FilterNode<TNode> root;

	private Filters(FilterNode<TNode> root) {
		this.root = root;
	}

	/**
	 * Combines the given nodes with {@link LogicalOperator#OR}.
	 *
	 * @param <TNode>
	 *            the enum type identifying the filterable fields
	 * @param nodes
	 *            the nodes to combine; {@code null} or empty yields an empty
	 *            filter, a single node is used as-is
	 * @return the resulting filter
	 */
	public static <TNode extends Enum<TNode>> Filters<TNode> or(List<FilterNode<TNode>> nodes) {
		if (nodes == null || nodes.isEmpty())
			return empty();
		if (nodes.size() == 1)
			return new Filters<TNode>(nodes.get(0));
		return new Filters<TNode>(FilterGroup.or(nodes));
	}

	/**
	 * Combines the given nodes with {@link LogicalOperator#AND}.
	 *
	 * @param <TNode>
	 *            the enum type identifying the filterable fields
	 * @param nodes
	 *            the nodes to combine; {@code null} or empty yields an empty
	 *            filter, a single node is used as-is
	 * @return the resulting filter
	 */
	public static <TNode extends Enum<TNode>> Filters<TNode> and(List<FilterNode<TNode>> nodes) {
		if (nodes == null || nodes.isEmpty())
			return empty();
		if (nodes.size() == 1)
			return new Filters<TNode>(nodes.get(0));
		return new Filters<TNode>(FilterGroup.and(nodes));
	}

	/**
	 * Returns a filter with no criteria.
	 *
	 * @param <TNode>
	 *            the enum type identifying the filterable fields
	 * @return an empty filter
	 */
	public static <TNode extends Enum<TNode>> Filters<TNode> empty() {
		return new Filters<TNode>(null);
	}

	/**
	 * Indicates whether this filter carries no criteria.
	 *
	 * @return {@code true} if there is no root node
	 */
	public boolean isEmpty() {
		return root == null;
	}

	/**
	 * Returns the root node of the filter expression.
	 *
	 * @return the root {@link FilterNode}, or {@code null} when this filter is
	 *         {@link #isEmpty() empty}
	 */
	public FilterNode<TNode> getRoot() {
		return root;
	}
}
