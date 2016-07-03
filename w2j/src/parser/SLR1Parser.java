package parser;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import lexer.Symbol;
import parser.grammar.AbstractGrammar;
import symbols.Alphabet;
import symbols.NonTerminals.NonTerminal;
import symbols.Tokens.Epsilon;

/**
 * SLR(1) parser.
 */
public class SLR1Parser {

	// Generator for LR(0) sets
	private LR0SetGenerator generatorLR0;

	// Generator for first and follow sets
	private LookAheadGenerator generatorLookAhead;

	// Start symbol of grammar
	private NonTerminal start;

	/**
	 * Constructor.
	 * 
	 * @param grammar
	 *            Grammar.
	 */
	public SLR1Parser(AbstractGrammar grammar) {
		this.generatorLR0 = new LR0SetGenerator(grammar);
		this.generatorLookAhead = new LookAheadGenerator(grammar);
		this.start = grammar.getStart();
	}

	/**
	 * Parse the input via SLR(1) parsing.
	 * 
	 * @param lexOutput
	 *            List of symbols
	 * @return A list of rules which corresponds to the right-most analysis
	 * @throws ParserException
	 *             Parser exception
	 */
	public List<Rule> parse(List<Symbol> lexOutput) throws ParserException {
		List<Rule> analysis = new LinkedList<Rule>();

		// TODO implement SLR(1) parser

		Iterator<Symbol> it = lexOutput.iterator();
		Stack<LR0Set> stack = new Stack<LR0Set>();

		stack.push(generatorLR0.getInitialState());
		Alphabet lookahead = Epsilon.EPS;

		while (!stack.isEmpty()) {
			LR0Set currentSet = stack.peek();
			// check if lookahead has been consumed by a previous iteration and
			// if so read one more token
			if (lookahead == Epsilon.EPS) {
				// read one more symbol if there are any
				lookahead = it.hasNext() ? it.next().getToken() : Epsilon.EPS;
			}

			// Finish parsing if the stack top contains [start -> alpha *]
			if (currentSet.containsFinalItem(start) && lookahead == Epsilon.EPS) {
				// input completely read
				analysis.add(currentSet.getCompleteItem());
				return analysis;
			}
			// otherwise continue parsing:
			else {
				// if stack top has a complete item and it matches the
				// lookahead,
				// reduce
				if (currentSet.containsCompleteItem()
						&& generatorLookAhead.containsFollow(currentSet.getCompleteItem().getLhs(), lookahead)) {
					// Item of the form [A -> alpha *]
					LR0Item completeItem = currentSet.getCompleteItem();
					// remove |alpha| elements from the stack
					for (int i = 0; i < completeItem.getRhs().length; i++) {
						stack.pop();
					}
					// I' := Top(stack)
					currentSet = stack.peek();
					// J := delta(I', A)
					LR0Set succState = generatorLR0.getSuccessor(currentSet, completeItem.getLhs());
					if (succState == null) {
						throw new ParserException(
								"Tried reducing with rule " + completeItem + " but could not find a successor delta("
										+ currentSet + ", " + completeItem.getLhs() + ")",
								analysis);
					}
					stack.push(succState);
					// append the applied rule (ensures correct order)
					analysis.add(completeItem);
				}
				// otherwise, shift
				else {
					if (lookahead == Epsilon.EPS) {
						// nothing more to read, nothing to reduce and no final
						// item
						throw new ParserException("Only shift operation possible but the input terminated", analysis);
					} else {
						LR0Set succState = generatorLR0.getSuccessor(currentSet, lookahead);
						if (succState == null) {
							throw new ParserException("Tried shifting " + lookahead
									+ " onto the stack but could not find a successor delta(" + currentSet + ", "
									+ lookahead + ")", analysis);
						} else {
							stack.push(succState);
							// mark lookahead as consumed
							lookahead = Epsilon.EPS;
						}
					}
				}
			}
		}

		return analysis;
	}
}
