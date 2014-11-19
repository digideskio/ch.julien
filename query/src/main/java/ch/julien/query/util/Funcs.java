package ch.julien.query.util;

import ch.julien.common.delegate.Func;


/**
 * Common {@link Func} implementations for convenience
 */
public class Funcs {

	/**
	 * Cast the element to <code>clazz</code>
	 */
	public static final <T> Func<Object, T> to(final Class<T> clazz) {
		return new Func<Object, T>() {
			@SuppressWarnings("unchecked")
			@Override public T invoke(Object arg) {
				return (T) arg;
			}
		};
	}

	/**
	 * Replace all matches for the <code>regex</code> with a <code>replacement</code>
	 *
	 * @see String#replaceAll(String, String)
	 */
	public static final Func<String, String> replaceAll(final String regex, final String replacement) {
		return new Func<String, String>() {
			@Override
			public String invoke(String arg) {
				return arg.replaceAll(regex, replacement);
			}
		};
	}

	/**
	 * Trim the string
	 *
	 * @see String#trim()
	 */
	public static final Func<String, String> trimString() {
		return new Func<String, String>() {
			@Override
			public String invoke(String arg) {
				return arg.trim();
			}
		};
	}

	/**
	 * To string
	 *
	 * @see Object#toString()
	 */
	public static final Func<Object, String> toStringRepresentation() {
		return new Func<Object, String>() {
			@Override
			public String invoke(Object arg) {
				return arg.toString();
			}
		};
	}

	/**
	 * Parse the element to an {@link Integer}
	 *
	 * @see Integer#parseInt(String)
	 */
	public static final Func<String, Integer> parseInteger() {
		return new Func<String, Integer>() {
			@Override
			public Integer invoke(String arg) {
				return Integer.parseInt(arg);
			}
		};
	}

	/**
	 * Parse the element to a {@link Long}
	 *
	 * @see Long#parseLong(String)
	 */
	public static final Func<String, Long> parseLong() {
		return new Func<String, Long>() {
			@Override
			public Long invoke(String arg) {
				return Long.parseLong(arg);
			}
		};
	}

	/**
	 * Parse the element to a {@link Double}
	 *
	 * @see Double#parseDouble(String)
	 */
	public static final Func<String, Double> parseDouble() {
		return new Func<String, Double>() {
			@Override
			public Double invoke(String arg) {
				return Double.parseDouble(arg);
			}
		};
	}

	/**
	 * The unmodified element
	 */
	public static final <T> Func<T, T> self() {
		return new Func<T, T>() {
			@Override
			public T invoke(T arg) {
				return arg;
			}
		};
	}

}
