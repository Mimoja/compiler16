package lexer;

import lexer.LexerGenerator.Token;

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
		assert (word.length() > 0); // <--- whyyyyyy?
		this.token = token;

		initialState = new State("init");
		sinkState = new State("sink");

		// TODO: moar
	}
}
