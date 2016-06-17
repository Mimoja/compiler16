package symbols;

/**
 * All non-terminals in a grammar.
 */
public class NonTerminals {

	/**
	 * All known non-terminals.
	 */
	public static enum NonTerminal implements Alphabet {
		START, PROGRAM, STATEMENT, DECLARATION, ASSIGNMENT, OUT, BRANCH, LOOP, EXPR, SUBEXPR, GUARD, SUBGUARD, RELATION,
		S, A, B, C, D
	}
}
