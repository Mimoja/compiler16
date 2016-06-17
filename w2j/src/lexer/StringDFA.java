package lexer;

import symbols.Tokens.Token;
import util.Pair;

import java.util.Arrays;
import java.util.HashMap;

/**
 * DFA recognizing string constants.
 */
public class StringDFA extends AbstractDFA {

	private final int readLetters = 1;

	/**
	 * Construct a new DFA that recognizes string constants
	 */
	public StringDFA() {
		token = Token.STRING;

		// there are only 4 states, state 0 is the initial state
		int finalState = 2;
		sinkState = 3;

		transitions = new HashMap<Pair<Integer, Character>, Integer>();

		transitions.put(new Pair<Integer, Character>(initialState, '"'), readLetters);
		transitions.put(new Pair<Integer, Character>(readLetters, '"'), finalState);

		finalStates.add(finalState);

		productive = new int[4];
		Arrays.fill(productive, -1);
	}

	/**
	 * Performs one step of the DFA for a given letter. This method works
	 * differently than in the superclass AbstractDFA.
	 * 
	 * @param letter
	 *            The current input.
	 */
	@Override
	public void doStep(char letter) {
		Integer nextState = transitions.get(new Pair<Integer, Character>(currentState, letter));
		if (nextState == null) {
			if (currentState == readLetters && letter != '"') {
				// stay there
			} else {
				currentState = sinkState;
			}
		} else {
			currentState = nextState;
		}
	}

	/**
	 * Checks if the final state is reachable. This method works differently
	 * than in the superclass AbstractDFA as not all possible steps are directly
	 * encoded as transitions.
	 */
	@Override
	protected boolean isFinalStateReachable(int state) {
		return state != sinkState;
	}

}
