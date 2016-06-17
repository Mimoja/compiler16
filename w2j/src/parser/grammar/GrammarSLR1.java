package parser.grammar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import symbols.Alphabet;
import symbols.NonTerminals.NonTerminal;
import symbols.Tokens.Token;

/**
 * Represents a simple grammar in SLR(1):
 * START -> S
 * S -> S+A | A
 * A -> A*B | B
 * B -> (S) | num
 */
public class GrammarSLR1 extends AbstractGrammar {

	// Single instance created upon class loading.
	private static final AbstractGrammar INSTANCE = new GrammarSLR1();

	/**
	 * Return singleton instance.
	 * 
	 * @return Singleton instance.
	 */
	public static AbstractGrammar getInstance() {
		return INSTANCE;
	}

	/**
	 * Constructor.
	 */
	private GrammarSLR1() {
		start = NonTerminal.START;
		rules = new HashMap<NonTerminal, List<List<Alphabet>>>();
		List<Alphabet> rhs;
		List<List<Alphabet>> alternatives;

		// START -> S
		rhs = new ArrayList<Alphabet>();
		alternatives = new ArrayList<List<Alphabet>>();
		rhs.add(NonTerminal.S);
		alternatives.add(rhs);
		rules.put(NonTerminal.START, alternatives);

		// S -> S+A | A
		rhs = new ArrayList<Alphabet>();
		alternatives = new ArrayList<List<Alphabet>>();
		rhs.add(NonTerminal.S);
		rhs.add(Token.PLUS);
		rhs.add(NonTerminal.A);
		alternatives.add(rhs);
		rhs = new ArrayList<Alphabet>();
		rhs.add(NonTerminal.A);
		alternatives.add(rhs);
		rules.put(NonTerminal.S, alternatives);

		// A -> A*B | B
		rhs = new ArrayList<Alphabet>();
		alternatives = new ArrayList<List<Alphabet>>();
		rhs.add(NonTerminal.A);
		rhs.add(Token.TIMES);
		rhs.add(NonTerminal.B);
		alternatives.add(rhs);
		rhs = new ArrayList<Alphabet>();
		rhs.add(NonTerminal.B);
		alternatives.add(rhs);
		rules.put(NonTerminal.A, alternatives);

		// B -> (S) | num
		rhs = new ArrayList<Alphabet>();
		alternatives = new ArrayList<List<Alphabet>>();
		rhs.add(Token.LPAR);
		rhs.add(NonTerminal.S);
		rhs.add(Token.RPAR);
		alternatives.add(rhs);
		rhs = new ArrayList<Alphabet>();
		rhs.add(Token.NUMBER);
		alternatives.add(rhs);
		rules.put(NonTerminal.B, alternatives);
	}

}
