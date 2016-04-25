package lexer;

import helper.Pair;
import java.lang.Character;
import java.util.HashMap;
import java.util.HashSet;
import lexer.LexerGenerator.Token;

/**
 * Abstract class for Deterministic Finite Automata.
 */
public abstract class AbstractDFA {

	protected Token token; // Token that is recognized by this automaton

	// TODO: typedef Pair<State, Character> Transition>;
	protected final HashMap<Pair<State, Character>, State> transitions;
	protected final HashSet<State> finalStates;
	protected State initialState;
	protected State sinkState;

	protected State currentState;

	AbstractDFA() {
		transitions = new HashMap<Pair<State, Character>, State>();
		finalStates = new HashSet<State>();
	}

	/**
	 * Reset the automaton to the initial state.
	 */
	public void reset() {
		currentState = initialState;
	}

	/**
	 * Performs one step of the DFA for a given letter. If there is a transition
	 * for the given letter, then the automaton proceeds to the successor state.
	 * Otherwise it goes to the sink state. By construction it will stay in the
	 * sink for every input letter.
	 * 
	 * @param letter
	 *            The current input.
	 */
	public void doStep(char letter) {
		Pair p = new Pair<State, Character>(currentState, letter);
		State s = transitions.get(p);

		currentState = (s != null)? s : sinkState;
		assert sinkState != null;
		assert currentState != null;
	}

	/**
	 * Check if the automaton is currently accepting.
	 * 
	 * @return True, if the automaton is currently in the accepting state.
	 */
	public boolean isAccepting() {
		return finalStates.contains(currentState);
	}

	/**
	 * Run the DFA on the input.
	 * 
	 * @param inputWord
	 *            String that contains the input word
	 * @return True, if if the word is accepted by this automaton
	 */
	public boolean run(String inputWord) {
		this.reset();
		char[] inputCharWord = inputWord.toCharArray();
		for (char letter : inputCharWord) {
			State old = currentState;
			doStep(letter);
		}
		return isAccepting();
	}

	/**
	 * Checks if the final state can be reached from the current state.
	 * 
	 * @return True, if the state is productive, i.e. the final state can be
	 *         reached.
	 */
	public boolean isProductive() {
		if (isAccepting())
			return true;

		if (currentState.equals(sinkState))
			return false;

		/*
		 * I would like to return "maybe" here, but that's not a
		 * boolean.
		 *
		 * "true", however, is correct, if the automaton is minimal in
		 * a certain way: If there are no states but the sink state,
		 * which make the final states unreachable.
		 */
		return true;
	}

	/**
	 * @return The Token that this automaton recognizes
	 */
	public Token getToken() {
		return token;
	}


	/* Internal classes. Maybe we should move them to individual files. */

	/** This class represents a state */
	class State {
		final String string;

		public State(String s)   { string = s; }
		public String toString() { return string; }

		/* Do we need to implement hashCode and equals here? */
	}
}
