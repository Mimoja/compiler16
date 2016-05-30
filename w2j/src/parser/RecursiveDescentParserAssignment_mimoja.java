package parser;

import lexer.LexerGenerator.Token;

/**
 * Recursive descent parser for assignment of the form: int id = num;
 * 
 * Grammar:
 *  1: start      -> assignment SEMICOLON EOF
 *  2: assignment -> ID ASSIGN expr
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
       
	rules = start();

        if (!symbols.isEmpty()) {
			printError("Symbols remaining.");
		}
	}
}

private void nextToken(){
	next();
	return token;
}

private List<Integer> start() throws ParserException{
	List<Integer> rules = new List<>();
	List<Integer> assignmentRules = assignment();
	
	if(nextToken() == Token.SEMICOLON)
		if(nextToken() == EOF)
			if(!assignmentRules.isEmpty()){
				rules.add(1);
				rules.addAll(assignmentRules);
			}
	return rules;
}

private List<Integer> assignment(){
	List<Integer> rules = new List<>();
	
	if(nextToken() == Token.ID)
		if(nextToken() == Token.ASSIGN){
			List<Integer> exprRules = expr();
			if(!exprRules.isEmpty()){
				rules.add(2);
				rules.addAll(exprRules);
			}
		}

	
	return rules;
}

private List<Integer> expr(){
	List<Integer> rules = new List<>();
	
	switch(nextToken()){
		case ID:
			List<Integer> subexprRules = subexpr();
			if(!subexprRules.isEmpty()){
				rules.add(3);
				rules.addAll(subexprRules);
			}
		break;
		case NUMBER:
			List<Integer> subexprRules = subexpr();
			if(!subexprRules.isEmpty()){
				rules.add(4);
				rules.addAll(subexprRules);
			}
		break;
		case LPAR:
			List<Integer> exprRules = expr();
			if(!exprRules.isEmpty())
				if(nextToken() == Token.RPAR){
					rules.add(5);
					rules.addAll(subexprRules);
				}
		break;
		case READ:
			if(nextToken() == Token.LPAR)
				if(nextToken() == RPAR){
					List<Integer> subexprRules = subexpr();
					if(!assignmentRules.isEmpty()){
							rules.add(6);
							rules.addAll(assignmentRules);
					}
				}
		break;
		default:
		break;
	}
	return rules;
}

private List<Integer> subexpr(){
	List<Integer> rules = new List<>();
	
	switch(nextToken()){
		case PLUS:
			List<Integer> exprRules = expr();
			if(!exprRules.isEmpty()){
				rules.add(7);
				rules.addAll(subexprRules);
			}
		break;
		case MINUS:
			List<Integer> exprRules = expr();
			if(!exprRules.isEmpty()){
				rules.add(8);
				rules.addAll(subexprRules);
			}
		break;
		case TIMES:
			List<Integer> exprRules = expr();
			if(!exprRules.isEmpty()){
				rules.add(9);
				rules.addAll(subexprRules);
			}
		break;
		case DIV:
			List<Integer> exprRules = expr();
			if(!exprRules.isEmpty()){
				rules.add(10);
				rules.addAll(subexprRules);
			}
		break;
		case MOD:
			List<Integer> exprRules = expr();
			if(!exprRules.isEmpty()){
				rules.add(11);
				rules.addAll(subexprRules);
			}
		break:
		default:
			//TODO how is esp represented
			rules.add(12)
		break;
		
	}
}




