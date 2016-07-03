package parser.grammar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import symbols.Alphabet;
import symbols.NonTerminals.NonTerminal;
import symbols.Tokens.Token;

/**
 * Represents the grammar of the WHILE language.
 * 
 * start       -> program EOF
 * program     -> statement program | statement
 * statement   -> declaration SEM | assignment SEM | branch | loop | out SEM
 * declaration -> INT ID
 * assignment  -> ID ASSIGN expr | ID ASSIGN READ LBRAC RBRAC
 * out         -> WRITE LBRAC expr RBRAC | WRITE LBRAC STRING RBRAC
 * branch      -> IF LBRAC guard RBRAC LCBRAC program RCBRAC |
 *                IF LBRAC guard RBRAC LCBRAC program RCBRAC ELSE LCBRAC program RCBRAC
 * loop        -> WHILE LBRAC guard RBRAC LCBRAC program RCBRAC
 * expr        -> NUM | ID | subexpr | LBRAC subexpr RBRAC
 * subexpr     -> expr PLUS expr | expr MINUS expr | expr TIMES expr | expr DIV expr
 * guard       -> relation | subguard | LBRAC subguard RBRAC | NOT LBRAC guard RBRAC
 * subguard    -> guard AND guard | guard OR guard
 * relation    -> expr LT expr | expr LEQ expr | expr EQ expr | expr NEQ expr | expr GEQ expr | expr GT expr
 */
public class WhileGrammar extends AbstractGrammar {

	// Single instance created upon class loading.
	private static final AbstractGrammar INSTANCE = new WhileGrammar();

	/**
	 * Return singleton instance.
	 * 
	 * @return Singleton instance.
	 */
	public static AbstractGrammar getInstance() {
		return INSTANCE;
	}

	private WhileGrammar() {
		start = NonTerminal.START;
		rules = new HashMap<NonTerminal, List<List<Alphabet>>>();
		List<List<Alphabet>> alternatives;
		List<Alphabet> rhs;

		// start -> program $
		alternatives = new ArrayList<List<Alphabet>>();
		rhs = new ArrayList<Alphabet>();
		rhs.add(NonTerminal.PROGRAM);
		rhs.add(Token.EOF);
		alternatives.add(rhs);
		rules.put(NonTerminal.START, alternatives);

		// program -> statement program | statement
		rhs = new ArrayList<Alphabet>();
		alternatives = new ArrayList<List<Alphabet>>();
		rhs.add(NonTerminal.STATEMENT);
		rhs.add(NonTerminal.PROGRAM);
		alternatives.add(rhs);
		rhs = new ArrayList<Alphabet>();
		rhs.add(NonTerminal.STATEMENT);
		alternatives.add(rhs);
		rules.put(NonTerminal.PROGRAM, alternatives);

		// statement -> declaration SEM | assignment SEM | branch | loop | out
		// SEM
		rhs = new ArrayList<Alphabet>();
		alternatives = new ArrayList<List<Alphabet>>();
		rhs.add(NonTerminal.DECLARATION);
		rhs.add(Token.SEMICOLON);
		alternatives.add(rhs);
		rhs = new ArrayList<Alphabet>();
		rhs.add(NonTerminal.ASSIGNMENT);
		rhs.add(Token.SEMICOLON);
		alternatives.add(rhs);
		rhs = new ArrayList<Alphabet>();
		rhs.add(NonTerminal.BRANCH);
		alternatives.add(rhs);
		rhs = new ArrayList<Alphabet>();
		rhs.add(NonTerminal.LOOP);
		alternatives.add(rhs);
		rhs = new ArrayList<Alphabet>();
		rhs.add(NonTerminal.OUT);
		rhs.add(Token.SEMICOLON);
		alternatives.add(rhs);
		rules.put(NonTerminal.STATEMENT, alternatives);

		// declaration -> INT ID
		rhs = new ArrayList<Alphabet>();
		alternatives = new ArrayList<List<Alphabet>>();
		rhs.add(Token.INT);
		rhs.add(Token.ID);
		alternatives.add(rhs);
		rules.put(NonTerminal.DECLARATION, alternatives);

		// assignment -> ID ASSIGN expr | ID ASSIGN READ LBRAC RBRAC
		rhs = new ArrayList<Alphabet>();
		alternatives = new ArrayList<List<Alphabet>>();
		rhs.add(Token.ID);
		rhs.add(Token.ASSIGN);
		rhs.add(NonTerminal.EXPR);
		alternatives.add(rhs);
		rhs = new ArrayList<Alphabet>();
		rhs.add(Token.ID);
		rhs.add(Token.ASSIGN);
		rhs.add(Token.READ);
		rhs.add(Token.LPAR);
		rhs.add(Token.RPAR);
		alternatives.add(rhs);
		rules.put(NonTerminal.ASSIGNMENT, alternatives);

		// out -> WRITE LBRAC expr RBRAC | WRITE LBRAC STRING RBRAC
		rhs = new ArrayList<Alphabet>();
		alternatives = new ArrayList<List<Alphabet>>();
		rhs.add(Token.WRITE);
		rhs.add(Token.LPAR);
		rhs.add(NonTerminal.EXPR);
		rhs.add(Token.RPAR);
		alternatives.add(rhs);
		rhs = new ArrayList<Alphabet>();
		rhs.add(Token.WRITE);
		rhs.add(Token.LPAR);
		rhs.add(Token.STRING);
		rhs.add(Token.RPAR);
		alternatives.add(rhs);
		rules.put(NonTerminal.OUT, alternatives);

		// branch -> IF LBRAC guard RBRAC LCBRAC prog RCBRAC |
		// IF LBRAC guard RBRAC LCBRAC prog RCBRAC ELSE LCBRAC prog RCBRAC
		rhs = new ArrayList<Alphabet>();
		alternatives = new ArrayList<List<Alphabet>>();
		rhs.add(Token.IF);
		rhs.add(Token.LPAR);
		rhs.add(NonTerminal.GUARD);
		rhs.add(Token.RPAR);
		rhs.add(Token.LBRACE);
		rhs.add(NonTerminal.PROGRAM);
		rhs.add(Token.RBRACE);
		alternatives.add(rhs);
		rhs = new ArrayList<Alphabet>();
		rhs.add(Token.IF);
		rhs.add(Token.LPAR);
		rhs.add(NonTerminal.GUARD);
		rhs.add(Token.RPAR);
		rhs.add(Token.LBRACE);
		rhs.add(NonTerminal.PROGRAM);
		rhs.add(Token.RBRACE);
		rhs.add(Token.ELSE);
		rhs.add(Token.LBRACE);
		rhs.add(NonTerminal.PROGRAM);
		rhs.add(Token.RBRACE);
		alternatives.add(rhs);
		rules.put(NonTerminal.BRANCH, alternatives);

		// loop -> WHILE LBRAC guard RBRAC LCBRAC prog RCBRAC
		rhs = new ArrayList<Alphabet>();
		alternatives = new ArrayList<List<Alphabet>>();
		rhs.add(Token.WHILE);
		rhs.add(Token.LPAR);
		rhs.add(NonTerminal.GUARD);
		rhs.add(Token.RPAR);
		rhs.add(Token.LBRACE);
		rhs.add(NonTerminal.PROGRAM);
		rhs.add(Token.RBRACE);
		alternatives.add(rhs);
		rules.put(NonTerminal.LOOP, alternatives);

		// expr -> NUM | ID | subexpr | LBRAC subexpr RBRAC
		rhs = new ArrayList<Alphabet>();
		alternatives = new ArrayList<List<Alphabet>>();
		rhs.add(Token.NUMBER);
		alternatives.add(rhs);
		rhs = new ArrayList<Alphabet>();
		rhs.add(Token.ID);
		alternatives.add(rhs);
		rhs = new ArrayList<Alphabet>();
		rhs.add(NonTerminal.SUBEXPR);
		alternatives.add(rhs);
		rhs = new ArrayList<Alphabet>();
		rhs.add(Token.LPAR);
		rhs.add(NonTerminal.SUBEXPR);
		rhs.add(Token.RPAR);
		alternatives.add(rhs);
		rules.put(NonTerminal.EXPR, alternatives);

		// subexpr -> expr PLUS expr | expr MINUS expr | expr TIMES expr | expr
		// DIV expr
		rhs = new ArrayList<Alphabet>();
		alternatives = new ArrayList<List<Alphabet>>();
		rhs.add(NonTerminal.EXPR);
		rhs.add(Token.PLUS);
		rhs.add(NonTerminal.EXPR);
		alternatives.add(rhs);
		rhs = new ArrayList<Alphabet>();
		rhs.add(NonTerminal.EXPR);
		rhs.add(Token.MINUS);
		rhs.add(NonTerminal.EXPR);
		alternatives.add(rhs);
		rhs = new ArrayList<Alphabet>();
		rhs.add(NonTerminal.EXPR);
		rhs.add(Token.TIMES);
		rhs.add(NonTerminal.EXPR);
		alternatives.add(rhs);
		rhs = new ArrayList<Alphabet>();
		rhs.add(NonTerminal.EXPR);
		rhs.add(Token.DIV);
		rhs.add(NonTerminal.EXPR);
		alternatives.add(rhs);
		rules.put(NonTerminal.SUBEXPR, alternatives);

		// guard -> relation | subguard | LBRAC subguard RBRAC | NOT LBRAC guard
		// RBRAC
		rhs = new ArrayList<Alphabet>();
		alternatives = new ArrayList<List<Alphabet>>();
		rhs.add(NonTerminal.RELATION);
		alternatives.add(rhs);
		rhs = new ArrayList<Alphabet>();
		rhs.add(NonTerminal.SUBGUARD);
		alternatives.add(rhs);
		rhs = new ArrayList<Alphabet>();
		rhs.add(Token.LPAR);
		rhs.add(NonTerminal.SUBGUARD);
		rhs.add(Token.RPAR);
		alternatives.add(rhs);
		rhs = new ArrayList<Alphabet>();
		rhs.add(Token.NOT);
		rhs.add(Token.LPAR);
		rhs.add(NonTerminal.GUARD);
		rhs.add(Token.RPAR);
		alternatives.add(rhs);
		rules.put(NonTerminal.GUARD, alternatives);

		// subguard -> guard AND guard | guard OR guard
		rhs = new ArrayList<Alphabet>();
		alternatives = new ArrayList<List<Alphabet>>();
		rhs.add(NonTerminal.GUARD);
		rhs.add(Token.AND);
		rhs.add(NonTerminal.GUARD);
		alternatives.add(rhs);
		rhs = new ArrayList<Alphabet>();
		rhs.add(NonTerminal.GUARD);
		rhs.add(Token.OR);
		rhs.add(NonTerminal.GUARD);
		alternatives.add(rhs);
		rules.put(NonTerminal.SUBGUARD, alternatives);

		// relation -> expr LT expr | expr LEQ expr | expr EQ expr | expr NEQ
		// expr | expr GEQ expr | expr GT expr
		rhs = new ArrayList<Alphabet>();
		alternatives = new ArrayList<List<Alphabet>>();
		rhs.add(NonTerminal.EXPR);
		rhs.add(Token.LT);
		rhs.add(NonTerminal.EXPR);
		alternatives.add(rhs);
		rhs = new ArrayList<Alphabet>();
		rhs.add(NonTerminal.EXPR);
		rhs.add(Token.LEQ);
		rhs.add(NonTerminal.EXPR);
		alternatives.add(rhs);
		rhs = new ArrayList<Alphabet>();
		rhs.add(NonTerminal.EXPR);
		rhs.add(Token.EQ);
		rhs.add(NonTerminal.EXPR);
		alternatives.add(rhs);
		rhs = new ArrayList<Alphabet>();
		rhs.add(NonTerminal.EXPR);
		rhs.add(Token.NEQ);
		rhs.add(NonTerminal.EXPR);
		alternatives.add(rhs);
		rhs = new ArrayList<Alphabet>();
		rhs.add(NonTerminal.EXPR);
		rhs.add(Token.GEQ);
		rhs.add(NonTerminal.EXPR);
		alternatives.add(rhs);
		rhs = new ArrayList<Alphabet>();
		rhs.add(NonTerminal.EXPR);
		rhs.add(Token.GT);
		rhs.add(NonTerminal.EXPR);
		alternatives.add(rhs);
		rules.put(NonTerminal.RELATION, alternatives);
	}

}
