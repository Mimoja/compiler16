package lexer;

import java.util.Arrays;
import java.util.HashMap;

import util.Pair;
import symbols.Tokens.Token;

/**
 * DFA recognizing a given word.
 */
public class DFA extends AbstractDFA {

	/**
	 * Construct a new DFA that recognizes exactly the given word. Given a word
	 * "foo" the constructed automaton looks like: -> () -f-> () -o-> () -o-> []
	 * from every state (including the final one) every other input letter leads
	 * to a distinguished sink state in which the automaton then remains
	 * 
	 * @param word
	 *            A String that the automaton should recognize
	 * @param token
	 *            The token corresponding to the recognized word.
	 */
	public DFA(String word, Token token) {
		assert (word.length() > 0);

		this.token = token;

		finalStates.add(word.length());
		sinkState = word.length() + 1;
		transitions = new HashMap<Pair<Integer, Character>, Integer>();
		for (int i = 0; i < word.length(); i++) {
			transitions.put(new Pair<Integer, Character>(i, word.charAt(i)), i + 1);
		}

		productive = new int[word.length() + 2];
		Arrays.fill(productive, -1);
	}
}
