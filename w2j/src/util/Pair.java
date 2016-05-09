package util;

/**
 * Helper class for a pair.
 *
 * @param <A>
 *            Type of first element.
 * @param <B>
 *            Type of second element.
 */
public class Pair<A, B> {
	private A first;
	private B second;

	/**
	 * Constructor.
	 * 
	 * @param first
	 *            First element.
	 * @param second
	 *            Second element.
	 */
	public Pair(A first, B second) {
		super();
		this.first = first;
		this.second = second;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		int hashFirst = first != null ? first.hashCode() : 0;
		int hashSecond = second != null ? second.hashCode() : 0;

		return (hashFirst + hashSecond) * hashSecond + hashFirst;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public boolean equals(Object other) {
		if (other instanceof Pair) {
			Pair<A, B> otherPair = (Pair<A, B>) other;
			return ((this.first == otherPair.first
					|| (this.first != null && otherPair.first != null && this.first.equals(otherPair.first)))
					&& (this.second == otherPair.second || (this.second != null && otherPair.second != null
							&& this.second.equals(otherPair.second))));
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "(" + first + ", " + second + ")";
	}

	/**
	 * Get first element.
	 * 
	 * @return First element.
	 */
	public A getFirst() {
		return first;
	}

	/**
	 * Get second element.
	 * 
	 * @return Second element.
	 */
	public B getSecond() {
		return second;
	}
}
