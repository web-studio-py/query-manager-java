package com.webstudio.query.manager.queryPage.application;

import java.util.List;

/**
 * A single page of results together with its pagination metadata.
 *
 * <p>
 * Besides the items on the page, it carries the current page index, the page
 * size, the total number of items across all pages, and the total number of
 * pages, so callers can render pagination controls without a second query.
 *
 * @param <TPageListItem>
 *            the type of the items contained in the page
 */
public class Page<TPageListItem> {

	private final List<TPageListItem> pageListItem;
	private final Integer page;
	private final Integer size;
	private final Long totalPageListItems;
	private final Integer totalPages;

	/**
	 * Creates a page with its items and pagination metadata.
	 *
	 * @param pageListItem
	 *            the items on this page
	 * @param page
	 *            the zero-based index of this page
	 * @param size
	 *            the page size used for the query
	 * @param totalPageListItems
	 *            the total number of items across all pages
	 * @param totalPages
	 *            the total number of pages
	 */
	public Page(List<TPageListItem> pageListItem, Integer page, Integer size, Long totalPageListItems,
			Integer totalPages) {
		this.pageListItem = pageListItem;
		this.page = page;
		this.size = size;
		this.totalPageListItems = totalPageListItems;
		this.totalPages = totalPages;
	}

	/**
	 * Returns the items contained in this page.
	 *
	 * @return the page items
	 */
	public List<TPageListItem> getPageListItem() {
		return pageListItem;
	}

	/**
	 * Returns the zero-based index of this page.
	 *
	 * @return the page index
	 */
	public Integer getPage() {
		return page;
	}

	/**
	 * Returns the page size used for the query.
	 *
	 * @return the page size
	 */
	public Integer getSize() {
		return size;
	}

	/**
	 * Returns the total number of items across all pages.
	 *
	 * @return the total item count
	 */
	public Long getTotalPageListItems() {
		return totalPageListItems;
	}

	/**
	 * Returns the total number of pages.
	 *
	 * @return the total page count
	 */
	public Integer getTotalPages() {
		return totalPages;
	}
}
