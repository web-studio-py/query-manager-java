package com.webstudio.query.manager._base.application.filter;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.webstudio.layered.architecture.application.exceptions.ApplicationBusinessRuleException;

class FilterOperatorRestrictionValidatorTest {

	enum TestField implements RestrictedFiltrableField<String> {
		NAME;

		@Override
		public Class<String> getFieldType() {
			return String.class;
		}

		@Override
		public Set<FilterOperator> getAllowedOperators() {
			return Set.of(FilterOperator.EQUAL, FilterOperator.CONTAINS);
		}
	}

	@Test
	void allowsCriteriaWithPermittedOperator() {
		Filters<TestField> filters = Filters.and(List.of(FilterCriteria.equal(TestField.NAME, "john")));

		assertDoesNotThrow(() -> FilterOperatorRestrictionValidator.validate(filters));
	}

	@Test
	void rejectsCriteriaWithForbiddenOperator() {
		Filters<TestField> filters = Filters.and(List.of(FilterCriteria.greaterThan(TestField.NAME, "john")));

		assertThrows(ApplicationBusinessRuleException.class,
				() -> FilterOperatorRestrictionValidator.validate(filters));
	}

	@Test
	void rejectsForbiddenOperatorNestedInGroup() {
		FilterNode<TestField> allowed = FilterCriteria.equal(TestField.NAME, "john");
		FilterNode<TestField> forbidden = FilterCriteria.startsWith(TestField.NAME, "j");
		FilterNode<TestField> deepForbidden = FilterCriteria.lessThan(TestField.NAME, "z");

		Filters<TestField> filters = Filters.and(List.of(allowed, FilterGroup.or(List.of(forbidden, deepForbidden))));

		assertThrows(ApplicationBusinessRuleException.class,
				() -> FilterOperatorRestrictionValidator.validate(filters));
	}

	@Test
	void allowsEmptyFilters() {
		assertDoesNotThrow(() -> FilterOperatorRestrictionValidator.validate(Filters.<TestField>empty()));
	}

	@Test
	void allowsNullFilters() {
		assertDoesNotThrow(() -> FilterOperatorRestrictionValidator.validate(null));
	}
}
