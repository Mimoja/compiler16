package lexer;

import java.util.Arrays;
import java.util.HashMap;

import util.Pair;
import symbols.Tokens.Token;

/**
 * DFA recognizing comments.
 */
public class CommentDFA extends AbstractDFA {

	private final int slashRead = 1;
	private final int singleLine = 2;
	private final int multiLine = 3;
	private final int maybeEndMultiLine = 4;
	private final int finalState = 5;
	private final int macFinalState = 6;
	private final int sinkState = 7;

	/**
	 * Construct a new DFA that recognizes comments within source code. There
	 * are two kinds of comments: A single line comment starts with // and ends
	 * with a newline and a multiline comment that starts with /* and ends with
	 * * / (without the space)
	 */
	public CommentDFA() {
		token = Token.COMMENT;

		transitions = new HashMap<Pair<Integer, Character>, Integer>();
		transitions.put(new Pair<Integer, Character>(initialState, '/'), slashRead);
		transitions.put(new Pair<Integer, Character>(slashRead, '/'), singleLine);
		transitions.put(new Pair<Integer, Character>(slashRead, '*'), multiLine);
		transitions.put(new Pair<Integer, Character>(singleLine, '\n'), finalState);
		transitions.put(new Pair<Integer, Character>(singleLine, '\r'), macFinalState);
		transitions.put(new Pair<Integer, Character>(macFinalState, '\n'), finalState);
		transitions.put(new Pair<Integer, Character>(multiLine, '*'), maybeEndMultiLine);
		transitions.put(new Pair<Integer, Character>(maybeEndMultiLine, '*'), maybeEndMultiLine);
		transitions.put(new Pair<Integer, Character>(maybeEndMultiLine, '/'), finalState);

		finalStates.add(finalState);
		finalStates.add(macFinalState);

		productive = new int[8];
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
			switch (currentState) {
			case singleLine:
			case multiLine:
				// stay here
				break;
			case maybeEndMultiLine:
				currentState = multiLine;
				break;
			default:
				currentState = sinkState;
			}
		} else
			currentState = nextState;
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
