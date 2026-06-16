package com.webstudio.query.manager._base.application.filter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

class FilterCriteriaTest {

	enum TestField {
		NAME, AGE;
	}

	@Test
	void equalStoresOperatorAndValue() {
		FilterCriteria<TestField, String> criteria = FilterCriteria.equal(TestField.NAME, "john");

		assertEquals(TestField.NAME, criteria.getField());
		assertEquals(FilterOperator.EQUAL, criteria.getOperator());
		assertEquals("john", criteria.getValue());
		assertFalse(criteria.isIgnoreCase());
	}

	@Test
	void notEqualStoresOperatorAndValue() {
		FilterCriteria<TestField, String> criteria = FilterCriteria.notEqual(TestField.NAME, "john");

		assertEquals(FilterOperator.NOT_EQUAL, criteria.getOperator());
		assertEquals("john", criteria.getValue());
	}

	@Test
	void greaterThanStoresOperatorAndValue() {
		FilterCriteria<TestField, Integer> criteria = FilterCriteria.greaterThan(TestField.AGE, 18);

		assertEquals(FilterOperator.GREATER_THAN, criteria.getOperator());
		assertEquals(18, criteria.getValue());
	}

	@Test
	void greaterThanOrEqualStoresOperatorAndValue() {
		FilterCriteria<TestField, Integer> criteria = FilterCriteria.greaterThanOrEqual(TestField.AGE, 18);

		assertEquals(FilterOperator.GREATER_THAN_OR_EQUAL, criteria.getOperator());
		assertEquals(18, criteria.getValue());
	}

	@Test
	void lessThanStoresOperatorAndValue() {
		FilterCriteria<TestField, Integer> criteria = FilterCriteria.lessThan(TestField.AGE, 65);

		assertEquals(FilterOperator.LESS_THAN, criteria.getOperator());
		assertEquals(65, criteria.getValue());
	}

	@Test
	void lessThanOrEqualStoresOperatorAndValue() {
		FilterCriteria<TestField, Integer> criteria = FilterCriteria.lessThanOrEqual(TestField.AGE, 65);

		assertEquals(FilterOperator.LESS_THAN_OR_EQUAL, criteria.getOperator());
		assertEquals(65, criteria.getValue());
	}

	@Test
	void inStoresAllValues() {
		FilterCriteria<TestField, Integer> criteria = FilterCriteria.in(TestField.AGE, List.of(18, 30, 65));

		assertEquals(FilterOperator.IN, criteria.getOperator());
		assertEquals(List.of(18, 30, 65), criteria.getValues());
	}

	@Test
	void notInStoresAllValues() {
		FilterCriteria<TestField, Integer> criteria = FilterCriteria.notIn(TestField.AGE, List.of(18, 30));

		assertEquals(FilterOperator.NOT_IN, criteria.getOperator());
		assertEquals(List.of(18, 30), criteria.getValues());
	}

	@Test
	void isNullCarriesNoValue() {
		FilterCriteria<TestField, Void> criteria = FilterCriteria.isNull(TestField.NAME);

		assertEquals(FilterOperator.IS_NULL, criteria.getOperator());
		assertTrue(criteria.getValues().isEmpty());
		assertNull(criteria.getValue());
	}

	@Test
	void isNotNullCarriesNoValue() {
		FilterCriteria<TestField, Void> criteria = FilterCriteria.isNotNull(TestField.NAME);

		assertEquals(FilterOperator.IS_NOT_NULL, criteria.getOperator());
		assertTrue(criteria.getValues().isEmpty());
		assertNull(criteria.getValue());
	}

	@Test
	void containsStoresSingleValueAndDefaultsToCaseSensitive() {
		FilterCriteria<TestField, String> criteria = FilterCriteria.contains(TestField.NAME, "jo");

		assertEquals(FilterOperator.CONTAINS, criteria.getOperator());
		assertEquals(List.of("jo"), criteria.getValues());
		assertFalse(criteria.isIgnoreCase());
	}

	@Test
	void notContainsCarriesIgnoreCaseFlag() {
		FilterCriteria<TestField, String> criteria = FilterCriteria.notContains(TestField.NAME, "jo", true);

		assertEquals(FilterOperator.NOT_CONTAINS, criteria.getOperator());
		assertTrue(criteria.isIgnoreCase());
	}

	@Test
	void startsWithStoresOperatorAndValue() {
		FilterCriteria<TestField, String> criteria = FilterCriteria.startsWith(TestField.NAME, "j", true);

		assertEquals(FilterOperator.STARTS_WITH, criteria.getOperator());
		assertEquals("j", criteria.getValue());
		assertTrue(criteria.isIgnoreCase());
	}

	@Test
	void endsWithStoresOperatorAndValue() {
		FilterCriteria<TestField, String> criteria = FilterCriteria.endsWith(TestField.NAME, "n");

		assertEquals(FilterOperator.ENDS_WITH, criteria.getOperator());
		assertEquals("n", criteria.getValue());
		assertFalse(criteria.isIgnoreCase());
	}

	@Test
	void betweenStoresLowAndHighInOrder() {
		FilterCriteria<TestField, Integer> criteria = FilterCriteria.between(TestField.AGE, 18, 65);

		assertEquals(FilterOperator.BETWEEN, criteria.getOperator());
		assertEquals(List.of(18, 65), criteria.getValues());
		assertEquals(18, criteria.getValues().get(0));
		assertEquals(65, criteria.getValues().get(1));
	}

	@Test
	void notBetweenStoresLowAndHighInOrder() {
		FilterCriteria<TestField, Integer> criteria = FilterCriteria.notBetween(TestField.AGE, 18, 65);

		assertEquals(FilterOperator.NOT_BETWEEN, criteria.getOperator());
		assertEquals(List.of(18, 65), criteria.getValues());
	}
}
