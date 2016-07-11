package generator;

import java.util.HashMap;
import java.util.List;

import checker.AST;
import checker.ASTNode;
import symbols.NonTerminals.NonTerminal;
import symbols.Tokens.Token;

/**
 * Generator which converts an abstract syntax tree into the Jasmin language.
 *
 */
public class JasminGenerator {

	// The symbol table mapping an identifier to its id
	private HashMap<String, Integer> symbolTable = new HashMap<String, Integer>();
	private int ifCount = 0;
	private int loopCount = 0;
	private int negCount = 0;
	private int relCount = 0;

	/**
	 * Given an abstract syntax tree with respect to WhileGrammar, this method
	 * translates it to the Jasmin language which is a textual representation of
	 * Java-Bytecode.
	 * 
	 * @param name
	 *            Name of the program
	 * @param ast
	 *            The abstract syntax tree
	 * @return Jasmin program as a String
	 * @throws GeneratorException
	 *             Exception while generation the Jasmin code
	 */
	public String translateWHILE(String name, AST ast) throws GeneratorException {
		StringBuilder result = new StringBuilder();

		// Define a class with the given name which is a subclass of Object.
		appendString(result, ".class public " + name);
		appendString(result, ".super java/lang/Object");
		appendString(result, ";");
		// Define the standard constructor which calls super().
		appendString(result, "; standard initializer");
		appendString(result, ".method public <init>()V");
		appendString(result, "  aload_0");
		appendString(result, "  invokenonvirtual java/lang/Object/<init>()V");
		appendString(result, "  return");
		appendString(result, ".end method");
		appendString(result, "");
		// Then start building the main method.
		appendString(result, ".method public static main([Ljava/lang/String;)V");
		// It simply reserves 100 registers and a data stack of depth 100 - not
		// beautiful but works for our small examples
		appendString(result, "  ; set limits used by this method");
		appendString(result, "  .limit locals 100");
		appendString(result, "  .limit stack 100");

		// Now walk the abstract syntax tree in-order and translate it to Jasmin
		// code
		// At the same time the symbol table is generated
		ASTNode root = ast.getRoot();
		assert (root.getAlphabet().equals(NonTerminal.START));
		assert (root.getChildren().size() == 2);
		result.append(translateProg(root.getChildren().get(0)));

		// here the main method ends
		appendString(result, "; done");
		appendString(result, "return");
		appendString(result, "");
		appendString(result, ".end method");

		return result.toString();
	}

	private String translateProg(ASTNode node) throws GeneratorException {
		assert (node.getAlphabet().equals(NonTerminal.PROGRAM));
		StringBuilder result = new StringBuilder();

		// TODO implement translation from program to Jasmin code

		return result.toString();
	}

	/**
	 * Generate Jasmin code for an expression.
	 * 
	 * @param node
	 *            The node in the AST.
	 * @return Jasmin code as string.
	 * @throws GeneratorException
	 *             Exception while generating.
	 */
	private String translateExpr(ASTNode node) throws GeneratorException {
		assert (node.getAlphabet().equals(NonTerminal.EXPR));
		StringBuilder result = new StringBuilder();

		// TODO implement translation of expression to Jasmin code.

		return result.toString();
	}

	/**
	 * Generate Jasmin code for reading an int from the console.
	 * 
	 * @return Jasmin code as string.
	 */
	private String translateReadInt() {
		StringBuilder result = new StringBuilder();
		appendString(result, "; int n = Integer.parseInt(System.console().readLine());");
		appendString(result, "; Console c = System.console();");
		appendString(result, "invokestatic java/lang/System/console()Ljava/io/Console;");
		appendString(result, "; Reads one line and stores in a String");
		appendString(result, "invokevirtual java/io/Console/readLine()Ljava/lang/String;");
		appendString(result, "; Parse String to int, do not handle exceptions");
		appendString(result, "invokestatic java/lang/Integer/parseInt(Ljava/lang/String;)I");
		return result.toString();
	}

	/**
	 * Generate Jasmin code for writing a string on the console.
	 * 
	 * @param node
	 *            The node in the AST
	 * @return Jasmin code as string.
	 * @throws GeneratorException
	 *             Exception while generating.
	 */
	private String translateWrite(ASTNode node) throws GeneratorException {
		assert (node.getAlphabet().equals(NonTerminal.OUT));
		StringBuilder result = new StringBuilder();

		assert (node.getChildren().size() == 4);
		ASTNode child = node.getChildren().get(2);
		if (child.getAlphabet() instanceof Token) {
			// out -> WRITE LBRAC STRING RBRAC
			assert (child.getAlphabet().equals(Token.STRING));
			// push PrintStream object
			appendString(result, "getstatic java/lang/System/out Ljava/io/PrintStream;");
			// push String
			// the extra quotes are already part of the string
			appendString(result, "ldc " + child.getAttribute());
			// print to command line
			appendString(result, "invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V");
		} else {
			// out -> WRITE LBRAC expr RBRAC
			assert (child.getAlphabet().equals(NonTerminal.EXPR));
			// evaluate expression

			result.append(translateExpr(child));

			// the result is now on the top of the operand stack
			// cast it to string and write to console
			appendString(result, "invokestatic java/lang/String/valueOf(I)Ljava/lang/String;");
			appendString(result, "; begin syso");
			appendString(result, "astore 0 	; store string object in register 0");
			appendString(result, "getstatic java/lang/System/out Ljava/io/PrintStream;");
			appendString(result, "aload 0   ; load the string");
			appendString(result, "invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V");
			appendString(result, "; end syso");
		}

		return result.toString();
	}

	/**
	 * Append string with newline at the end.
	 * 
	 * @param builder
	 *            Stringbuilder
	 * @param s
	 *            String to append
	 */
	private void appendString(StringBuilder builder, String s) {
		builder.append(s + System.lineSeparator());
	}
}
