package com.webstudio.query.manager._base.application.filter;

import java.util.List;

/**
 * A leaf {@link FilterNode} that matches a single field against a
 * {@link FilterOperator} and its operand value(s).
 *
 * <p>
 * Instances are created through the static factory methods, one per operator,
 * which enforce the correct number of values (none, one, a collection, or a
 * pair of bounds) and keep the operand list immutable. The comparison factories
 * ({@code greaterThan}, {@code between}, &hellip;) require a {@link Comparable}
 * value type, while the text factories ({@code contains}, {@code startsWith},
 * &hellip;) operate on {@link String} and accept an optional case-insensitivity
 * flag.
 *
 * <p>
 * Example:
 *
 * <pre>{@code
 * FilterCriteria<UserField, String> byName = FilterCriteria.contains(UserField.NAME, "jo", true);
 * FilterCriteria<UserField, Integer> byAge = FilterCriteria.between(UserField.AGE, 18, 65);
 * }</pre>
 *
 * @param <TField>
 *            the enum type identifying the filterable fields of a query
 * @param <TValue>
 *            the Java type of the operand value(s)
 */
public final class FilterCriteria<TField extends Enum<TField>, TValue> implements FilterNode<TField> {

	private final TField field;
	private final FilterOperator operator;
	private final List<TValue> values;
	private final boolean ignoreCase;

	private FilterCriteria(TField field, FilterOperator operator, List<TValue> values) {
		this(field, operator, values, false);
	}

	private FilterCriteria(TField field, FilterOperator operator, List<TValue> values, boolean ignoreCase) {
		this.field = field;
		this.operator = operator;
		this.values = List.copyOf(values);
		this.ignoreCase = ignoreCase;
	}

	/**
	 * Creates an {@link FilterOperator#EQUAL} criteria.
	 *
	 * @param <TField>
	 *            the field enum type
	 * @param <TValue>
	 *            the value type
	 * @param field
	 *            the field to match
	 * @param value
	 *            the value the field must equal
	 * @return the criteria
	 */
	public static <TField extends Enum<TField>, TValue> FilterCriteria<TField, TValue> equal(TField field,
			TValue value) {
		return new FilterCriteria<TField, TValue>(field, FilterOperator.EQUAL, List.of(value));
	}

	/**
	 * Creates a {@link FilterOperator#NOT_EQUAL} criteria.
	 *
	 * @param <TField>
	 *            the field enum type
	 * @param <TValue>
	 *            the value type
	 * @param field
	 *            the field to match
	 * @param value
	 *            the value the field must differ from
	 * @return the criteria
	 */
	public static <TField extends Enum<TField>, TValue> FilterCriteria<TField, TValue> notEqual(TField field,
			TValue value) {
		return new FilterCriteria<TField, TValue>(field, FilterOperator.NOT_EQUAL, List.of(value));
	}

	/**
	 * Creates a {@link FilterOperator#GREATER_THAN} criteria.
	 *
	 * @param <TField>
	 *            the field enum type
	 * @param <TValue>
	 *            the comparable value type
	 * @param field
	 *            the field to match
	 * @param value
	 *            the exclusive lower bound
	 * @return the criteria
	 */
	public static <TField extends Enum<TField>, TValue extends Comparable<TValue>> FilterCriteria<TField, TValue> greaterThan(
			TField field, TValue value) {
		return new FilterCriteria<TField, TValue>(field, FilterOperator.GREATER_THAN, List.of(value));
	}

	/**
	 * Creates a {@link FilterOperator#GREATER_THAN_OR_EQUAL} criteria.
	 *
	 * @param <TField>
	 *            the field enum type
	 * @param <TValue>
	 *            the comparable value type
	 * @param field
	 *            the field to match
	 * @param value
	 *            the inclusive lower bound
	 * @return the criteria
	 */
	public static <TField extends Enum<TField>, TValue extends Comparable<TValue>> FilterCriteria<TField, TValue> greaterThanOrEqual(
			TField field, TValue value) {
		return new FilterCriteria<TField, TValue>(field, FilterOperator.GREATER_THAN_OR_EQUAL, List.of(value));
	}

	/**
	 * Creates a {@link FilterOperator#LESS_THAN} criteria.
	 *
	 * @param <TField>
	 *            the field enum type
	 * @param <TValue>
	 *            the comparable value type
	 * @param field
	 *            the field to match
	 * @param value
	 *            the exclusive upper bound
	 * @return the criteria
	 */
	public static <TField extends Enum<TField>, TValue extends Comparable<TValue>> FilterCriteria<TField, TValue> lessThan(
			TField field, TValue value) {
		return new FilterCriteria<TField, TValue>(field, FilterOperator.LESS_THAN, List.of(value));
	}

	/**
	 * Creates a {@link FilterOperator#LESS_THAN_OR_EQUAL} criteria.
	 *
	 * @param <TField>
	 *            the field enum type
	 * @param <TValue>
	 *            the comparable value type
	 * @param field
	 *            the field to match
	 * @param value
	 *            the inclusive upper bound
	 * @return the criteria
	 */
	public static <TField extends Enum<TField>, TValue extends Comparable<TValue>> FilterCriteria<TField, TValue> lessThanOrEqual(
			TField field, TValue value) {
		return new FilterCriteria<TField, TValue>(field, FilterOperator.LESS_THAN_OR_EQUAL, List.of(value));
	}

	/**
	 * Creates an {@link FilterOperator#IN} criteria.
	 *
	 * @param <TField>
	 *            the field enum type
	 * @param <TValue>
	 *            the value type
	 * @param field
	 *            the field to match
	 * @param values
	 *            the candidate values; copied defensively
	 * @return the criteria
	 */
	public static <TField extends Enum<TField>, TValue> FilterCriteria<TField, TValue> in(TField field,
			List<? extends TValue> values) {
		return new FilterCriteria<TField, TValue>(field, FilterOperator.IN, List.copyOf(values));
	}

	/**
	 * Creates a {@link FilterOperator#NOT_IN} criteria.
	 *
	 * @param <TField>
	 *            the field enum type
	 * @param <TValue>
	 *            the value type
	 * @param field
	 *            the field to match
	 * @param values
	 *            the excluded values; copied defensively
	 * @return the criteria
	 */
	public static <TField extends Enum<TField>, TValue> FilterCriteria<TField, TValue> notIn(TField field,
			List<? extends TValue> values) {
		return new FilterCriteria<TField, TValue>(field, FilterOperator.NOT_IN, List.copyOf(values));
	}

	/**
	 * Creates a case-sensitive {@link FilterOperator#CONTAINS} criteria.
	 *
	 * @param <TField>
	 *            the field enum type
	 * @param field
	 *            the text field to match
	 * @param value
	 *            the substring the field must contain
	 * @return the criteria
	 */
	public static <TField extends Enum<TField>> FilterCriteria<TField, String> contains(TField field, String value) {
		return contains(field, value, false);
	}

	/**
	 * Creates a {@link FilterOperator#CONTAINS} criteria.
	 *
	 * @param <TField>
	 *            the field enum type
	 * @param field
	 *            the text field to match
	 * @param value
	 *            the substring the field must contain
	 * @param ignoreCase
	 *            whether the match is case-insensitive
	 * @return the criteria
	 */
	public static <TField extends Enum<TField>> FilterCriteria<TField, String> contains(TField field, String value,
			boolean ignoreCase) {
		return new FilterCriteria<TField, String>(field, FilterOperator.CONTAINS, List.of(value), ignoreCase);
	}

	/**
	 * Creates a case-sensitive {@link FilterOperator#NOT_CONTAINS} criteria.
	 *
	 * @param <TField>
	 *            the field enum type
	 * @param field
	 *            the text field to match
	 * @param value
	 *            the substring the field must not contain
	 * @return the criteria
	 */
	public static <TField extends Enum<TField>> FilterCriteria<TField, String> notContains(TField field, String value) {
		return notContains(field, value, false);
	}

	/**
	 * Creates a {@link FilterOperator#NOT_CONTAINS} criteria.
	 *
	 * @param <TField>
	 *            the field enum type
	 * @param field
	 *            the text field to match
	 * @param value
	 *            the substring the field must not contain
	 * @param ignoreCase
	 *            whether the match is case-insensitive
	 * @return the criteria
	 */
	public static <TField extends Enum<TField>> FilterCriteria<TField, String> notContains(TField field, String value,
			boolean ignoreCase) {
		return new FilterCriteria<TField, String>(field, FilterOperator.NOT_CONTAINS, List.of(value), ignoreCase);
	}

	/**
	 * Creates a case-sensitive {@link FilterOperator#STARTS_WITH} criteria.
	 *
	 * @param <TField>
	 *            the field enum type
	 * @param field
	 *            the text field to match
	 * @param value
	 *            the prefix the field must start with
	 * @return the criteria
	 */
	public static <TField extends Enum<TField>> FilterCriteria<TField, String> startsWith(TField field, String value) {
		return startsWith(field, value, false);
	}

	/**
	 * Creates a {@link FilterOperator#STARTS_WITH} criteria.
	 *
	 * @param <TField>
	 *            the field enum type
	 * @param field
	 *            the text field to match
	 * @param value
	 *            the prefix the field must start with
	 * @param ignoreCase
	 *            whether the match is case-insensitive
	 * @return the criteria
	 */
	public static <TField extends Enum<TField>> FilterCriteria<TField, String> startsWith(TField field, String value,
			boolean ignoreCase) {
		return new FilterCriteria<TField, String>(field, FilterOperator.STARTS_WITH, List.of(value), ignoreCase);
	}

	/**
	 * Creates a case-sensitive {@link FilterOperator#ENDS_WITH} criteria.
	 *
	 * @param <TField>
	 *            the field enum type
	 * @param field
	 *            the text field to match
	 * @param value
	 *            the suffix the field must end with
	 * @return the criteria
	 */
	public static <TField extends Enum<TField>> FilterCriteria<TField, String> endsWith(TField field, String value) {
		return endsWith(field, value, false);
	}

	/**
	 * Creates an {@link FilterOperator#ENDS_WITH} criteria.
	 *
	 * @param <TField>
	 *            the field enum type
	 * @param field
	 *            the text field to match
	 * @param value
	 *            the suffix the field must end with
	 * @param ignoreCase
	 *            whether the match is case-insensitive
	 * @return the criteria
	 */
	public static <TField extends Enum<TField>> FilterCriteria<TField, String> endsWith(TField field, String value,
			boolean ignoreCase) {
		return new FilterCriteria<TField, String>(field, FilterOperator.ENDS_WITH, List.of(value), ignoreCase);
	}

	/**
	 * Creates a {@link FilterOperator#BETWEEN} criteria matching the inclusive
	 * range {@code [low, high]}.
	 *
	 * @param <TField>
	 *            the field enum type
	 * @param <TValue>
	 *            the comparable value type
	 * @param field
	 *            the field to match
	 * @param low
	 *            the inclusive lower bound
	 * @param high
	 *            the inclusive upper bound
	 * @return the criteria
	 */
	public static <TField extends Enum<TField>, TValue extends Comparable<TValue>> FilterCriteria<TField, TValue> between(
			TField field, TValue low, TValue high) {
		return new FilterCriteria<TField, TValue>(field, FilterOperator.BETWEEN, List.of(low, high));
	}

	/**
	 * Creates a {@link FilterOperator#NOT_BETWEEN} criteria matching values outside
	 * the inclusive range {@code [low, high]}.
	 *
	 * @param <TField>
	 *            the field enum type
	 * @param <TValue>
	 *            the comparable value type
	 * @param field
	 *            the field to match
	 * @param low
	 *            the inclusive lower bound of the excluded range
	 * @param high
	 *            the inclusive upper bound of the excluded range
	 * @return the criteria
	 */
	public static <TField extends Enum<TField>, TValue extends Comparable<TValue>> FilterCriteria<TField, TValue> notBetween(
			TField field, TValue low, TValue high) {
		return new FilterCriteria<TField, TValue>(field, FilterOperator.NOT_BETWEEN, List.of(low, high));
	}

	/**
	 * Creates an {@link FilterOperator#IS_NULL} criteria, which carries no value.
	 *
	 * @param <TField>
	 *            the field enum type
	 * @param field
	 *            the field that must be {@code null}
	 * @return the criteria
	 */
	public static <TField extends Enum<TField>> FilterCriteria<TField, Void> isNull(TField field) {
		return new FilterCriteria<TField, Void>(field, FilterOperator.IS_NULL, List.of());
	}

	/**
	 * Creates an {@link FilterOperator#IS_NOT_NULL} criteria, which carries no
	 * value.
	 *
	 * @param <TField>
	 *            the field enum type
	 * @param field
	 *            the field that must not be {@code null}
	 * @return the criteria
	 */
	public static <TField extends Enum<TField>> FilterCriteria<TField, Void> isNotNull(TField field) {
		return new FilterCriteria<TField, Void>(field, FilterOperator.IS_NOT_NULL, List.of());
	}

	/**
	 * Returns the field this criteria applies to.
	 *
	 * @return the field
	 */
	public TField getField() {
		return field;
	}

	/**
	 * Returns the operator used to match the field.
	 *
	 * @return the operator
	 */
	public FilterOperator getOperator() {
		return operator;
	}

	/**
	 * Returns the immutable list of operand values.
	 *
	 * <p>
	 * The list is empty for
	 * {@link FilterOperator#IS_NULL}/{@link FilterOperator#IS_NOT_NULL}, holds one
	 * element for single-value operators, two for
	 * {@code BETWEEN}/{@code NOT_BETWEEN}, and any number for
	 * {@code IN}/{@code NOT_IN}.
	 *
	 * @return the operand values; never {@code null}
	 */
	public List<TValue> getValues() {
		return values;
	}

	/**
	 * Returns the first operand value, a convenience for single-value operators.
	 *
	 * @return the first value, or {@code null} if this criteria carries no values
	 */
	public TValue getValue() {
		return values.isEmpty() ? null : values.get(0);
	}

	/**
	 * Indicates whether text matching should ignore case.
	 *
	 * <p>
	 * Only meaningful for the text operators; always {@code false} for the others.
	 *
	 * @return {@code true} if the match is case-insensitive
	 */
	public boolean isIgnoreCase() {
		return ignoreCase;
	}
}
