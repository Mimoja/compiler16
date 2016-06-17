package parser.grammar;

import symbols.Alphabet;
import symbols.NonTerminals.NonTerminal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import parser.Rule;

/**
 * Abstract class representing a context free grammar.
 */
public abstract class AbstractGrammar {

	// All rules in the grammar.
	protected HashMap<NonTerminal, List<List<Alphabet>>> rules;

	// Start symbol.
	protected NonTerminal start;

	/**
	 * Get start symbol.
	 * 
	 * @return Start symbol.
	 */
	public NonTerminal getStart() {
		return start;
	}

	/**
	 * Get all rules for a given non-terminal.
	 * 
	 * @param name
	 *            Non-terminal on left-hand side.
	 * @return All rules.
	 */
	public List<Rule> getRules(NonTerminal name) {
		List<Rule> result = new ArrayList<Rule>();
		if (rules.containsKey(name)) {
			for (List<Alphabet> rhs : rules.get(name)) {
				result.add(new Rule(name, rhs));
			}
		}
		return result;
	}

	/**
	 * Get all rules.
	 * 
	 * @return List of all rules.
	 */
	public List<Rule> getRules() {
		List<Rule> result = new ArrayList<Rule>();
		for (NonTerminal nonTerminal : rules.keySet()) {
			for (List<Alphabet> rhs : rules.get(nonTerminal)) {
				result.add(new Rule(nonTerminal, rhs));
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuilder result = new StringBuilder();
		for (NonTerminal nonTerminal : rules.keySet()) {
			result.append(nonTerminal);
			result.append(" -> ");
			List<List<Alphabet>> allRules = rules.get(nonTerminal);
			for (int i = 0; i < allRules.size(); i++) {
				List<Alphabet> alternatives = allRules.get(i);
				for (Alphabet symbol : alternatives) {
					result.append(symbol);
					result.append(" ");
				}
				if (i < allRules.size() - 1) {
					result.append(" | ");
				}
			}
			result.append("\n");
		}
		return result.toString();
	}
}