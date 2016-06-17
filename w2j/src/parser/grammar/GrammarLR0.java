package parser.grammar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import symbols.Alphabet;
import symbols.NonTerminals.NonTerminal;
import symbols.Tokens.Token;

/**
 * Represents a simple grammar in LR(0):
 * START -> S
 * S -> A | B
 * A -> *A | /
 * B -> *B | %
 */
public class GrammarLR0 extends AbstractGrammar {

	// Single instance created upon class loading.
	private static final AbstractGrammar INSTANCE = new GrammarLR0();

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
	private GrammarLR0() {
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

		// S -> A | B
		rhs = new ArrayList<Alphabet>();
		alternatives = new ArrayList<List<Alphabet>>();
		rhs.add(NonTerminal.A);
		alternatives.add(rhs);
		rhs = new ArrayList<Alphabet>();
		rhs.add(NonTerminal.B);
		alternatives.add(rhs);
		rules.put(NonTerminal.S, alternatives);

		// A -> *A | /
		rhs = new ArrayList<Alphabet>();
		alternatives = new ArrayList<List<Alphabet>>();
		rhs.add(Token.TIMES);
		rhs.add(NonTerminal.A);
		alternatives.add(rhs);
		rhs = new ArrayList<Alphabet>();
		rhs.add(Token.DIV);
		alternatives.add(rhs);
		rules.put(NonTerminal.A, alternatives);

		// B -> *B | %
		rhs = new ArrayList<Alphabet>();
		alternatives = new ArrayList<List<Alphabet>>();
		rhs.add(Token.TIMES);
		rhs.add(NonTerminal.B);
		alternatives.add(rhs);
		rhs = new ArrayList<Alphabet>();
		rhs.add(Token.MOD);
		alternatives.add(rhs);
		rules.put(NonTerminal.B, alternatives);
	}

}
