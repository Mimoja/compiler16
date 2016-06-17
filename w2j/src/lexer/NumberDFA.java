package lexer;

import symbols.Tokens.Token;
import util.Pair;

import java.util.Arrays;
import java.util.HashMap;

/**
 * DFA recognizing integers.
 */
public class NumberDFA extends AbstractDFA {

	/**
	 * Construct a new DFA that recognizes integers.
	 */
	public NumberDFA() {
		token = Token.NUMBER;

		// there are only 4 states, state 0 is the initial state
		int finalState = 1;
		int firstNull = 2;
		sinkState = 3;

		transitions = new HashMap<Pair<Integer, Character>, Integer>();

		for (int i = 0; i < LexerGenerator.numbers.length; i++) {
			if (LexerGenerator.numbers[i] == '0') {
				transitions.put(new Pair<Integer, Character>(initialState, LexerGenerator.numbers[i]), firstNull);
			} else {
				transitions.put(new Pair<Integer, Character>(initialState, LexerGenerator.numbers[i]), finalState);
			}
			transitions.put(new Pair<Integer, Character>(finalState, LexerGenerator.numbers[i]), finalState);
		}

		finalStates.add(finalState);
		finalStates.add(firstNull);

		productive = new int[4];
		Arrays.fill(productive, -1);
	}
}
