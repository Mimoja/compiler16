import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lexer.*;
import parser.*;
import parser.grammar.*;
import recursive_descent_parser.*;

/**
 * Main class for running the compiler.
 */
public class Main {

	/**
	 * Starting method.
	 * 
	 * @param args
	 *            Arguments which should contain the path to the text file to
	 *            compile.
	 */
	public static void main(String[] args) {
		// If args is not a path to a text file, show help.
		// Otherwise open the file

		String inputProgram = "";

		if (args.length != 1) {
			showHelp();
			System.exit(0);
		} else {
			try {
				inputProgram = file2String(args[0]);
			} catch (IOException e) {
				e.printStackTrace();
				showHelp();
				System.exit(0);
			}
		}

		// Append symbol for EOF
		// inputProgram += "$";

		// Lexical Analysis

		List<Symbol> symbols = null;
		try {
			symbols = LexerGenerator.analyse(inputProgram);
			System.out.println("Symbol stream: " + symbols);
		} catch (LexerException e) {
			System.out.println("LexErr");
			System.out.println(e.getMessage());
			System.out.println(e.getAnalysisBeforeFailure());
		}

		// Syntactical Analysis

		// Recursive descent parser
		// RecursiveDescentParser parser = new
		// RecursiveDescentParserAssignment();
		// try {
		// List<Integer> rules = parser.analyse(symbols);
		// System.out.println("Rule stream: " + rules);
		// System.out.println("ParseErr");
		// } catch (RecursiveDescentParserException e) {
		// System.out.println(e.getMessage());
		// System.out.println(e.getAnalysisBeforeFailure());
		// }

		// Compute LR(0) sets
		System.out.println("Exercise (a)");
		System.out.println("GrammarLR0");
		AbstractGrammar grammarLR0 = GrammarLR0.getInstance();
		LR0SetGenerator generatorLR0 = new LR0SetGenerator(grammarLR0);
		System.out.println("LR(0) sets:");
		generatorLR0.printLR0Sets();
		System.out.println("There are " + generatorLR0.nrStates() + " LR(0) sets.");
		System.out.println();

		System.out.println("GrammarSLR1");
		AbstractGrammar grammarSLR1 = GrammarSLR1.getInstance();
		LR0SetGenerator generatorSLR1 = new LR0SetGenerator(grammarSLR1);
		System.out.println("LR(0) sets:");
		generatorSLR1.printLR0Sets();
		System.out.println("There are " + generatorSLR1.nrStates() + " LR(0) sets.");
		System.out.println();
		
		System.out.println("Exercise (b)");
		System.out.println(generatorLR0.nrConflicts() + " conflicts were detected for GrammarLR0.");
		System.out.println(generatorSLR1.nrConflicts() + " conflicts were detected for GrammarSLR1.");

		// LR(0) parser
		System.out.println("Exercise (c)");
		LR0Parser parserLR0 = new LR0Parser(grammarLR0);
		List<Rule> analysisLR0 = null;
		try {
			analysisLR0 = parserLR0.parse(symbols);
			System.out.print("LR(0) parsing result: ");
			System.out.println(analysisLR0);
		} catch (ParserException e) {
			System.out.println("ParseErr");
			System.out.println(e.getMessage());
			System.out.println(e.getAnalysisBeforeFailure());
		}
		System.out.println();

		// Compute first and follow sets
		System.out.println("Exercise (d)");
		LookAheadGenerator generatorLookAheadLR0 = new LookAheadGenerator(grammarLR0);
		System.out.println("First sets for GrammarLR0:");
		generatorLookAheadLR0.printFirstSets();
		System.out.println("Follow sets for GrammarLR0:");
		generatorLookAheadLR0.printFollowSets();
		System.out.println();

		LookAheadGenerator generatorLookAheadSLR1 = new LookAheadGenerator(grammarSLR1);
		System.out.println("First sets for GrammarSLR1:");
		generatorLookAheadSLR1.printFirstSets();
		System.out.println("Follow sets for GrammarSLR1:");
		generatorLookAheadSLR1.printFollowSets();
		System.out.println();

		// SLR(1) parser
		System.out.println("Exercise (e)");
		SLR1Parser parserSLR1 = new SLR1Parser(grammarSLR1);
		List<Rule> analysisSLR1 = null;
		try {
			analysisSLR1 = parserSLR1.parse(symbols);
			System.out.println(analysisSLR1);
		} catch (ParserException e) {
			System.out.println("ParseErr");
			System.out.println(e.getMessage());
			System.out.println(e.getAnalysisBeforeFailure());
		}

		// Semantical Analysis

		// Byte Code Generation

	}

	/**
	 * Show help.
	 */
	public static void showHelp() {
		System.out.println("Usage: java Main PATH_TO_SOURCE_FILE");
	}

	/**
	 * Convert file to string.
	 * 
	 * @param filename
	 *            File
	 * @return String with content of file.
	 * @throws IOException
	 *             FileNotFoundExeption when the file does not exist or cannot
	 *             be read.
	 */
	public static String file2String(String filename) throws IOException {
		// Try to open file
		FileReader in = new FileReader(filename);

		// Read file into string
		StringBuilder str = new StringBuilder();
		int countBytes = 0;
		char[] bytesRead = new char[512];
		while ((countBytes = in.read(bytesRead)) > 0) {
			str.append(bytesRead, 0, countBytes);
		}

		// Close stream
		in.close();
		return str.toString();
	}

}
