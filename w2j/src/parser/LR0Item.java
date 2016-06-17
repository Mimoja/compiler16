package parser;

import java.util.Arrays;

import symbols.Alphabet;
import symbols.NonTerminals.NonTerminal;
import symbols.Tokens.Token;

/**
 * Represents an LR(0) item as lhs -> first MARKER last
 */
public class LR0Item extends Rule {

	// Marker points to index i which is the first symbol after the marker.
	private final int marker;

	/**
	 * Constructor.
	 * 
	 * @param lhs
	 *            Non-terminal on left-hand side.
	 * @param rhs
	 *            Right-hand side.
	 * @param marker
	 *            Position of marker.
	 */
	public LR0Item(NonTerminal lhs, Alphabet[] rhs, int marker) {
		super(lhs, rhs);
		this.marker = marker;
	}

	/**
	 * Check if a shift over a terminal is possible.
	 * 
	 * @return True iff the marker is followed by a terminal symbol
	 */
	public boolean canShiftOverTerminal() {
		return marker < getRhs().length && getRhs()[marker] instanceof Token;
	}

	/**
	 * Check if a reduce is possible.
	 * 
	 * @return True iff there comes nothing after the marker
	 */
	public boolean canReduce() {
		return marker == getRhs().length;
	}

	/**
	 * Check if the marker can be shifted further. Equivalently, if it cannot
	 * this item is complete and marker = rhs.length.
	 * 
	 * @return True iff the marker can be shifted
	 */
	public boolean canShift() {
		return marker < getRhs().length;
	}

	/**
	 * Get the non-terminal after the marker.
	 * 
	 * @return The non-terminal after the marker if any otherwise null
	 */
	public NonTerminal getNextNonTerminal() {
		if (marker < getRhs().length && getRhs()[marker] instanceof NonTerminal)
			return (NonTerminal) getRhs()[marker];
		else
			return null;
	}

	/**
	 * Get new item after one shift.
	 * 
	 * @return New LR(0) item
	 */
	public LR0Item getShiftedItem() {
		assert (canShift());
		return new LR0Item(getLhs(), getRhs(), marker + 1);
	}

	/**
	 * Given a grammar production rule like S -> alpha return an item [S ->
	 * .alpha]
	 * 
	 * @param rule
	 *            Rule
	 * @return LR(0) item created from rule.
	 */
	public static LR0Item freshItem(Rule rule) {
		return new LR0Item(rule.getLhs(), rule.getRhs(), 0);
	}

	/**
	 * Get the symbol after the marker.
	 * 
	 * @return The symbol after the marker if there is one else null
	 */
	public Alphabet getShiftableSymbolName() {
		if (0 <= marker && marker < getRhs().length) {
			return getRhs()[marker];
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		int hashFirst = getLhs().hashCode();
		int hashSecond = Arrays.hashCode(getRhs());

		return (hashFirst + hashSecond) * hashSecond + hashFirst;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object a) {
		if (a instanceof LR0Item) {
			LR0Item other = (LR0Item) a;
			return other.getLhs().equals(getLhs()) && other.marker == marker && Arrays.equals(other.getRhs(), getRhs());
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("[ ");
		result.append(getLhs());
		result.append(" -> ");
		for (int i = 0; i < marker; i++) {
			result.append(getRhs()[i]);
			result.append(" ");
		}
		result.append(". ");
		for (int i = marker; i < getRhs().length; i++) {
			result.append(getRhs()[i]);
			result.append(" ");
		}
		result.append("]");
		return result.toString();
	}
}
