package com.webstudio.query.manager._base.application.filter;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

class FiltersTest {

	enum TestField {
		NAME, AGE;
	}

	@Test
	void emptyHasNoRoot() {
		Filters<TestField> filters = Filters.empty();

		assertTrue(filters.isEmpty());
		assertNull(filters.getRoot());
	}

	@Test
	void andWithNullNodesYieldsEmpty() {
		Filters<TestField> filters = Filters.and(null);

		assertTrue(filters.isEmpty());
	}

	@Test
	void orWithEmptyNodesYieldsEmpty() {
		Filters<TestField> filters = Filters.or(List.of());

		assertTrue(filters.isEmpty());
	}

	@Test
	void singleNodeIsUsedAsRootWithoutWrapping() {
		FilterNode<TestField> node = FilterCriteria.equal(TestField.NAME, "john");

		Filters<TestField> filters = Filters.and(List.of(node));

		assertFalse(filters.isEmpty());
		assertSame(node, filters.getRoot());
	}

	@Test
	void andWithMultipleNodesWrapsInAndGroup() {
		FilterNode<TestField> first = FilterCriteria.equal(TestField.NAME, "john");
		FilterNode<TestField> second = FilterCriteria.greaterThan(TestField.AGE, 18);

		Filters<TestField> filters = Filters.and(List.of(first, second));

		FilterGroup<TestField> root = assertInstanceOf(FilterGroup.class, filters.getRoot());
		assertSame(LogicalOperator.AND, root.getOperator());
		assertSame(first, root.getChildren().get(0));
		assertSame(second, root.getChildren().get(1));
	}

	@Test
	void orWithMultipleNodesWrapsInOrGroup() {
		FilterNode<TestField> first = FilterCriteria.equal(TestField.NAME, "john");
		FilterNode<TestField> second = FilterCriteria.equal(TestField.NAME, "jane");

		Filters<TestField> filters = Filters.or(List.of(first, second));

		FilterGroup<TestField> root = assertInstanceOf(FilterGroup.class, filters.getRoot());
		assertSame(LogicalOperator.OR, root.getOperator());
	}
}
