package util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Helper class for a mapping to sets.
 * 
 * @param <A>
 *            Type of keys.
 * @param <B>
 *            Type of values in sets.
 */
public class MapSet<A, B> extends HashMap<A, Set<B>> {

	private static final long serialVersionUID = 1L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.HashMap#get(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public Set<B> get(Object key) {
		Set<B> set = super.get(key);
		if (set == null) {
			set = new HashSet<B>();
			super.put((A) key, set);
		}
		return set;
	}

	/**
	 * Add element to set for given key.
	 * 
	 * @param key
	 *            Key
	 * @param value
	 *            Value to add to set.
	 */
	public void add(A key, B value) {
		get(key).add(value);
	}

	/**
	 * Checks if the set for the given key contains the given value.
	 * 
	 * @param key
	 *            Key
	 * @param value
	 *            Value
	 * @return True iff value is contained in the set for key
	 */
	public boolean contains(A key, B value) {
		return get(key).contains(value);
	}

}
