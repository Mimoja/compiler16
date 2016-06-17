package lexer;

import symbols.Tokens.Token;
import util.Pair;

import java.util.Arrays;
import java.util.HashMap;

/**
 * DFA recognizing identifiers.
 */
public class IdentifierDFA extends AbstractDFA {

	/**
	 * Construct a new DFA that recognizes every alphanumerical identifier that
	 * starts with a letter and continues with letter, numbers or underscores.
	 */
	public IdentifierDFA() {
		token = Token.ID;

		int finalState = 1;
		sinkState = 2;

		transitions = new HashMap<Pair<Integer, Character>, Integer>();
		for (int i = 0; i < LexerGenerator.alpha.length; i++) {
			transitions.put(new Pair<Integer, Character>(initialState, LexerGenerator.alpha[i]), finalState);
			transitions.put(new Pair<Integer, Character>(finalState, LexerGenerator.alpha[i]), finalState);
		}
		for (int i = 0; i < LexerGenerator.underScoreNumerical.length; i++) {
			transitions.put(new Pair<Integer, Character>(finalState, LexerGenerator.underScoreNumerical[i]),
					finalState);
		}

		finalStates.add(finalState);

		productive = new int[3];
		Arrays.fill(productive, -1);
	}
}
