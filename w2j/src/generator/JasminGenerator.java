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
            result.append("; Program\n");
            for (ASTNode subNode : node.getChildren()) {
                if(subNode.getAlphabet().equals(NonTerminal.PROGRAM)){
                    result.append(translateProg(subNode));
                }else if(subNode.getAlphabet().equals(NonTerminal.STATEMENT)){
                    result.append(translateStatement(subNode));
                }else{
                    throw new GeneratorException("Unkown node type");
                }
            }

            return result.toString();
	}

        private String translateStatement(ASTNode node) throws GeneratorException{
            assert (node.getAlphabet().equals(NonTerminal.STATEMENT));
            StringBuilder result = new StringBuilder();
            
            List<ASTNode> subNodes = node.getChildren();
            ASTNode first = subNodes.get(0);
            if(first.getAlphabet().equals(NonTerminal.DECLARATION)){
                 result.append(translateDecla(first));
            }else if(first.getAlphabet().equals(NonTerminal.ASSIGNMENT)){
                 result.append(translateAssi(first));
            }else if(first.getAlphabet().equals(NonTerminal.BRANCH)){
                 result.append(translateBranch(first));
            }else if(first.getAlphabet().equals(NonTerminal.LOOP)){
                 result.append(translateLoop(first));
            }else if(first.getAlphabet().equals(NonTerminal.OUT)){
                 result.append(translateWrite(first));
            }else{
                    throw new GeneratorException("Unkown node type");
            }
            
            return result.toString();
        }
        
        private String translateDecla(ASTNode node) throws GeneratorException{
            assert (node.getAlphabet().equals(NonTerminal.DECLARATION));

            ASTNode iden = node.getChildren().get(1);
            symbolTable.put(iden.getAttribute(), symbolTable.size());
            
            return "";
        }
        private String translateAssi(ASTNode node) throws GeneratorException{
            assert (node.getAlphabet().equals(NonTerminal.ASSIGNMENT));
            StringBuilder result = new StringBuilder();
            
            List<ASTNode> subNodes = node.getChildren();
            ASTNode first = subNodes.get(0);
            ASTNode third = subNodes.get(2);
            
            result.append("; Assignment\n");
            
            if(third.getAlphabet().equals(Token.READ)){
                result.append(translateReadInt());
            }else if(third.getAlphabet().equals(NonTerminal.EXPR)){
                result.append(translateExpr(third));
            }
            result.append("istore ");
            int varnum = symbolTable.get(first.getAttribute());
            result.append(varnum);
            result.append("\n");
            return result.toString();
        }
        
        private String translateBranch(ASTNode node) throws GeneratorException{
            assert (node.getAlphabet().equals(NonTerminal.BRANCH));
            StringBuilder result = new StringBuilder();
            
            result.append("; IF\n");
            
            List<ASTNode> children = node.getChildren();
            int programcount = 0;
            for(ASTNode child : children){
                if(child.getAlphabet().equals(NonTerminal.GUARD)){
                    result.append(translateGuard(child));
                    result.append("ifeq ifLabel_"+ifCount+"\n");
                }
                if(child.getAlphabet().equals(NonTerminal.PROGRAM)){
                    programcount++;
                    if(programcount == 2){
                        result.append("; ELSE\n");
                        result.append("ifLabel_"+ifCount+":\n");
                    }
                    
                    result.append(translateProg(child));
                    result.append("goto ifLabelEnd_"+ifCount+"\n");
                }
            }
            if(programcount == 1)
                result.append("ifLabel_"+ifCount+":\n");
            result.append("ifLabelEnd_"+ifCount+":\n");
            
            ifCount++;
            return result.toString();
        }
        private String translateLoop(ASTNode node) throws GeneratorException{
            assert (node.getAlphabet().equals(NonTerminal.LOOP));
            StringBuilder result = new StringBuilder();
            
            List<ASTNode> subNodes = node.getChildren();
            
            ASTNode guard = subNodes.get(2);
            ASTNode program = subNodes.get(5);

            result.append("LoopLabelGuard_"+loopCount+":\n");
            result.append(translateGuard(guard));
            
            result.append("ifeq LoopLabelEnd_"+loopCount+"\n");
            
            result.append(translateProg(program));
            
            // Evaluate guard again
            result.append("goto LoopLabelGuard_"+loopCount+"\n");
            
            result.append("LoopLabelEnd_"+loopCount+":\n");
            
            loopCount++;
            return result.toString();
        }

        private String translateGuard(ASTNode node) throws GeneratorException{
            assert (node.getAlphabet().equals(NonTerminal.GUARD));
            StringBuilder result = new StringBuilder();
            List<ASTNode> subNodes = node.getChildren();
            
            ASTNode first = subNodes.get(0);
            result.append("; Guard\n");
            
            if(first.getAlphabet().equals(NonTerminal.RELATION)){
                result.append(translateRelation(first));
            }
            if(first.getAlphabet().equals(NonTerminal.SUBGUARD)){
                result.append(translateSubguard(first));
            }
            if(first.getAlphabet().equals(Token.LBRACE)){
                result.append(translateSubguard(subNodes.get(1)));
            }
            if(first.getAlphabet().equals(Token.NOT)){
                result.append(translateGuard(subNodes.get(2)));
                result.append("bipush 1");
                result.append("ixor");
            }
            return result.toString();
        }
        
        private String translateSubguard(ASTNode node) throws GeneratorException{
            assert (node.getAlphabet().equals(NonTerminal.SUBGUARD));
            StringBuilder result = new StringBuilder();
            List<ASTNode> subNodes = node.getChildren();
            
            ASTNode first = subNodes.get(0);
            ASTNode second = subNodes.get(1);
            ASTNode third = subNodes.get(2);
            
            result.append(translateGuard(first));
            result.append(translateGuard(third));
            
            if(second.getAlphabet().equals(Token.AND)){
                result.append("iand\n");
            }else{
                result.append("ior\n");
            }
            
            return result.toString();
        }
        private String translateRelation(ASTNode node) throws GeneratorException{
            assert (node.getAlphabet().equals(NonTerminal.RELATION));
            StringBuilder result = new StringBuilder();
            
            List<ASTNode> subNodes = node.getChildren();
            
            ASTNode first = subNodes.get(0);
            ASTNode second = subNodes.get(1);
            ASTNode third = subNodes.get(2);
            
            result.append(translateExpr(first));
            result.append(translateExpr(third));
            
            Token t = (Token) second.getAlphabet();
            
            if(t.equals(Token.LT)){
                result.append("if_icmplt RelLabelTrue_"+relCount+"\n");
            }else if(t.equals(Token.LEQ)){
                result.append("if_icmple RelLabelTrue_"+relCount+"\n");
            }else if(t.equals(Token.EQ)){
                result.append("if_icmpeq RelLabelTrue_"+relCount+"\n");
            }else if(t.equals(Token.NEQ)){
                result.append("if_icmpne RelLabelTrue_"+relCount+"\n");
            }else if(t.equals(Token.GEQ)){
                result.append("if_icmpge RelLabelTrue_"+relCount+"\n");
            }else if(t.equals(Token.GT)){
                result.append("if_icmpgt RelLabelTrue_"+relCount+"\n");
            }
            
            result.append("bipush 0\n");
            result.append("goto RelLabelEnd_"+relCount+"\n");
            
            result.append("RelLabelTrue_"+relCount+":\n");
            result.append("bipush 1\n");
            
            result.append("RelLabelEnd_"+relCount+":\n");
            
            relCount++;
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
                
                result.append("; Expression\n");
                
                List<ASTNode> subNodes = node.getChildren();
                ASTNode first = subNodes.get(0);
                if(first.getAlphabet().equals(Token.NUMBER)){
                    result.append("bipush ");
                    result.append(first.getAttribute());
                    result.append("\n");
                    
                }else if(first.getAlphabet().equals(Token.ID)){
                    result.append("iload ");
                    int varnum = symbolTable.get(first.getAttribute());
                    result.append(varnum);
                    result.append("\n");
                }
                else if(first.getAlphabet().equals(NonTerminal.SUBEXPR)){
                    result.append(translateSubexpr(first));
                }else if(first.getAlphabet().equals(Token.LBRACE)){
                    result.append(translateSubexpr(subNodes.get(1)));
                }

		return result.toString();
	}
        
        private String translateSubexpr(ASTNode node) throws GeneratorException {
            assert (node.getAlphabet().equals(NonTerminal.PROGRAM));
            StringBuilder result = new StringBuilder();

            List<ASTNode> subNodes = node.getChildren();
            ASTNode second = subNodes.get(1);
            
            result.append(translateExpr(subNodes.get(0)));
            result.append(translateExpr(subNodes.get(2)));
            
            symbols.Alphabet a = second.getAlphabet();
            if(a.equals(Token.PLUS)){
                result.append("iadd");
            }else if(a.equals(Token.MINUS)){
                result.append("isub");
            }else if(a.equals(Token.TIMES)){
                result.append("imul");
            }else if(a.equals(Token.DIV)){
                result.append("idiv");
            }
            result.append("\n");
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
