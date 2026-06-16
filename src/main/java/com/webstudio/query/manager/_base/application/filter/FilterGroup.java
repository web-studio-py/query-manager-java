package com.webstudio.query.manager._base.application.filter;

import java.util.Iterator;
import java.util.List;

/**
 * An internal {@link FilterNode} that combines child nodes with a
 * {@link LogicalOperator}.
 *
 * <p>
 * A group lets clients express nested boolean logic, for example
 * {@code A AND (B OR C)}. Children may themselves be groups, so the resulting
 * structure is an arbitrarily deep expression tree. Instances are immutable:
 * the supplied children are defensively copied with {@link List#copyOf} and are
 * never exposed in a mutable form.
 *
 * <p>
 * Create groups through the {@link #and(List)} and {@link #or(List)} factory
 * methods.
 *
 * @param <TField>
 *            the enum type identifying the filterable fields of a query
 */
public final class FilterGroup<TField extends Enum<TField>>
		implements
			FilterNode<TField>,
			Iterable<FilterNode<TField>> {

	private final LogicalOperator operator;
	private final List<FilterNode<TField>> children;

	private FilterGroup(LogicalOperator operator, List<FilterNode<TField>> children) {
		this.operator = operator;
		this.children = List.copyOf(children);
	}

	/**
	 * Creates a group whose children are combined with {@link LogicalOperator#AND}.
	 *
	 * @param <TField>
	 *            the enum type identifying the filterable fields
	 * @param children
	 *            the child nodes to combine; copied defensively
	 * @return a new conjunction group
	 */
	public static <TField extends Enum<TField>> FilterGroup<TField> and(List<FilterNode<TField>> children) {
		return new FilterGroup<TField>(LogicalOperator.AND, children);
	}

	/**
	 * Creates a group whose children are combined with {@link LogicalOperator#OR}.
	 *
	 * @param <TField>
	 *            the enum type identifying the filterable fields
	 * @param children
	 *            the child nodes to combine; copied defensively
	 * @return a new disjunction group
	 */
	public static <TField extends Enum<TField>> FilterGroup<TField> or(List<FilterNode<TField>> children) {
		return new FilterGroup<TField>(LogicalOperator.OR, children);
	}

	/**
	 * Returns the connective used to combine this group's children.
	 *
	 * @return the logical operator
	 */
	public LogicalOperator getOperator() {
		return operator;
	}

	/**
	 * Returns the immutable list of child nodes.
	 *
	 * @return the children; never {@code null}
	 */
	public List<FilterNode<TField>> getChildren() {
		return children;
	}

	/**
	 * Returns an iterator over this group's child nodes.
	 *
	 * @return an iterator over the children
	 */
	@Override
	public Iterator<FilterNode<TField>> iterator() {
		return children.iterator();
	}
}
