package lexer;

import java.util.Iterator;
import java.util.List;

/**
 * The lexer. Knows about the recognized alphabet and tokens and performs the
 * lexer analysis.
 */
public class LexerGenerator {

	// The automaton's alphabet is every character that Java can represent
	// However the relevant letters are the following

	// Individual letters
	final static protected char[] alpha = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o',
			'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
			'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

	// Individual digits
	final static protected char[] numbers = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

	// Underscore and digits
	final static protected char[] underScoreNumerical = { '_', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

	// Special characters like parenthesis, line breaks, ...
	final static protected char[] special = { ':', '=', '(', ')', '{', '}', '+', '-', '*', '/', '<', '>', '!', '%', '$',
			'&', '|', ';', '"', ' ', '\t', '\r', '\n' };

	// All recognized tokens
	public static enum Token {
		WHILE, WRITE, READ, INT, IF, ELSE, TRUE, FALSE, ASSIGN, LPAR, RPAR, LBRACE, RBRACE, PLUS, MINUS, TIMES, DIV, MOD, LEQ, LT, GEQ, GT, EQ, NEQ, AND, OR, NOT, INC, DEC, SEMICOLON, ID, STRING, NUMBER, COMMENT, BLANK, EOF
	}

	/**
	 * Perform the lexer analysis.
	 * 
	 * @param input
	 *            The input program.
	 * @return Recognized symbols.
	 * @throws LexerException
	 *             Exception from the lexer.
	 */
	public static List<Symbol> analyse(String input) throws LexerException {
		return analyse(input, true);
	}

	/**
	 * Perform the lexer analysis.
	 * 
	 * @param input
	 *            The input program.
	 * @param suppressBlankAndComments
	 *            If true, blanks and comments are ignored.
	 * @return Recognized symbols.
	 * @throws LexerException
	 *             Exception from the lexer.
	 */
	public static List<Symbol> analyse(String input, boolean suppressBlankAndComments) throws LexerException {
		BacktrackingDFA bdfa = new BacktrackingDFA();
		List<Symbol> analysis = null;
		analysis = bdfa.run(input);

		if (suppressBlankAndComments) {
			Iterator<Symbol> it = analysis.iterator();
			while (it.hasNext()) {
				Token t = it.next().getToken();
				if (Token.BLANK == t || Token.COMMENT == t) {
					it.remove();
				}
			}
		}

		return analysis;
	}

}
