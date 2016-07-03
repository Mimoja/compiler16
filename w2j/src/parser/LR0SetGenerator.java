package parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Queue;

import parser.grammar.AbstractGrammar;
import symbols.Alphabet;
import symbols.NonTerminals.NonTerminal;
import util.Pair;

/**
 * Generator for all LR(0) sets for a given grammar.
 */
public class LR0SetGenerator {

	// Grammar.
	private AbstractGrammar grammar;

	// Complete state space.
	private HashSet<LR0Set> states;

	// Initial state.
	private LR0Set initialState;

	// Transitions.
	private HashMap<Pair<LR0Set, Alphabet>, LR0Set> transitions;

	/**
	 * Constructor.
	 * 
	 * @param grammar
	 *            Grammar.
	 */
	public LR0SetGenerator(AbstractGrammar grammar) {
		this.grammar = grammar;
		this.states = new HashSet<LR0Set>();
		this.transitions = new HashMap<Pair<LR0Set, Alphabet>, LR0Set>();
		generateLR0StateSpace();
	}

	/**
	 * Add new LR(0) set
	 * 
	 * @param state
	 *            LR(0) set
	 */
	private void addState(LR0Set state) {
		assert (!states.contains(state));
		states.add(state);
	}

	/**
	 * Add new transition.
	 * 
	 * @param source
	 *            Source LR(0) set
	 * @param letter
	 *            Letter
	 * @param target
	 *            Target LR(0) set
	 */
	private void addTransition(LR0Set source, Alphabet letter, LR0Set target) {
		assert (states.contains(source));
		assert (states.contains(target));
		assert (!transitions.containsKey(new Pair<LR0Set, Alphabet>(source, letter)));
		transitions.put(new Pair<LR0Set, Alphabet>(source, letter), target);
	}

	/**
	 * Get successor LR(0) set.
	 * 
	 * @param source
	 *            Source LR(0) set.
	 * @param letter
	 *            Letter.
	 * @return Returns the letter-successor of source, or null if no such
	 *         mapping exists.
	 */
	public LR0Set getSuccessor(LR0Set source, Alphabet letter) {
		return transitions.get(new Pair<LR0Set, Alphabet>(source, letter));
	}

	/**
	 * Generate all LR(0) sets for the given grammar.
	 */
	private void generateLR0StateSpace() {
		// TODO implement state space generation

		// Start with the empty word
		LR0Set epsilon = new LR0Set("");
		assert (grammar.getRules(NonTerminal.START).size() == 1);
		Rule startRule = grammar.getRules(grammar.getStart()).get(0);
		epsilon.add(LR0Item.freshItem(startRule));
		epsilon.addAll(epsilonClosure(epsilon));
		addState(epsilon);
		initialState = epsilon;

		// While new sets were added continue building
		Queue<LR0Set> queue = new LinkedList<LR0Set>();
		queue.add(epsilon);
		while (!queue.isEmpty()) {
			LR0Set set = queue.poll();
			for (Alphabet symbol : set.getShiftableSymbols()) {
				LR0Set newSet = new LR0Set(set.getName() + symbol);
				newSet.addAll(set.getShiftedItemsFor(symbol));
				newSet.addAll(epsilonClosure(newSet));
				if (!states.contains(newSet)) {
					// New state
					queue.add(newSet);
					addState(newSet);
				} else {
					// State already exists
					for (LR0Set state : states) {
						if (state.equals(newSet)) {
							newSet = state;
							break;
						}
					}
				}
				if (!transitions.containsKey(new Pair<LR0Set, Alphabet>(set, symbol))) {
					addTransition(set, symbol, newSet);
				}
			}
		}
	}

	/**
	 * Compute the epsilon closure for the given LR(0) set.
	 * 
	 * @param set
	 *            LR(0) set
	 * @return LR(0) representing the epsilon closure.
	 */
	private LR0Set epsilonClosure(LR0Set set) {
		// TODO it might be helpful to implement this method.
		
		LR0Set result = new LR0Set(set.getName());
		ArrayList<NonTerminal> nonTerminals = new ArrayList<NonTerminal>();

		// For every item of the form A -> alpha * B gamma
		// collect the nonterminal to the right of *
		for (LR0Item item : set) {
			NonTerminal n = item.getNextNonTerminal();
			if (n != null) {
				nonTerminals.add(n);
			}
		}

		for (NonTerminal nonTerminal : nonTerminals) {
			for (LR0Item item : freshItems(nonTerminal)) {
				// Here we prevent the addition of items that were there
				// already,
				// without this we might run into infinite recursion!
				if (!set.contains(item))
					result.add(item);
			}
		}

		// Recursively continue the closure since the above procedure might have
		// introduced an item like [S->.Aa] etc...
		// The recursion terminates when no *new* items of the above form can be
		// found
		if (result.size() > 0) {
			result.addAll(set);
			result.addAll(epsilonClosure(result));
			return result;
		} else {
			return set;
		}
	}

	/**
	 * From a set of rules of the form A -> ab | aC create the "fresh" items A
	 * -> * ab, A -> * aC
	 * 
	 * @param lhs
	 *            Non-terminal on left-hand side
	 * @return A set of items with nothing left of the marker
	 */
	private LR0Set freshItems(NonTerminal lhs) {
		LR0Set result = new LR0Set("");
		List<Rule> rules = grammar.getRules(lhs);
		for (Rule rule : rules) {
			result.add(LR0Item.freshItem(rule));
		}
		return result;
	}

	/**
	 * Get number of conflicts.
	 * 
	 * @return Number of conflicts.
	 */
	public int nrConflicts() {
		int counter = 0;
		for (LR0Set lr0set : states) {
			if (lr0set.hasConflicts())
				counter++;
		}
		return counter;
	}

	/**
	 * Get number of states.
	 * 
	 * @return Number of states.
	 */
	public int nrStates() {
		return states.size();
	}

	/**
	 * Get initial LR(0) set.
	 * 
	 * @return Initial LR(0) set
	 */
	public LR0Set getInitialState() {
		return initialState;
	}

	/**
	 * Print all LR(0) sets.
	 */
	public void printLR0Sets() {
		for (LR0Set set : states) {
			System.out.println(set.getName() + ": " + set);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("digraph{");
		for (LR0Set state : states) {
			builder.append(state.getName());
			builder.append(" [label=\"");
			builder.append(state.toString());
			builder.append("\"];\n");
		}
		builder.append("initial state: ");
		builder.append(initialState.getName());
		builder.append("\n");

		Iterator<Entry<Pair<LR0Set, Alphabet>, LR0Set>> it = transitions.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Pair<LR0Set, Alphabet>, LR0Set> entry = it.next();
			Pair<LR0Set, Alphabet> pair = entry.getKey();
			builder.append(pair.getFirst().getName());
			builder.append(" -> ");
			builder.append(entry.getValue().getName());
			builder.append(" [label=\"");
			builder.append(pair.getSecond());
			builder.append("\"];\n");
		}
		builder.append("}");
		return builder.toString();
	}
}
