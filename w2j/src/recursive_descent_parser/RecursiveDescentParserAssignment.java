package recursive_descent_parser;

import symbols.Tokens.Token;
import parser.ParserException;

/**
 * Recursive descent parser for assignment of the form: int id = num;
 * 
 * Grammar: 1: start -> assignment SEMICOLON EOF 2: assignment -> INT ID ASSIGN
 * expr 3: expr -> ID subexpr 4: expr -> NUMBER subexpr 5: expr -> LPAR expr
 * RPAR 6: expr -> READ LPAR RPAR subexpr 7: subexpr -> PLUS expr 8: subexpr ->
 * MINUS expr 9: subexpr -> TIMES expr 10: subexpr -> DIV expr 11: subexpr ->
 * MOD expr 12: subexpr -> eps
 */
public class RecursiveDescentParserAssignment extends RecursiveDescentParser {

	/**
	 * Starting symbol of the grammar.
	 * 
	 * @throws ParserException
	 *             Exception from the parser.
	 */
	protected void main() throws RecursiveDescentParserException {
		next();
		start();
		if (!symbols.isEmpty()) {
			printError("Symbols remaining.");
		}
	}

	private void start() throws RecursiveDescentParserException {
		if (token == Token.INT) {
			print(1);
			assignment();
			if (token != Token.SEMICOLON) {
				printError("Wrong token in start");
			}
			next();
			if (token != Token.EOF) {
				printError("Wrong token in start");
			}
		} else {
			printError("Not recognized rule in start");
		}
	}

	private void assignment() throws RecursiveDescentParserException {
		if (token == Token.INT) {
			print(2);
			next();
			if (token != Token.ID) {
				printError("Wrong token in assigment");
			}
			next();
			if (token != Token.ASSIGN) {
				printError("Wrong token in assigment");
			}
			next();
			expr();
		} else {
			printError("Not recognized rule in assigment");
		}
	}

	private void expr() throws RecursiveDescentParserException {
		if (token == Token.ID) {
			print(3);
			next();
			subexpr();
		} else if (token == Token.NUMBER) {
			print(4);
			next();
			subexpr();
		} else if (token == Token.LPAR) {
			print(5);
			next();
			expr();
			if (token != Token.RPAR) {
				printError("Wrong token in expr");
			}
			next();
		} else if (token == Token.READ) {
			print(6);
			next();
			if (token != Token.LPAR) {
				printError("Wrong token in expr");
			}
			next();
			if (token != Token.RPAR) {
				printError("Wrong token in expr");
			}
			next();
			subexpr();
		} else {
			printError("Not recognized rule in expr");
		}
	}

	private void subexpr() throws RecursiveDescentParserException {
		if (token == Token.PLUS) {
			print(7);
			next();
			expr();
		} else if (token == Token.MINUS) {
			print(8);
			next();
			expr();
		} else if (token == Token.TIMES) {
			print(9);
			next();
			expr();
		} else if (token == Token.DIV) {
			print(10);
			next();
			expr();
		} else if (token == Token.MOD) {
			print(11);
			next();
			expr();
		} else if (token == Token.SEMICOLON || token == Token.RPAR) {
			print(12);
		} else {
			printError("Not recognized rule in subexpr");
		}
	}

}
