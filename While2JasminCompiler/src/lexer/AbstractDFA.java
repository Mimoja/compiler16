package lexer;

import lexer.LexerGenerator.Token;

/**
 * Abstract class for Deterministic Finite Automata.
 */
public abstract class AbstractDFA {

	protected Token token; // Token that is recognized by this automaton

	// TODO: use data structure for representing
	// - states
	// - final states (and sink states)
	// - transitions of the form (state, input) -> state
	// - current state

	/**
	 * Reset the automaton to the initial state.
	 */
	public void reset() {
		// TODO: reset automaton to initial state
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
		// TODO: do step by going to the next state according to the current
		// state and the read letter.
	}

	/**
	 * Check if the automaton is currently accepting.
	 * 
	 * @return True, if the automaton is currently in the accepting state.
	 */
	public boolean isAccepting() {
		// TODO: return if the current state is accepting
		return false;
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
	 * Checks if the final state can be reached from the current state.
	 * 
	 * @return True, if the state is productive, i.e. the final state can be
	 *         reached.
	 */
	public boolean isProductive() {
		// TODO: return if the current state is productive
		return false;
	}

	/**
	 * @return The Token that this automaton recognizes
	 */
	public Token getToken() {
		return token;
	}
}
