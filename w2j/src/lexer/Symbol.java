package lexer;

import lexer.LexerGenerator.Token;

/**
 * Represents a symbol.
 */
public class Symbol {

	private final Token token;
	private final String attribute;

	/**
	 * Constructor.
	 * 
	 * @param token
	 *            The token.
	 * @param attribute
	 *            The attribute.
	 */
	public Symbol(Token token, String attribute) {
		this.token = token;
		this.attribute = attribute;
	}

	/**
	 * Get the token.
	 * 
	 * @return The token.
	 */
	public Token getToken() {
		return token;
	}

	/**
	 * Get the attribute.
	 * 
	 * @return The attribute.
	 */
	public String getAttribute() {
		return attribute;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String sToken = token != null ? token.toString() : "null";
		String sAttribute = attribute != null ? attribute.toString() : "null";
		return "(" + sToken + ", " + sAttribute + ")";
	}

}
