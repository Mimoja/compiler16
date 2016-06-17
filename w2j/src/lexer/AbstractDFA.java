package lexer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import symbols.Tokens.Token;
import util.Pair;

/**
 * Abstract class for Deterministic Finite Automata.
 */
public abstract class AbstractDFA {

	protected Token token; // Token that is recognized by this automaton

	protected final int initialState = 0;
	protected int sinkState;
	// set of final states
	protected ArrayList<Integer> finalStates = new ArrayList<Integer>();
	// mapping from (state, character) to nextState
	protected HashMap<Pair<Integer, Character>, Integer> transitions;

	protected int currentState = 0;

	protected int[] productive;

	/**
	 * Reset the automaton to the initial state.
	 */
	public void reset() {
		currentState = initialState;
	}

	/**
	 * This is useful for resuming parsing at a certain position
	 * 
	 * @param state
	 *            Id of state that the automaton should start in
	 */
	public void resetToState(int state) {
		assert (0 <= state && state < productive.length);
		currentState = state;
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
		Integer nextState = transitions.get(new Pair<Integer, Character>(currentState, letter));
		if (nextState == null)
			currentState = sinkState;
		else
			currentState = nextState;
	}

	/**
	 * Check if the automaton is currently accepting.
	 * 
	 * @return True, if the automaton is currently in an accepting state.
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
			doStep(letter);
		}
		return isAccepting();
	}

	/**
	 * Get the current state.
	 * 
	 * @return The id of the current state.
	 */
	public int getCurrentState() {
		return currentState;
	}

	/**
	 * Checks if the final state can be reached from the current state.
	 * 
	 * @return True, if the state is productive, i.e. the final state can be
	 *         reached.
	 */
	public boolean isProductive() {
		return isFinalStateReachable(currentState);
	}

	/**
	 * Perform BFS to check if a final state is reachable from the given state.
	 * 
	 * @param state
	 *            Starting state for BFS.
	 * @return True, if the state is productive, i.e. a final state can be
	 *         reached.
	 */
	protected boolean isFinalStateReachable(int state) {
		if (productive[state] == 0) {
			return false;
		} else if (productive[state] == 1) {
			return true;
		} else if (finalStates.contains(state)) {
			productive[state] = 1;
			return true;
		} else {
			assert (productive[state] == -1);
			productive[state] = 0;
		}

		// Perform BFS
		for (Map.Entry<Pair<Integer, Character>, Integer> entry : transitions.entrySet()) {
			Pair<Integer, Character> key = entry.getKey();
			// Consider outgoing transitions of state
			if (key.getFirst() == state) {
				if (isFinalStateReachable(entry.getValue())) {
					assert (state != sinkState);
					productive[state] = 1;
					return true;
				}
			}
		}

		productive[state] = 0;
		return false;
	}

	/**
	 * @return The Token that this automaton recognizes
	 */
	public Token getToken() {
		return token;
	}
}
