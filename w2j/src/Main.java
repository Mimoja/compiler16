import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import checker.DeclarationChecker;
import generator.GeneratorException;
import generator.JasminGenerator;
import lexer.*;
import parser.*;
import parser.grammar.*;

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

		if (args.length != 2) {
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

		String outputFilename = args[1];

		// Append symbol for EOF
		inputProgram += "$";

		// Lexical Analysis
		List<Symbol> symbols = null;
		try {
			symbols = LexerGenerator.analyse(inputProgram);
			// System.out.println("Symbol stream: " + symbols);
		} catch (LexerException e) {
			System.out.println("LexErr");
			System.out.println(e.getMessage());
			System.out.println(e.getAnalysisBeforeFailure());
			System.exit(1);
		}

		// Syntactical Analysis
		AbstractGrammar grammar = WhileGrammar.getInstance();
		SLR1Parser parser = new SLR1Parser(grammar);
		List<Rule> analysis = null;
		try {
			analysis = parser.parse(symbols);
			// System.out.println(analysis);
		} catch (ParserException e) {
			System.out.println("ParseErr");
			System.out.println(e.getMessage());
			System.out.println(e.getAnalysisBeforeFailure());
			System.exit(2);
		}

		// Semantical Analysis
		DeclarationChecker checker = new DeclarationChecker(symbols, analysis);
		if (!checker.checkDeclaredBeforeUsed()) {
			System.out.println("SemanticErr");
			System.out.println("Not every variable was declared before use.");
			System.exit(3);
		}

		// Byte Code Generation
		JasminGenerator jasminGenerator = new JasminGenerator();
		String jasminCode = "";
		try {
			jasminCode = jasminGenerator.translateWHILE(getFileName(outputFilename), checker.getAst());
		} catch (GeneratorException e) {
			System.out.println("GeneratorErr");
			System.out.println(e.getMessage());
			System.exit(4);
		}
		System.out.println("JASMIN code:");
		System.out.println(jasminCode);

		try {
			string2File(outputFilename, jasminCode);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
		System.out.println("Jasmin code written to " + outputFilename);
	}

	/**
	 * Show help.
	 */
	public static void showHelp() {
		System.out.println("Usage: java Main PATH_TO_SOURCE_FILE PATH_TO_TARGET_FILE");
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

	/**
	 * Write string to file.
	 * 
	 * @param filename
	 *            Name of file.
	 * @param s
	 *            String to write.
	 * @throws IOException
	 *             FileNotFoundExeption when the file does not exist or cannot
	 *             be written to.
	 */
	public static void string2File(String filename, String s) throws IOException {
		FileWriter writer = new FileWriter(filename);
		writer.write(s);
		writer.close();
	}

	/**
	 * Return filename without extension
	 * 
	 * @param file
	 *            Path to file
	 * @return Name of the file
	 */
	public static String getFileName(String file) {
		String filename = new File(file).getName();
		int pos = filename.lastIndexOf(".");
		if (pos > 0) {
			return filename.substring(0, pos);
		} else {
			return filename;
		}
	}

}
