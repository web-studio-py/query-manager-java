package com.webstudio.query.manager._base.application.result;

/**
 * Contract for an enum constant that may be selected as an output column of a
 * query.
 *
 * <p>
 * Clients model the selectable fields of a query as an enum whose constants
 * implement this interface, exposing the Java type of the value stored for that
 * field in a {@link QueryResultRow}. The declared type enables the type-safe
 * {@link QueryResultRow#get(SelectableField)} accessor.
 *
 * @param <Type>
 *            the Java type of the value held by this field in a result row
 */
public interface SelectableField<Type> {

	/**
	 * Returns the runtime type of the value held by this field.
	 *
	 * @return the field value type; never {@code null}
	 */
	Class<Type> getFieldType();
}
