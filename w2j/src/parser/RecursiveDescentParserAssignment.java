package parser;

import lexer.LexerGenerator.Token;

/**
 * Recursive descent parser for assignment of the form: int id = num;
 * 
 * Grammar:
 *  1: start      -> assignment SEMICOLON EOF
 *  2: assignment -> INT ID ASSIGN expr
 *  3: expr       -> ID subexpr
 *  4: expr       -> NUMBER subexpr
 *  5: expr       -> LPAR expr RPAR
 *  6: expr       -> READ LPAR RPAR subexpr
 *  7: subexpr    -> PLUS expr
 *  8: subexpr    -> MINUS expr
 *  9: subexpr    -> TIMES expr
 * 10: subexpr    -> DIV expr
 * 11: subexpr    -> MOD expr
 * 12: subexpr    -> eps
 */
public class RecursiveDescentParserAssignment extends RecursiveDescentParser {

	/**
	 * Starting symbol of the grammar.
	 * 
	 * @throws ParserException
	 *             Exception from the parser.
	 */
	protected void main() throws ParserException {
       
         // TODO implement		
		
        if (!symbols.isEmpty()) {
			printError("Symbols remaining.");
		}
	}
}
