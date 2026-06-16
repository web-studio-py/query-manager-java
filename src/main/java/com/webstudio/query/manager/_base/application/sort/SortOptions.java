package com.webstudio.query.manager._base.application.sort;

import java.util.Iterator;
import java.util.List;

/**
 * An ordered, immutable collection of {@link SortOption sort options} applied
 * to a query.
 *
 * <p>
 * The options are evaluated in iteration order, so the first option is the
 * primary sort key, the second a tie-breaker, and so on. Create instances
 * through the {@link #of(List)}, {@link #of(Enum, SortDirection)} and
 * {@link #empty()} factory methods; the list is copied defensively.
 *
 * @param <TField>
 *            the enum type identifying the sortable fields of a query
 */
public class SortOptions<TField extends Enum<TField>> implements Iterable<SortOption<TField>> {

	private final List<SortOption<TField>> options;

	private SortOptions(List<SortOption<TField>> options) {
		this.options = options;
	}

	/**
	 * Creates sort options from the given list, in priority order.
	 *
	 * @param <TField>
	 *            the field enum type
	 * @param options
	 *            the sort options; {@code null} or empty yields {@link #empty()}
	 * @return the resulting sort options
	 */
	public static <TField extends Enum<TField>> SortOptions<TField> of(List<SortOption<TField>> options) {
		if (options == null || options.isEmpty())
			return empty();
		return new SortOptions<TField>(List.copyOf(options));
	}

	/**
	 * Creates sort options with a single field and direction.
	 *
	 * @param <TField>
	 *            the field enum type
	 * @param field
	 *            the field to order by
	 * @param direction
	 *            the order direction
	 * @return the resulting sort options
	 */
	public static <TField extends Enum<TField>> SortOptions<TField> of(TField field, SortDirection direction) {
		return new SortOptions<TField>(List.of(new SortOption<TField>(field, direction)));
	}

	/**
	 * Returns sort options with no entries, leaving ordering unspecified.
	 *
	 * @param <TField>
	 *            the field enum type
	 * @return empty sort options
	 */
	public static <TField extends Enum<TField>> SortOptions<TField> empty() {
		return new SortOptions<TField>(List.of());
	}

	/**
	 * Indicates whether there are no sort options.
	 *
	 * @return {@code true} if empty
	 */
	public boolean isEmpty() {
		return options.isEmpty();
	}

	/**
	 * Returns the number of sort options.
	 *
	 * @return the option count
	 */
	public int size() {
		return options.size();
	}

	/**
	 * Returns the immutable list of sort options, in priority order.
	 *
	 * @return the options; never {@code null}
	 */
	public List<SortOption<TField>> getOptions() {
		return options;
	}

	/**
	 * Returns an iterator over the sort options, in priority order.
	 *
	 * @return an iterator over the options
	 */
	@Override
	public Iterator<SortOption<TField>> iterator() {
		return options.iterator();
	}
}
