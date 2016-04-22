package lexer;

import lexer.LexerGenerator.Token;

/**
 * DFA recognizing comments.
 */
public class CommentDFA extends AbstractDFA {

	/**
	 * Construct a new DFA that recognizes comments within source code. There
	 * are two kinds of comments: A single line comment starts with // and ends
	 * with a newline and a multiline comment that starts with /* and ends with
	 * * / (without the space)
	 */
	public CommentDFA() {
		token = Token.COMMENT;

		// TODO: build DFA recognizing comments
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
		// TODO: implement accordingly
	}

	/**
	 * Check if the automaton is currently accepting.
	 * 
	 * @return True, if the automaton is currently in the accepting state.
	 */
	@Override
	public boolean isAccepting() {
		// TODO: implement accordingly
		return false;
	}
}
