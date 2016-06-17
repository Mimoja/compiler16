package symbols;

/**
 * All recognized tokens.
 */
public class Tokens {

	/**
	 * All known tokens.
	 */
	public static enum Token implements Alphabet {
		WHILE,
		WRITE, READ,
		INT,
		IF, ELSE,
		TRUE, FALSE,
		ASSIGN,
		LPAR, RPAR, LBRACE, RBRACE,
		PLUS, MINUS, TIMES, DIV, MOD,
		LEQ, LT, GEQ, GT, EQ, NEQ,
		AND, OR, NOT,
		INC, DEC,
		SEMICOLON,
		ID,
		STRING,
		NUMBER,
		COMMENT,
		BLANK,
		EOF
	}

	/**
	 * Epsilon
	 */
	public static enum Epsilon implements Alphabet {
		EPS
	}
}
