package parser;

import java.util.Iterator;
import java.util.List;

import parser.grammar.AbstractGrammar;
import symbols.Alphabet;
import symbols.NonTerminals.NonTerminal;
import symbols.Tokens.Epsilon;
import symbols.Tokens.Token;
import util.MapSet;

/**
 * Generator for first and follow sets for a given grammar.
 */
public class LookAheadGenerator {

	// Grammar.
	private AbstractGrammar grammar;

	// First sets for each non-terminal
	private MapSet<NonTerminal, Alphabet> first;

	// Follow sets for each non-terminal
	private MapSet<NonTerminal, Alphabet> follow;

	/**
	 * Constructor.
	 * 
	 * @param grammar
	 *            Grammar.
	 */
	public LookAheadGenerator(AbstractGrammar grammar) {
		this.grammar = grammar;
		computeFirst();
		computeFollow();
	}

	/**
	 * Compute the first set for each non-terminal.
	 */
	public void computeFirst() {
		first = new MapSet<NonTerminal, Alphabet>();
		// TODO implement

		List<Rule> rules = grammar.getRules();
		Iterator<Rule> iter = rules.iterator();
		while (iter.hasNext()) {
			Rule rule = iter.next();
			if (rule.getRhs().length > 0) {
				// Add terminal symbol to first set
				if (rule.getRhs()[0] instanceof Token) {
					first.add(rule.getLhs(), rule.getRhs()[0]);
					// Rule is not important anymore
					iter.remove();
				}
			} else {
				// Add epsilon to first set
				first.add(rule.getLhs(), Epsilon.EPS);
				// Rule is not important anymore
				iter.remove();
			}
		}

		// Repeat until fix point is reached
		boolean changed = true;
		while (changed) {
			changed = false;

			for (Rule rule : rules) {
				NonTerminal nonTerminal = rule.getLhs();
				// Iterate over all symbols
				for (int i = 0; i < rule.getRhs().length; i++) {
					if (rule.getRhs()[i] instanceof Token) {
						if (!first.contains(nonTerminal, rule.getRhs()[i])) {
							// The symbol is a terminal and we take it
							first.add(nonTerminal, rule.getRhs()[i]);
							changed = true;
						}
						break;
					} else {
						boolean foundEpsilon = false;
						assert (rule.getRhs()[i] instanceof NonTerminal);
						NonTerminal newNonTerminal = (NonTerminal) rule.getRhs()[i];
						for (Alphabet symbol : first.get(newNonTerminal)) {
							if (!first.contains(nonTerminal, symbol)) {
								first.add(nonTerminal, symbol);
								changed = true;
							}
							if (symbol.equals(Epsilon.EPS)) {
								foundEpsilon = true;
							}
						}
						if (!foundEpsilon) {
							break;
						}
					}
				}
			}
		}
	}

	/**
	 * Compute the follow set for each non-terminal. Assume that the first sets
	 * were computed beforehand.
	 */
	public void computeFollow() {
		assert (first != null);

		follow = new MapSet<NonTerminal, Alphabet>();
		// TODO implement

		// Add epsilon to start symbol
		follow.add(grammar.getStart(), Epsilon.EPS);

		List<Rule> rules = grammar.getRules();
		Iterator<Rule> iter = rules.iterator();
		while (iter.hasNext()) {
			Rule rule = iter.next();
			for (int i = 0; i < rule.getRhs().length; i++) {
				if (rule.getRhs()[i] instanceof NonTerminal) {
					NonTerminal nonTerminal = (NonTerminal) rule.getRhs()[i];
					if (i < rule.getRhs().length - 1 && rule.getRhs()[i + 1] instanceof Token) {
						// Add token after non-terminal to follow set
						follow.add(nonTerminal, rule.getRhs()[i + 1]);
					}
				}
			}
		}

		// Repeat until fix point is reached
		boolean changed = true;
		while (changed) {
			changed = false;

			for (Rule rule : rules) {
				// Iterate over all symbols
				for (int i = 0; i < rule.getRhs().length; i++) {
					if (rule.getRhs()[i] instanceof NonTerminal) {
						NonTerminal nonTerminal = (NonTerminal) rule.getRhs()[i];
						if (i < rule.getRhs().length - 1 && rule.getRhs()[i + 1] instanceof NonTerminal) {
							NonTerminal newNonTerminal = (NonTerminal) rule.getRhs()[i + 1];
							for (Alphabet symbol : first.get(newNonTerminal)) {
								if (!follow.contains(nonTerminal, symbol)) {
									follow.add(nonTerminal, symbol);
									changed = true;
								}
							}
						}

						// Check if epsilons occur until end of rule
						boolean allEpsilon = true;
						for (int j = i + 1; j < rule.getRhs().length; j++) {
							if (rule.getRhs()[j] instanceof Token) {
								// Terminal found
								allEpsilon = false;
								break;
							}
							assert (rule.getRhs()[j] instanceof NonTerminal);
							NonTerminal newNonTerminal = (NonTerminal) rule.getRhs()[j];
							if (!first.contains(newNonTerminal, Epsilon.EPS)) {
								// No epsilon in first set found
								allEpsilon = false;
								break;
							}
						}
						if (allEpsilon) {
							// Add follow set of lhs
							for (Alphabet symbol : follow.get(rule.getLhs())) {
								if (!follow.contains(nonTerminal, symbol)) {
									follow.add(nonTerminal, symbol);
									changed = true;
								}
							}
						}
					}
				}
			}

		}
	}

	/**
	 * Check if the first set for the given non-terminal contains the given
	 * symbol.
	 * 
	 * @param nonTerminal
	 *            Non-terminal
	 * @param symbol
	 *            Symbol
	 * @return True iff the follow set contains the symbol
	 */
	public boolean containsFirst(NonTerminal nonTerminal, Alphabet symbol) {
		return first.contains(nonTerminal, symbol);
	}

	/**
	 * Check if the follow set for the given non-terminal contains the given
	 * symbol.
	 * 
	 * @param nonTerminal
	 *            Non-terminal
	 * @param symbol
	 *            Symbol
	 * @return True iff the follow set contains the symbol
	 */
	public boolean containsFollow(NonTerminal nonTerminal, Alphabet symbol) {
		return follow.contains(nonTerminal, symbol);
	}

	/**
	 * Print first sets.
	 */
	public void printFirstSets() {
		for (NonTerminal nonTerminal : first.keySet()) {
			System.out.print("fi(" + nonTerminal + "): {");
			Iterator<Alphabet> iter = first.get(nonTerminal).iterator();
			while (iter.hasNext()) {
				System.out.print(iter.next());
				if (iter.hasNext()) {
					System.out.print(", ");
				}
			}
			System.out.println("}");
		}
	}

	/**
	 * Print follow sets.
	 */
	public void printFollowSets() {
		for (NonTerminal nonTerminal : follow.keySet()) {
			System.out.print("fo(" + nonTerminal + "): {");
			Iterator<Alphabet> iter = follow.get(nonTerminal).iterator();
			while (iter.hasNext()) {
				System.out.print(iter.next());
				if (iter.hasNext()) {
					System.out.print(", ");
				}
			}
			System.out.println("}");
		}
	}
}
