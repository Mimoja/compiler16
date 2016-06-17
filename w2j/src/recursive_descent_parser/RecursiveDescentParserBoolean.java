package recursive_descent_parser;

import symbols.Tokens.Token;
import parser.ParserException;

/**
 * Recursive descent parser for boolean expressions.
 * 
 * Grammar: 1. start -> guard 2. guard -> rel subguard 3. subguard -> eps 4.
 * subguard -> AND guard 5. subguard -> OR guard 6. rel -> ID subrel 7. subrel
 * -> EQ ID 8. subrel -> LEQ ID
 */
public class RecursiveDescentParserBoolean extends RecursiveDescentParser {

	/**
	 * Starting symbol of the grammar.
	 * 
	 * @throws ParserException
	 *             Exception from the parser.
	 */
	protected void main() throws RecursiveDescentParserException {
		next();
		start();
	}

	private void start() throws RecursiveDescentParserException {
		if (token == Token.ID) {
			print(1);
			guard();
		} else {
			printError("Not recognized rule in start");
		}
	}

	private void guard() throws RecursiveDescentParserException {
		if (token == Token.ID) {
			print(2);
			rel();
			subguard();
		} else {
			printError("Not recognized rule in guard");
		}
	}

	private void subguard() throws RecursiveDescentParserException {
		if (token == Token.EOF) {
			print(3);
		} else if (token == Token.AND) {
			print(4);
			next();
			guard();
		} else if (token == Token.OR) {
			print(5);
			next();
			guard();
		} else {
			printError("Not recognized rule in subguard");
		}
	}

	private void rel() throws RecursiveDescentParserException {
		if (token == Token.ID) {
			print(6);
			next();
			subrel();
		} else {
			printError("Not recognized rule in rel");
		}
	}

	private void subrel() throws RecursiveDescentParserException {
		if (token == Token.EQ) {
			print(7);
			next();
			id();
		} else if (token == Token.LEQ) {
			print(8);
			next();
			id();
		} else {
			printError("Not recognized rule in subrel");
		}
	}

	private void id() throws RecursiveDescentParserException {
		if (token == Token.ID) {
			next();
		} else {
			printError("Expected token ID");
		}
	}
}
