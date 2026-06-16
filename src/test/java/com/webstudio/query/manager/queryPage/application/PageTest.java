package com.webstudio.query.manager.queryPage.application;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

class PageTest {

	@Test
	void exposesItemsAndPaginationMetadata() {
		List<String> items = List.of("a", "b", "c");

		Page<String> page = new Page<>(items, 1, 3, 7L, 3);

		assertEquals(items, page.getPageListItem());
		assertEquals(1, page.getPage());
		assertEquals(3, page.getSize());
		assertEquals(7L, page.getTotalPageListItems());
		assertEquals(3, page.getTotalPages());
	}
}
