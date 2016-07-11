package checker;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lexer.Symbol;
import parser.Rule;
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
		//System.out.println(ast);
		try {
			checkDeclaredness(ast.getRoot(), new HashSet<String>());
		} catch (VariableNotDeclaredException e) {
			System.out.println("Undeclared variable " + e.getMessage() + " used! Semantic check failed!");
			return false;
		}
		return true;
	}

	/**
	 * Recursive method for checking the declaredness of identifiers.
	 * 
	 * @param node
	 *            Node to start from.
	 * @param declaredIDs
	 *            Set of already declared ids.
	 * @return Set of declared ids in this AST with root node node.
	 * @throws VariableNotDeclaredException
	 *             Thrown when a variable is used but not declared before.
	 */
	private Set<String> checkDeclaredness(ASTNode node, Set<String> declaredIDs) throws VariableNotDeclaredException {
		// base cases:
		if (node.getAlphabet().equals(NonTerminal.DECLARATION)) {
			// Case 1: the subtree contains a declaration
			declaredIDs.add(findDeclaredID(node));
		} else if (node.getAlphabet() instanceof Token) {
			// Case 2: the subtree is a leaf. If it is of type ID check whether
			// it
			// has been declared before.
			if (node.getAlphabet().equals(Token.ID)) {
				// check declaredness
				if (!declaredIDs.contains(node.getAttribute())) {
					// Variable was not declared before.
					throw new VariableNotDeclaredException(node.getAttribute());
				}
			}
		}
		// other cases: we are in an inner node where information is just passed
		// up and down, however we must be careful with scopes
		// Case 3: the subtree is rooted in a new scope, i.e. a while or if
		// structure
		// Copy declared IDs into a fresh set, such that IDs that are found
		// in the subtree are not passed on to upper levels
		else if (node.getAlphabet().equals(NonTerminal.LOOP)) {
			Set<String> copyDeclaredIDs = new HashSet<String>();
			copyDeclaredIDs.addAll(declaredIDs);
			for (ASTNode subNode : node.getChildren()) {
				checkDeclaredness(subNode, copyDeclaredIDs);
			}
		} else if (node.getAlphabet().equals(NonTerminal.BRANCH)) {
			Set<String> copyDeclaredIDs = new HashSet<String>();
			copyDeclaredIDs.addAll(declaredIDs);
			for (ASTNode subNode : node.getChildren()) {
				if (subNode.getAlphabet().equals(NonTerminal.PROGRAM)) {
					copyDeclaredIDs = new HashSet<String>();
					copyDeclaredIDs.addAll(declaredIDs);
				}
				checkDeclaredness(subNode, copyDeclaredIDs);
			}
		} else {
			// Case 4: none of the above applies, just pass on gathered
			// information, check subtree and pass information up
			for (ASTNode subNode : node.getChildren()) {
				checkDeclaredness(subNode, declaredIDs);
			}
		}
		return declaredIDs;
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
