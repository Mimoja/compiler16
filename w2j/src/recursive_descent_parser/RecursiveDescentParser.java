package recursive_descent_parser;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import symbols.Tokens.Token;
import parser.ParserException;
import lexer.Symbol;

/**
 * Abstract class for a recursive descent parser.
 */
public abstract class RecursiveDescentParser {

	/**
	 * Current list of symbols.
	 */
	protected Queue<Symbol> symbols;

	/**
	 * Recognized list of rules of the grammar.
	 */
	protected List<Integer> rules;

	/**
	 * Current token.
	 */
	protected Token token;

	/**
	 * Perform parsing via recursive descent.
	 * 
	 * @param symbols
	 *            The recognized tokens and attributes.
	 * @return Corresponding rule numbers.
	 * @throws ParserException
	 *             Exception from the parser.
	 */
	public List<Integer> analyse(List<Symbol> symbols) throws RecursiveDescentParserException {
		// Initialize
		this.symbols = new LinkedList<Symbol>();
		for (Symbol symbol : symbols) {
			this.symbols.add(symbol);
		}
		this.rules = new ArrayList<Integer>();
		token = null;
		main();
		return rules;
	}

	/**
	 * Consider the next token.
	 */
	protected void next() {
		token = symbols.poll().getToken();
	}

	/**
	 * Print a new recognized rule.
	 * 
	 * @param rule
	 *            The number of the rule.
	 */
	protected void print(int rule) {
		rules.add(rule);
	}

	/**
	 * Print an parser error.
	 * 
	 * @param message
	 *            Message of the error.
	 * @throws ParserException
	 *             Throws the corresponding parser Exception containing the
	 *             message and the recognized rules up until this error.
	 */
	protected void printError(String message) throws RecursiveDescentParserException {
		throw new RecursiveDescentParserException(message, rules);
	}

	/**
	 * Starting symbol of the grammar.
	 * 
	 * @throws ParserException
	 *             Exception from the parser.
	 */
	protected abstract void main() throws RecursiveDescentParserException;
}
