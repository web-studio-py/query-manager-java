package com.webstudio.query.manager._base.application.result;

import java.util.Map;

/**
 * A single row of a query result, mapping each selected field to its value.
 *
 * <p>
 * The row is immutable: the supplied map is defensively copied with
 * {@link Map#copyOf}. Values are read back through the type-safe
 * {@link #get(SelectableField)} accessor, which casts each value to the type
 * declared by the field, so callers never need an unchecked cast.
 *
 * <p>
 * Example:
 *
 * <pre>{@code
 * String name = row.get(UserField.NAME);
 * Integer age = row.get(UserField.AGE);
 * }</pre>
 *
 * @param <TSelectableField>
 *            the enum type identifying the selectable fields of the query
 */
public class QueryResultRow<TSelectableField extends Enum<TSelectableField> & SelectableField<?>> {

	private final Map<SelectableField<?>, Object> values;

	/**
	 * Creates a row from the given field-to-value mapping.
	 *
	 * @param values
	 *            the field values; copied defensively
	 */
	public QueryResultRow(Map<SelectableField<?>, Object> values) {
		this.values = Map.copyOf(values);
	}

	/**
	 * Returns the value of the given field, cast to its declared type.
	 *
	 * @param <T>
	 *            the value type declared by the field
	 * @param field
	 *            the field to read
	 * @return the field value, or {@code null} if the row has no value for it
	 */
	public <T> T get(SelectableField<T> field) {
		return field.getFieldType().cast(values.get(field));
	}
}
