package com.webstudio.query.manager._base.application.filter;

import java.util.Set;

/**
 * A {@link FiltrableField} that additionally restricts which
 * {@link FilterOperator operators} may be applied to it.
 *
 * <p>
 * Input-side field enums typically implement this interface so that a use case
 * can reject a query that uses an operator the field does not support. The
 * enforcement is performed by {@link FilterOperatorRestrictionValidator}, which
 * is invoked automatically by the query interactors.
 *
 * @param <Type>
 *            the Java type of the values held by this field
 */
public interface RestrictedFiltrableField<Type> extends FiltrableField<Type> {

	/**
	 * Returns the set of operators that are permitted for this field.
	 *
	 * <p>
	 * A {@link FilterCriteria} whose operator is not contained in this set is
	 * considered invalid and causes validation to fail.
	 *
	 * @return the allowed operators; never {@code null}
	 */
	Set<FilterOperator> getAllowedOperators();
}
