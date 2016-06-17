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

		// TODO implement LR(0) parser

		return analysis;
	}
}
