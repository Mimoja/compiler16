package parser;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import util.Pair;
import lexer.Symbol;
import parser.grammar.AbstractGrammar;
import symbols.Alphabet;
import symbols.NonTerminals.NonTerminal;
import symbols.Tokens.Token;

/**
 * LR(0) parser.
 */
public class LR0Parser {

	// Generator for LR(0) sets
	private LR0SetGenerator generatorLR0;

	// Start symbol of grammar
	private NonTerminal start;

	/**
	 * Constructor.
	 * 
	 * @param grammar
	 *            Grammar.
	 */
	public LR0Parser(AbstractGrammar grammar) {
		this.generatorLR0 = new LR0SetGenerator(grammar);
		this.start = grammar.getStart();
	}

	/**
	 * Parse the input via LR(0) parsing.
	 * 
	 * @param lexOutput
	 *            List of symbols
	 * @return A list of rules which corresponds to the right-most analysis
	 * @throws ParserException
	 *             Parser exception
	 */
	public List<Rule> parse(List<Symbol> lexOutput) throws ParserException {
		List<Rule> analysis = new LinkedList<Rule>();

		// implementation: see Def 9.8

		// to fetch symbols from lexer (represents w)
		Iterator<Symbol> symbols = lexOutput.iterator();

		// need a stack, contains (Alphabet,LR0Set) tuples
		Stack<Pair<Alphabet, LR0Set>> stack = new Stack<Pair<Alphabet, LR0Set>>();

		// initialize with initial state (w, I_0, \varepsilon)
		stack.push(
			new Pair<Alphabet, LR0Set>(null, generatorLR0.getInitialState())
		);

		while (!stack.isEmpty())
		{
			Pair<Alphabet,LR0Set> top = stack.peek();
			LR0Set topset = top.getSecond();

			// final item?
			if (topset.containsFinalItem(start))
			{
				if (symbols.hasNext()) // finished but more input?
				{
					throw new ParserException("Parser finished but there is input left!", analysis);
				}
				else // finished and no more input. good.
				{
					return analysis;
				}
			}

			// not finished.

			// check for complete item (form [A -> alpha.])...
			if (topset.containsCompleteItem())
			{
				// complete item -> reduce

				LR0Item completeItem = topset.getCompleteItem();
				NonTerminal lhs = completeItem.getLhs();
				Alphabet[] rhs = completeItem.getRhs();

				for (int i = 0; i < rhs.length; i += 1)
					stack.pop();

				// compute transition on topmost item on stack
				// I := ...
				Pair<Alphabet,LR0Set> prevtop = stack.peek();

				// J := delta(I, A)
				LR0Set nextstate = generatorLR0.getSuccessor(
					prevtop.getSecond(),
					lhs
				);

				if (nextstate == null)
					throw new ParserException("Reduce using " + completeItem + ": getSuccessor("+ prevtop.getSecond() + ", " + lhs + ") failed!", analysis);

				// success!

				// record rule in analysis
				analysis.add(0, completeItem);

				// and push next state to stack
				stack.push(
					new Pair<Alphabet,LR0Set>(lhs, nextstate)
				);
			}
			else
			{
				// not a complete item -> shift (try to)

				if (!symbols.hasNext())
					throw new ParserException("Supposed to shift, but no input left!", analysis);

				// we can shift
				Token token = symbols.next().getToken();

				// compute successor or fail
				LR0Set nextstate =
					generatorLR0.getSuccessor(topset, token);
				if (nextstate == null)
					throw new ParserException("Can't shift " + token + " onto stack because there is no delta(" + topset + ", " + token + ")", analysis);

				stack.push(
					new Pair<Alphabet,LR0Set>(token, nextstate)
				);
			}
		}

		return analysis;
	}
}
