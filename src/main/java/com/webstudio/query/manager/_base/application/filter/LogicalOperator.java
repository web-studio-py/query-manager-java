package com.webstudio.query.manager._base.application.filter;

/**
 * Boolean connective used by a {@link FilterGroup} to combine its child nodes.
 */
public enum LogicalOperator {
	/** All child nodes must match (logical conjunction). */
	AND,
	/** At least one child node must match (logical disjunction). */
	OR
}
