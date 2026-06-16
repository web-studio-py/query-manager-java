package com.webstudio.query.manager._base.application.filter;

import com.webstudio.layered.architecture.application.exceptions.ApplicationBusinessRuleException;

/**
 * Validates that every {@link FilterCriteria} in a filter tree uses an operator
 * permitted by its field.
 *
 * <p>
 * The validator walks the whole {@link FilterNode} tree and, for each leaf
 * whose field is a {@link RestrictedFiltrableField}, checks the criteria's
 * {@link FilterOperator} against the field's
 * {@link RestrictedFiltrableField#getAllowedOperators() allowed operators}.
 * Fields that are not restricted are accepted unconditionally. The query
 * interactors invoke this validator automatically; it is also a static utility
 * usable on its own.
 */
public final class FilterOperatorRestrictionValidator {

	private FilterOperatorRestrictionValidator() {
	}

	/**
	 * Validates the operators used throughout the given filter.
	 *
	 * @param filters
	 *            the filter to validate; a {@code null} or {@link Filters#isEmpty()
	 *            empty} filter is considered valid
	 * @throws ApplicationBusinessRuleException
	 *             if any criteria uses an operator not allowed by its
	 *             {@link RestrictedFiltrableField}
	 */
	public static void validate(Filters<?> filters) {
		if (filters == null || filters.isEmpty()) {
			return;
		}
		validateNode(filters.getRoot());
	}

	private static void validateNode(FilterNode<?> node) {
		if (node instanceof FilterGroup<?> group) {
			for (FilterNode<?> child : group) {
				validateNode(child);
			}
		} else if (node instanceof FilterCriteria<?, ?> criteria) {
			validateCriteria(criteria);
		}
	}

	private static void validateCriteria(FilterCriteria<?, ?> criteria) {
		Enum<?> field = criteria.getField();
		if (field instanceof RestrictedFiltrableField<?> filtrable
				&& !filtrable.getAllowedOperators().contains(criteria.getOperator())) {
			throw new ApplicationBusinessRuleException(
					String.format("Filter operator %s is not allowed for field %s", criteria.getOperator(), field));
		}
	}
}
