package parser;

import lexer.LexerGenerator.Token;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import lexer.Symbol;

/**
 * Recursive descent parser for assignment of the form: int id = num;
 *
 * Grammar: 1: start -> assignment SEMICOLON EOF 2: assignment -> ID ASSIGN expr
 * 3: expr -> ID subexpr 4: expr -> NUMBER subexpr 5: expr -> LPAR expr RPAR 6:
 * expr -> READ LPAR RPAR subexpr 7: subexpr -> PLUS expr 8: subexpr -> MINUS
 * expr 9: subexpr -> TIMES expr 10: subexpr -> DIV expr 11: subexpr -> MOD expr
 * 12: subexpr -> eps
 */
public class RecursiveDescentParserAssignment extends RecursiveDescentParser {

    /**
     * Starting symbol of the grammar.
     *
     * @throws ParserException Exception from the parser.
     */
    protected void main() throws ParserException {

        rules = start();

        if (!symbols.isEmpty()) {
            printError("Symbols remaining.");
        }
    }

    private List<Integer> start() throws ParserException {
        List<Integer> rules = new LinkedList<>();
        List<Integer> assignmentRules = assignment();
        
        next();
        if (token == Token.SEMICOLON) {
            next();
            if (token == Token.EOF) {
                if (!assignmentRules.isEmpty()) {
                    rules.add(1);
                    rules.addAll(assignmentRules);
                }
            }
        }
        return rules;
    }

    private List<Integer> assignment() {
        List<Integer> rules = new LinkedList<>();
        next();
        if (token == Token.INT) {
            next();
            if (token == Token.ID) {
                next();
                if (token == Token.ASSIGN) {
                    List<Integer> exprRules = expr();
                    if (!exprRules.isEmpty()) {
                        rules.add(2);
                        rules.addAll(exprRules);
                    };
                }
            }
        }

        return rules;
    }

    private List<Integer> expr() {
        List<Integer> rules = new LinkedList<>();
        List<Integer> subexprRules;

        next();
        switch (token) {
            case ID:
                subexprRules = subexpr();
                if (!subexprRules.isEmpty()) {
                    rules.add(3);
                    rules.addAll(subexprRules);
                }
                break;
            case NUMBER:
                subexprRules = subexpr();
                if (!subexprRules.isEmpty()) {
                    rules.add(4);
                    rules.addAll(subexprRules);
                }
                break;
            case LPAR:
                List<Integer> exprRules = expr();
                if (!exprRules.isEmpty()) {
                    next();
                    if (token == Token.RPAR) {
                        rules.add(5);
                        rules.addAll(exprRules);
                    }
                }
                break;
            case READ:
                next();
                if (token == Token.LPAR) {
                    next();
                    if (token == Token.RPAR) {
                        subexprRules = subexpr();
                        if (!subexprRules.isEmpty()) {
                            rules.add(6);
                            rules.addAll(subexprRules);
                        }
                    }
                }
                break;
        }
        return rules;
    }

    private List<Integer> subexpr() {
        List<Integer> rules = new LinkedList<>();
        List<Integer> exprRules;
        Queue<Symbol> bak = new LinkedList<Symbol>(symbols);
        
        next();
        switch (token) {
            case PLUS:
                exprRules = expr();
                if (!exprRules.isEmpty()) {
                    rules.add(7);
                    rules.addAll(exprRules);
                }
                break;
            case MINUS:
                exprRules = expr();
                if (!exprRules.isEmpty()) {
                    rules.add(8);
                    rules.addAll(exprRules);
                }
                break;
            case TIMES:
                exprRules = expr();
                if (!exprRules.isEmpty()) {
                    rules.add(9);
                    rules.addAll(exprRules);
                }
                break;
            case DIV:
                exprRules = expr();
                if (!exprRules.isEmpty()) {
                    rules.add(10);
                    rules.addAll(exprRules);
                }
                break;
            case MOD:
                exprRules = expr();
                if (!exprRules.isEmpty()) {
                    rules.add(11);
                    rules.addAll(exprRules);
                }
                break;
            default:
                symbols = bak;
                rules.add(12);
                break;

        }
        return rules;
    }
}
