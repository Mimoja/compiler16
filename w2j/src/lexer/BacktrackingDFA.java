package lexer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import lexer.LexerGenerator.Token;
import util.Pair;

public class BacktrackingDFA {

	private List<AbstractDFA> automata;
	private HashMap<Pair<int[], Character>, int[]> transitions;
	private Map<String, Token> recognisedToken;
	private int[] initialState;
	private int[] backtrackState;
	private int[] currentState;

	/**
	 * Constructor.
	 */
	public BacktrackingDFA() {
		generateDFAforTokens();
		generateTransitions();
	}

	/**
	 * This method creates an array of DFAs, one for every token (and symbol).
	 * Those automata will run in parallel and are controlled by the doStep(),
	 * isProductive() and resetToState() methods.
	 */
	public void generateDFAforTokens() {
		automata = new ArrayList<AbstractDFA>();
		// generate all automata
		automata.add(new DFA("while", Token.WHILE));
		automata.add(new DFA("write", Token.WRITE));
		automata.add(new DFA("read", Token.READ));
		automata.add(new DFA("int", Token.INT));
		automata.add(new DFA("if", Token.IF));
		automata.add(new DFA("else", Token.ELSE));
		automata.add(new DFA("true", Token.TRUE));
		automata.add(new DFA("false", Token.FALSE));
		automata.add(new DFA("(", Token.LPAR));
		automata.add(new DFA(")", Token.RPAR));
		automata.add(new DFA("{", Token.LBRACE));
		automata.add(new DFA("}", Token.RBRACE));
		automata.add(new DFA("+", Token.PLUS));
		automata.add(new DFA("-", Token.MINUS));
		automata.add(new DFA("*", Token.TIMES));
		automata.add(new DFA("/", Token.DIV));
		automata.add(new DFA("%", Token.MOD));
		automata.add(new DFA("<=", Token.LEQ));
		automata.add(new DFA("<", Token.LT));
		automata.add(new DFA(">=", Token.GEQ));
		automata.add(new DFA(">", Token.GT));
		automata.add(new DFA("==", Token.EQ));
		automata.add(new DFA("=", Token.ASSIGN));
		automata.add(new DFA("!=", Token.NEQ));
		automata.add(new DFA("&&", Token.AND));
		automata.add(new DFA("||", Token.OR));
		automata.add(new DFA("!", Token.NOT));
		automata.add(new DFA("++", Token.INC));
		automata.add(new DFA("--", Token.DEC));
		automata.add(new DFA(";", Token.SEMICOLON));
		automata.add(new DFA("$", Token.EOF));
		automata.add(new IdentifierDFA());
		automata.add(new NumberDFA());
		automata.add(new CommentDFA());
		automata.add(new StringDFA());
		automata.add(new DFA(" ", Token.BLANK));
		automata.add(new DFA("\t", Token.BLANK));
		automata.add(new DFA("\r", Token.BLANK));
		automata.add(new DFA("\n", Token.BLANK));

		initialState = new int[automata.size()];
		Arrays.fill(initialState, 0);
		backtrackState = new int[initialState.length];
		currentState = new int[initialState.length];
	}

	/**
	 * Generate all transitions by exploring the state space.
	 */
	private void generateTransitions() {
		transitions = new HashMap<Pair<int[], Character>, int[]>();
		recognisedToken = new HashMap<String, Token>();

		// Create array of relevant alphabet
		char[] relevantAlphabet = new char[LexerGenerator.alpha.length + LexerGenerator.underScoreNumerical.length
				+ LexerGenerator.special.length];
		System.arraycopy(LexerGenerator.alpha, 0, relevantAlphabet, 0, LexerGenerator.alpha.length);
		System.arraycopy(LexerGenerator.underScoreNumerical, 0, relevantAlphabet, LexerGenerator.alpha.length,
				LexerGenerator.underScoreNumerical.length);
		System.arraycopy(LexerGenerator.special, 0, relevantAlphabet,
				LexerGenerator.alpha.length + LexerGenerator.underScoreNumerical.length, LexerGenerator.special.length);

		Queue<int[]> statesToExpand = new LinkedList<int[]>();

		Set<String> visitedStates = new HashSet<String>();

		int[] state = new int[initialState.length];
		System.arraycopy(initialState, 0, state, 0, initialState.length);
		statesToExpand.add(state);
		visitedStates.add(hashState(state));

		// Explore possible states
		int[] tempState = new int[initialState.length];
		while (!statesToExpand.isEmpty()) {
			state = statesToExpand.remove();
			// Consider all possible transitions
			for (char letter : relevantAlphabet) {
				for (int i = 0; i < automata.size(); i++) {
					AbstractDFA automaton = automata.get(i);
					automaton.resetToState(state[i]);
					automaton.doStep(letter);
					tempState[i] = automaton.getCurrentState();
				}
				if (!visitedStates.contains(hashState(tempState))) {
					// New state needs exploration
					statesToExpand.add(tempState.clone());
					visitedStates.add(hashState(tempState));
				}
				transitions.put(new Pair<int[], Character>(state, letter), tempState);
			}
			// Check final states
			setToken(state);
		}
	}

	/**
	 * Construct a hash for the given state.
	 * 
	 * @param state
	 *            The state of the backtracking DFA.
	 * @return The hash for the state.
	 */
	private String hashState(int[] state) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < state.length; i++) {
			builder.append(state[i]);
		}
		return builder.toString();
	}

	/**
	 * Set the token for the given state
	 * 
	 * @param state
	 *            State to set token for.
	 */
	private void setToken(int[] state) {
		for (int i = 0; i < state.length; i++) {
			automata.get(i).resetToState(state[i]);
			if (automata.get(i).isAccepting()) {
				recognisedToken.put(hashState(state), automata.get(i).getToken());
				// already found an accepting state => proceed with next
				// state in transitions
				break;
			}
		}
	}

	/**
	 * Do a step in the backtracking DFA.
	 * 
	 * @param letter
	 *            The current character.
	 * @return The recognized token.
	 */
	public Token doStep(char letter) {
		for (int i = 0; i < automata.size(); i++) {
			automata.get(i).doStep(letter);
			currentState[i] = automata.get(i).getCurrentState();
		}
		return recognisedToken.get(hashState(currentState));
	}

	/**
	 * Given a string of lexemes, chop them up to the corresponding symbols,
	 * i.e. a list of (token, attribute) pairs. Note that since all keywords and
	 * symbols are represented by their own token, the attribute only really
	 * matters for identifiers and numbers.
	 * 
	 * @param word
	 *            The input program to analyze.
	 * @return List of symbols.
	 * @throws LexerException
	 *             Exception from the lexer if the analysis is not successful.
	 */
	public List<Symbol> run(String word) throws LexerException {
		List<Symbol> result = new ArrayList<Symbol>();

		String lookahead = "";
		String attribute = "";

		// null Token means normal mode
		Token mode = null;

		for(int i = 0; i < automata.size(); i++) {
			AbstractDFA A = automata.get(i);
			A.reset();
			initialState[i] = A.getCurrentState();
		}

		resetToState(initialState);

		int[] nextstate;

		while(true){
			if(!word.isEmpty()){
				Token nextmode = doStep(word.charAt(0));
			
				if(mode == null) {
					// Comments with numbers in parentheses reference lines from Definition 3.15
					if(!isProductive()) {
						// (3)
						throw new LexerException("Lexer Error: no matching token");
					} else if ( nextmode == null) {
						// (2)
						attribute += word.charAt(0);
						word = word.substring(1);
						mode = null;
					} else {
						// (1)
						attribute += word.charAt(0);
						word = word.substring(1);
						mode = nextmode;
					}
				} else {
					if(!isProductive()) {
						// (6)
						resetToState(initialState);
						word = lookahead + word;
						lookahead = "";
						result.add(new Symbol(mode, attribute));
						mode = null;
						attribute = "";
					} else if (nextmode == null) {
						// (5)
						lookahead = lookahead + word.charAt(0);
						word = word.substring(1);
					} else {
						// (4)
						mode = nextmode;
						attribute = attribute + lookahead + word.charAt(0);
						word = word.substring(1);
						lookahead = "";
					}
				}
			} else {
				if(!lookahead.isEmpty()) {
					// (9)
					resetToState(initialState);
					word = lookahead;
					lookahead = "";
					result.add(new Symbol(mode, attribute));
					mode = null;
					attribute = "";
				} else if (mode == null) {
					// (8)
					throw new LexerException("Lexer Error: unexpected end of input");
				} else {
					// (7)
					result.add(new Symbol(mode, attribute));
					return result;
				}	
			}
		}
	}

	/**
	 * Check if the current state is productive.
	 * 
	 * @return True iff the current state of at least one component is productive.
	 */
	private boolean isProductive() {
		for (AbstractDFA automaton : automata) {
			if (automaton.isProductive())
				return true;
		}
		return false;
	}

	/**
	 * Reset the current state to a previous state.
	 * 
	 * @param state
	 *            The state to reset to.
	 */
	public void resetToState(int[] state) {
		for (int i = 0; i < automata.size(); i++) {
			currentState[i] = state[i];
			automata.get(i).resetToState(currentState[i]);
		}
	}

}
