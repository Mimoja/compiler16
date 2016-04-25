package lexer;

import lexer.LexerGenerator.Token;
import java.util.HashMap;
import helper.Pair;

/**
 * DFA recognizing comments.
 */
public class CommentDFA extends AbstractDFA {

	/**
	 * Construct a new DFA that recognizes comments within source code. There
	 * are two kinds of comments: A single line comment starts with // and ends
	 * with a newline and a multiline comment that starts with /* and ends with
	 * * / (without the space)
	 */
	public CommentDFA() {
		token = Token.COMMENT;

		State init = new State("init");
		State which = new State("which");
		State inOneline = new State("inOneline");
		State endOfOneline = new State("endOfOneline");
		State inOnelineCR = new State("inOnelineCR");
		State inMultiline = new State("inMultiline");
		State inMultilineStar = new State("inMultilineStar");
		State endOfMultiline = new State("endOfMultiline");
		State sink = new State("sink");

		transitions.put(new Pair<State, Character>(init, '/'), which);
		transitions.put(new Pair<State, Character>(which, '/'), inOneline);
		transitions.put(new Pair<State, Character>(which, '*'), inMultiline);
		transitions.put(new Pair<State, Character>(inOneline, '\n'), endOfOneline);
		transitions.put(new Pair<State, Character>(inOneline, '\r'), inOnelineCR);
		transitions.put(new Pair<State, Character>(inOnelineCR, '\n'), endOfOneline);
		transitions.put(new Pair<State, Character>(inMultiline, '*'), inMultilineStar);
		transitions.put(new Pair<State, Character>(inMultilineStar, '/'), endOfMultiline);
		transitions.put(new Pair<State, Character>(inMultilineStar, '*'), inMultilineStar);

		// Override some default transitions
		// TODO: a more efficient implementation
		for (int i = 0; i <= 0x10FFF; i++) {
			char c = (char) i;
			if (c != '*')
				transitions.put(new Pair<State, Character>(inMultiline, c), inMultiline);
			if (c != '/' && c != '*')
				transitions.put(new Pair<State, Character>(inMultilineStar, c), inMultiline);
			if (c != '\n' && c != '\r')
				transitions.put(new Pair<State, Character>(inOneline, c), inOneline);
		}

		finalStates.add(endOfOneline);
		finalStates.add(endOfMultiline);
		sinkState = sink;
		initialState = init;
	}
}
