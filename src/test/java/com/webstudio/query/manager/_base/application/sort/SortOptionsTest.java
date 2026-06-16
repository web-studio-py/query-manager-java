package com.webstudio.query.manager._base.application.sort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class SortOptionsTest {

	enum TestField {
		NAME, AGE;
	}

	@Test
	void emptyHasNoOptions() {
		SortOptions<TestField> options = SortOptions.empty();

		assertTrue(options.isEmpty());
		assertEquals(0, options.size());
	}

	@Test
	void ofNullListYieldsEmpty() {
		SortOptions<TestField> options = SortOptions.of((List<SortOption<TestField>>) null);

		assertTrue(options.isEmpty());
	}

	@Test
	void ofFieldAndDirectionHoldsSingleOption() {
		SortOptions<TestField> options = SortOptions.of(TestField.NAME, SortDirection.ASC);

		assertEquals(1, options.size());
		SortOption<TestField> only = options.getOptions().get(0);
		assertSame(TestField.NAME, only.getField());
		assertSame(SortDirection.ASC, only.getDirection());
	}

	@Test
	void ofListPreservesOrder() {
		SortOption<TestField> primary = new SortOption<>(TestField.NAME, SortDirection.ASC);
		SortOption<TestField> secondary = new SortOption<>(TestField.AGE, SortDirection.DESC);

		SortOptions<TestField> options = SortOptions.of(List.of(primary, secondary));

		assertFalse(options.isEmpty());
		assertEquals(List.of(primary, secondary), options.getOptions());
	}

	@Test
	void iteratesInPriorityOrder() {
		SortOption<TestField> primary = new SortOption<>(TestField.NAME, SortDirection.ASC);
		SortOption<TestField> secondary = new SortOption<>(TestField.AGE, SortDirection.DESC);

		SortOptions<TestField> options = SortOptions.of(List.of(primary, secondary));

		List<SortOption<TestField>> iterated = new ArrayList<>();
		options.forEach(iterated::add);

		assertEquals(List.of(primary, secondary), iterated);
	}

	@Test
	void exposedOptionsAreImmutable() {
		SortOptions<TestField> options = SortOptions.of(TestField.NAME, SortDirection.ASC);

		assertThrows(UnsupportedOperationException.class,
				() -> options.getOptions().add(new SortOption<>(TestField.AGE, SortDirection.DESC)));
	}
}
