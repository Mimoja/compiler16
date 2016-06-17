package parser;

import java.util.HashSet;
import java.util.Iterator;

import symbols.Alphabet;
import symbols.NonTerminals.NonTerminal;

/**
 * Represents LR0Sets for a given symbol sequence.
 */
public class LR0Set extends HashSet<LR0Item> {

	private static final long serialVersionUID = 1L;

	// Symbol sequence
	private String name;

	/**
	 * Constructor.
	 * 
	 * @param name
	 *            Symbol sequence for LR(0) set.
	 */
	public LR0Set(String name) {
		this.name = name;
	}

	/**
	 * Get the name representing the symbol sequence.
	 * 
	 * @return Name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Check for conflicts.
	 * 
	 * @return True iff the set has conflicts
	 */
	public boolean hasConflicts() {
		// TODO implement check for conflicts

		return false;
	}

	/**
	 * Get all shiftable symbols.
	 * 
	 * @return Set of shiftable symbols.
	 */
	public HashSet<Alphabet> getShiftableSymbols() {
		HashSet<Alphabet> symbols = new HashSet<Alphabet>();
		for (LR0Item item : this) {
			if (item.canShift()) {
				symbols.add(item.getShiftableSymbolName());
			}
		}
		return symbols;
	}

	/**
	 * Get new LR(0) items after shift with given symbol.
	 * 
	 * @param symbol
	 *            Symbol to shift.
	 * @return A subset (of the current LR(0) set) which contains those items
	 *         that allow for a shift with the given symbol
	 */
	public LR0Set getShiftedItemsFor(Alphabet symbol) {
		LR0Set result = new LR0Set(getName() + symbol);
		for (LR0Item item : this) {
			if (item.canShift() && item.getShiftableSymbolName().equals(symbol)) {
				result.add(item.getShiftedItem());
			}
		}
		return result;
	}

	/**
	 * Check if the set contains a complete item of the form [A -> alpha.].
	 * 
	 * @return True iff the set contains a complete item
	 */
	public boolean containsCompleteItem() {
		for (LR0Item item : this) {
			if (!item.canShift()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Check if the set contains a final item.
	 * 
	 * @param start
	 *            Start symbol of grammar
	 * @return True iff the set contains a final item.
	 */
	public boolean containsFinalItem(NonTerminal start) {
		LR0Item item = getCompleteItem();
		if (item != null) {
			if (item.getLhs() == start) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Assuming there is one complete item in this set, return it. Returns null
	 * if there is no complete item.
	 * 
	 * @return Complete item
	 */
	public LR0Item getCompleteItem() {
		for (LR0Item item : this) {
			if (!item.canShift()) {
				return item;
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.AbstractCollection#toString()
	 */
	public String toString() {
		StringBuilder result = new StringBuilder();
		Iterator<LR0Item> it = this.iterator();
		while (it.hasNext()) {
			result.append(it.next());
			if (it.hasNext()) {
				result.append(", ");
			}
		}
		return result.toString();
	}
}
