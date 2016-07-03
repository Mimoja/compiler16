package checker;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import lexer.Symbol;
import parser.Rule;
import symbols.Alphabet;
import symbols.NonTerminals.NonTerminal;
import symbols.Tokens.Token;

/**
 * Checks if identifiers are declared before using them.
 */
public class DeclarationChecker {

	// AST
	private AST ast;

	/**
	 * Get AST
	 * 
	 * @return AST
	 */
	public AST getAst() {
		return ast;
	}

	/**
	 * Constructor. Requires a right most analysis from the parser to initiate a
	 * new abstract syntax tree. This tree is then used for subsequent semantic
	 * checks.
	 * 
	 * @param symbols
	 *            List of symbols.
	 * @param analysis
	 *            Right most analysis of the parser.
	 */
	public DeclarationChecker(List<Symbol> symbols, List<Rule> analysis) {
		ast = new AST(symbols, analysis);
	}

	/**
	 * Check if every identifier which is used has been declared before.
	 * 
	 * @return true, if everything is correct, false if an identifier used
	 *         before its declaration.
	 */
	public boolean checkDeclaredBeforeUsed() {
		System.out.println(ast);

		ASTNode root = ast.getRoot();
                List<String> varList = new LinkedList<>();
                
                return recursiceTraversal(root, varList);
	}
        
        private boolean recursiceTraversal(ASTNode currentN, List<String> declaredAbove){
 
            boolean isValid = true;
            
            Alphabet currentA = currentN.getAlphabet();
            
            // Everything delcared here is valid in the children but not the 
            // other way round. So we make a temp copy   
            if(currentA == NonTerminal.LOOP 
                    || currentA == NonTerminal.BRANCH 
                    || currentA == NonTerminal.PROGRAM){
                
                List<String> currentlyDeclared = new LinkedList<>(declaredAbove);
                for(ASTNode child : currentN.getChildren()){
                    isValid = isValid && recursiceTraversal(child, currentlyDeclared);
                }
                return isValid;
            }
            
            // current Node is a DELCARATION
            if(currentA == NonTerminal.DECLARATION){
                    declaredAbove.add(findDeclaredID(currentN));
                    return true;
            }
            
            // var is beein used. Has to allready be declared
            if(currentA == Token.ID
                    && (!declaredAbove.contains(currentN.getAttribute()))){
                return false;          
            }
            
            // bridthsearch of the children
            for(ASTNode child : currentN.getChildren()){
                isValid = isValid && recursiceTraversal(child, declaredAbove);
            }
            
            return isValid;
	}
	

	/**
	 * Get declared id as attribute of node.
	 * 
	 * @param node
	 *            Node
	 * @return Declared id.
	 */
	/**
	 * @param node
	 * @return
	 */
	private String findDeclaredID(ASTNode node) {
		assert (node.getAlphabet().equals(NonTerminal.DECLARATION));
		assert (node.getChildren().size() == 2);
		return node.getChildren().get(1).getAttribute();
	}

}
