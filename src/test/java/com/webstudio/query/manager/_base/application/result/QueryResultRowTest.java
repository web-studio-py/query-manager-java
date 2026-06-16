package com.webstudio.query.manager._base.application.result;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

class QueryResultRowTest {

	/** Enum used only to parameterize {@link QueryResultRow}; never read back. */
	enum DummyField implements SelectableField<Object> {
		UNUSED;

		@Override
		public Class<Object> getFieldType() {
			return Object.class;
		}
	}

	/**
	 * Standalone typed field, so {@code get(...)} can be exercised for type safety.
	 */
	static final class TypedField<T> implements SelectableField<T> {
		private final Class<T> type;

		TypedField(Class<T> type) {
			this.type = type;
		}

		@Override
		public Class<T> getFieldType() {
			return type;
		}
	}

	static final SelectableField<String> NAME = new TypedField<>(String.class);
	static final SelectableField<Integer> AGE = new TypedField<>(Integer.class);

	@Test
	void getReturnsValueCastToFieldType() {
		QueryResultRow<DummyField> row = new QueryResultRow<>(Map.of(NAME, "john", AGE, 42));

		String name = row.get(NAME);
		Integer age = row.get(AGE);

		assertEquals("john", name);
		assertEquals(42, age);
	}

	@Test
	void getReturnsNullForAbsentField() {
		QueryResultRow<DummyField> row = new QueryResultRow<>(Map.of(NAME, "john"));

		assertNull(row.get(AGE));
	}

	@Test
	void rowIsNotAffectedByLaterMutationOfSourceMap() {
		Map<SelectableField<?>, Object> source = new HashMap<>();
		source.put(NAME, "john");

		QueryResultRow<DummyField> row = new QueryResultRow<>(source);
		source.put(AGE, 42);

		assertEquals("john", row.get(NAME));
		assertNull(row.get(AGE));
	}
}
