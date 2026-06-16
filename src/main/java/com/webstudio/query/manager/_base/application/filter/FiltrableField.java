package com.webstudio.query.manager._base.application.filter;

/**
 * Marker contract for an enum constant that may be used as the target of a
 * {@link FilterCriteria}.
 *
 * <p>
 * Clients model the filterable fields of a query as an enum whose constants
 * implement this interface, exposing the Java type of the values that can be
 * compared against the field. Use {@link RestrictedFiltrableField} instead when
 * a field must also limit which operators are allowed.
 *
 * @param <Type>
 *            the Java type of the values held by this field
 */
public interface FiltrableField<Type> {

	/**
	 * Returns the runtime type of the values held by this field.
	 *
	 * @return the field value type; never {@code null}
	 */
	Class<Type> getFieldType();
}
