package parser;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import lexer.Symbol;
import parser.grammar.AbstractGrammar;
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

		// TODO implement LR(0) parser

		Iterator<Symbol> it = lexOutput.iterator();
		Stack<LR0Set> stack = new Stack<LR0Set>();

		stack.push(generatorLR0.getInitialState());

		while (!stack.isEmpty()) {
			LR0Set currentSet = stack.peek();
			if (currentSet.containsFinalItem(start)) {
				// we do not actually have to empty the stack now
				if (!it.hasNext()) {
					// input completely read
					analysis.add(currentSet.getCompleteItem());
					return analysis;
				} else {
					// Parser finished but there is more input
					throw new ParserException("Parser finished but there is more unprocessed input!", analysis);
				}
			} else {
				if (currentSet.containsCompleteItem()) {
					// Item of the form [A -> alpha *]
					// Reduce
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
				} else {
					// Shift
					if (!it.hasNext()) {
						// nothing more to read, nothing to reduce and no final
						// item
						throw new ParserException("Only shift operation possible but the input terminated", analysis);
					} else {
						Token symbol = it.next().getToken();
						LR0Set succState = generatorLR0.getSuccessor(currentSet, symbol);
						if (null == succState) {
							throw new ParserException(
									"Tried shifting " + symbol + " onto the stack but could not find a successor delta("
											+ currentSet + ", " + symbol + ")",
									analysis);
						} else {
							stack.push(succState);
						}
					}
				}
			}
		}

		return analysis;
	}
}
