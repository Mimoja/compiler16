package lexer;

import lexer.LexerGenerator.Token;
import helper.Pair;

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
		int n = word.length();
		assert (n > 0); // <--- whyyyyyy?
		this.token = token;

		initialState = new State("init");
		sinkState = new State("sink");

		State[] states = new State[n+1];
		states[0] = initialState;

		for(int i = 0; i < n; i++) {
			states[i+1] = new State("letter " + i);
			Pair p = new Pair<State, Character>(states[i], word.charAt(i));
			transitions.put(p, states[i+1]);
		}
		finalStates.add(states[n]);
	}
}
