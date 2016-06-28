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
        LR0Set eps = new LR0Set("");
        
        //Find start -> S
        for(Rule r : grammar.getRules()){
            if(r.getLhs() == grammar.getStart()){
                Alphabet[] rhs = r.getRhs();
                assert(rhs.length == 1);
                assert(rhs[0] instanceof NonTerminal);
                eps.add(new LR0Item(r.getLhs(), rhs, 0));
            }
        }
        
        //Fill LR(0)(eps)
        boolean added = true;
        HashSet<NonTerminal> alreadyAdded = new HashSet<NonTerminal>();
        while(added){
            added = false;
            LR0Set toBeAdded = new LR0Set("add these please");
            for(LR0Item i : eps){
                NonTerminal n = i.getNextNonTerminal();
                if(n != null && !alreadyAdded.contains(n)){
                    for(Rule r : grammar.getRules(n)){
                        toBeAdded.add(LR0Item.freshItem(r));
                    }
                    alreadyAdded.add(n);
                    added = true;
        	    }
            }
            eps.addAll(toBeAdded);
            toBeAdded = null;
        }
       
        addState(eps);
        initialState = eps;
        
        //Create the other LR(0) sets by shifting
        HashSet<LR0Item> alreadyShifted = new HashSet<LR0Item>();
        HashSet<LR0Set> allSets = new HashSet<LR0Set>(states);
        added = true;
        while(added){
            added = false;
            HashSet<LR0Set> toBeAdded = new HashSet<LR0Set>();
            for(LR0Set set : states){
                for(LR0Item item : set){
                    if (item.canShift() && !alreadyShifted.contains(item)){
                        String setName = set.getName() + item.getShiftableSymbolName();
                        LR0Item shifted = item.getShiftedItem();
                        //see if we have to make a new set
                        boolean found = false;
                        for(LR0Set l : allSets){
                            if(l.getName().equals(setName)){
                                // just add it to the old set
                                l.add(shifted);
                                found = true;
                                break;
                            }
                        }
                        if(!found){
                            // make a new set
                            LR0Set newSet = new LR0Set(setName);
                            newSet.add(shifted);
                            toBeAdded.add(newSet);
                            allSets.add(newSet);
                        }
                        alreadyShifted.add(item);
                        added = true;
                    }
                }
            }
            states.addAll(toBeAdded);
        }
        
        //Fill the LR(0) sets
        for(LR0Set set : states){
            added = true;
            while(added){
                added = false;
                LR0Set toBeAdded = new LR0Set("i am garbage");
                for(LR0Item item : set){
                    NonTerminal n = item.getNextNonTerminal();
                    if (n != null) {
                        for(Rule r : grammar.getRules(n)){
                            LR0Item newItem = LR0Item.freshItem(r);
                            //is this item already in there?
                            boolean found = false;
                            for(LR0Item otherItem : set){
                                if(otherItem.equals(newItem)){
                                    found = true;
                                }
                            }
                            if(!found){
                                toBeAdded.add(newItem);
                                added = true;
                            }
                        }
                    }
                }
                set.addAll(toBeAdded);
            }
        }
        
        //Shift once more, not creating new sets.
        alreadyShifted = new HashSet<LR0Item>();
        added = true;
        while(added){
            added = false;
            HashSet<LR0Set> toBeAdded = new HashSet<LR0Set>();
            for(LR0Set set : states){
                for(LR0Item item : set){
                    if (item.canShift() && !alreadyShifted.contains(item)){
                        String setName = set.getName() + item.getShiftableSymbolName();
                        LR0Item shifted = item.getShiftedItem();
                        for(LR0Set l : states){
                            if(l.getName().equals(setName)){
                                l.add(shifted);
                                break;
                            }
                        }
                    }
                }
            }
            states.addAll(toBeAdded);
        }
	}
        
        public String arrayToString(Alphabet[] alp)
        {
            StringBuilder output = new StringBuilder();
            for(Alphabet a : alp){
                output.append(a);
            }
            return output.toString();
        }

	/**
	 * Compute the epsilon closure for the given LR(0) set.
	 * 
	 * @param set
	 *            LR(0) set
	 * @return LR(0) representing the epsilon closure.
	 */
	private LR0Set epsilonClosure(LR0Set set) {
		// what is an epsilon closure?
		// there is nothing about it in the slides
		// probably not needed, we can do without it

		LR0Set result = new LR0Set(set.getName());
		
		return result;
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
			System.out.println("    " + set.getName() + ": " + set);
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
