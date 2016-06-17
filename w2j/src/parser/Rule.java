package parser;

import java.util.List;

import symbols.Alphabet;
import symbols.NonTerminals.NonTerminal;

/**
 * Represents a rule in a context free grammar.
 */
public class Rule {

	// Non-terminal on left-hand side of rule.
	private final NonTerminal lhs;

	// Symbols on right-hand side of rule.
	private final Alphabet[] rhs;

	/**
	 * Constructor.
	 * 
	 * @param lhs
	 *            left-hand side
	 * @param rhs
	 *            right-hand side
	 */
	public Rule(NonTerminal lhs, Alphabet[] rhs) {
		this.lhs = lhs;
		this.rhs = rhs;
	}

	/**
	 * Constructor.
	 * 
	 * @param lhs
	 *            left-hand side
	 * @param rhs
	 *            right-hand side
	 */
	public Rule(NonTerminal lhs, List<Alphabet> rhs) {
		this.lhs = lhs;
		this.rhs = new Alphabet[rhs.size()];
		for (int i = 0; i < rhs.size(); i++) {
			this.rhs[i] = rhs.get(i);
		}
	}

	/**
	 * Get left-hand side of rule.
	 * 
	 * @return left-hand side
	 */
	public NonTerminal getLhs() {
		return lhs;
	}

	/**
	 * Get right-hand side of rule.
	 * 
	 * @return right-hand side
	 */
	public Alphabet[] getRhs() {
		return rhs;
	}
}
