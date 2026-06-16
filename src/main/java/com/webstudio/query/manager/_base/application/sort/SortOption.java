package com.webstudio.query.manager._base.application.sort;

/**
 * A single sort instruction: a field paired with a {@link SortDirection}.
 *
 * <p>
 * Several options are combined, in priority order, by a {@link SortOptions}
 * instance.
 *
 * @param <TField>
 *            the enum type identifying the sortable fields of a query
 */
public class SortOption<TField extends Enum<TField>> {
	private TField field;
	private SortDirection direction;

	/**
	 * Creates a sort option for the given field and direction.
	 *
	 * @param field
	 *            the field to order by
	 * @param direction
	 *            the order direction
	 */
	public SortOption(TField field, SortDirection direction) {
		this.field = field;
		this.direction = direction;
	}

	/**
	 * Returns the field to order by.
	 *
	 * @return the field
	 */
	public TField getField() {
		return field;
	}

	/**
	 * Returns the order direction.
	 *
	 * @return the direction
	 */
	public SortDirection getDirection() {
		return direction;
	}
}
