package com.webstudio.query.manager._base.application.filter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class FilterGroupTest {

	enum TestField {
		NAME, AGE;
	}

	@Test
	void andExposesAndOperatorAndChildren() {
		FilterNode<TestField> first = FilterCriteria.equal(TestField.NAME, "john");
		FilterNode<TestField> second = FilterCriteria.greaterThan(TestField.AGE, 18);

		FilterGroup<TestField> group = FilterGroup.and(List.of(first, second));

		assertSame(LogicalOperator.AND, group.getOperator());
		assertEquals(List.of(first, second), group.getChildren());
	}

	@Test
	void orExposesOrOperator() {
		FilterGroup<TestField> group = FilterGroup.or(List.of(FilterCriteria.equal(TestField.NAME, "john")));

		assertSame(LogicalOperator.OR, group.getOperator());
	}

	@Test
	void iteratesOverChildrenInOrder() {
		FilterNode<TestField> first = FilterCriteria.equal(TestField.NAME, "john");
		FilterNode<TestField> second = FilterCriteria.equal(TestField.NAME, "jane");

		FilterGroup<TestField> group = FilterGroup.and(List.of(first, second));

		List<FilterNode<TestField>> iterated = new ArrayList<>();
		for (FilterNode<TestField> child : group) {
			iterated.add(child);
		}

		assertEquals(List.of(first, second), iterated);
	}

	@Test
	void defensivelyCopiesChildrenFromSource() {
		List<FilterNode<TestField>> source = new ArrayList<>();
		source.add(FilterCriteria.equal(TestField.NAME, "john"));

		FilterGroup<TestField> group = FilterGroup.and(source);
		source.add(FilterCriteria.equal(TestField.NAME, "jane"));

		assertEquals(1, group.getChildren().size());
	}

	@Test
	void exposedChildrenAreImmutable() {
		FilterGroup<TestField> group = FilterGroup.and(List.of(FilterCriteria.equal(TestField.NAME, "john")));

		assertThrows(UnsupportedOperationException.class,
				() -> group.getChildren().add(FilterCriteria.equal(TestField.NAME, "jane")));
	}
}
