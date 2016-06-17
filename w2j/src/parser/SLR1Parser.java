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

		return analysis;
	}
}
