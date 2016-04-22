package lexer;

/**
 *
 */
public class LexerGenerator {

	// The automata's alphabet is every character that Java can represent
	// However the relevant letters are the following

	// Individual letters
	final static protected char[] alpha = {};

	// Individual digits
	final static protected char[] numbers = {};

	// Underscore and digits
	final static protected char[] underScoreNumerical = {};

	// Special characters like parenthesis, line breaks, ...
	final static protected char[] special = {};

	// All recognized tokens
	public static enum Token {
		WHILE, COMMENT,
		// and so on
	}

}
