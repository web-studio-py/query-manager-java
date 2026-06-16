package com.webstudio.query.manager._base.application.filter;

/**
 * Comparison operator applied by a {@link FilterCriteria} to a field.
 *
 * <p>
 * Each constant maps to a way of matching a field's value against the value(s)
 * carried by the criteria. Operators that take no value ({@link #IS_NULL},
 * {@link #IS_NOT_NULL}), a single value (for example {@link #EQUAL}), a
 * collection ({@link #IN}, {@link #NOT_IN}) or a pair of bounds
 * ({@link #BETWEEN}, {@link #NOT_BETWEEN}) are all represented here; the
 * matching arity is enforced by the corresponding {@code FilterCriteria}
 * factory method.
 *
 * <p>
 * Implementers of a {@link RestrictedFiltrableField} can restrict which of
 * these operators are accepted for a given field; see
 * {@link FilterOperatorRestrictionValidator}.
 */
public enum FilterOperator {
	/** Field value equals the given value. */
	EQUAL,
	/** Field value differs from the given value. */
	NOT_EQUAL,
	/** Field value is strictly greater than the given value. */
	GREATER_THAN,
	/** Field value is greater than or equal to the given value. */
	GREATER_THAN_OR_EQUAL,
	/** Field value is strictly less than the given value. */
	LESS_THAN,
	/** Field value is less than or equal to the given value. */
	LESS_THAN_OR_EQUAL,
	/** Field value is one of the given values. */
	IN,
	/** Field value is none of the given values. */
	NOT_IN,
	/** Text field contains the given substring. */
	CONTAINS,
	/** Text field does not contain the given substring. */
	NOT_CONTAINS,
	/** Text field starts with the given prefix. */
	STARTS_WITH,
	/** Text field ends with the given suffix. */
	ENDS_WITH,
	/** Field value falls within the inclusive range {@code [low, high]}. */
	BETWEEN,
	/** Field value falls outside the inclusive range {@code [low, high]}. */
	NOT_BETWEEN,
	/** Field value is {@code null}. */
	IS_NULL,
	/** Field value is not {@code null}. */
	IS_NOT_NULL
}
