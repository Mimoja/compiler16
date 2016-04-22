import java.io.FileReader;
import java.io.IOException;

import lexer.*;
import lexer.LexerGenerator.Token;

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

		// Lexical Analysis
		System.out.println("Input: " + inputProgram);
		// Try to recognize with automaton for "while"
		AbstractDFA whileDFA = new DFA("while", Token.WHILE);
		System.out.println("WHILE: " + whileDFA.run(inputProgram));

		// Try to recognize with automaton for comments
		AbstractDFA commentDFA = new CommentDFA();
		System.out.println("COMMENT: " + commentDFA.run(inputProgram));

		// Syntactical Analysis

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
