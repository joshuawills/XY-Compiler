package compiler;
import java.util.ArrayList;

import compiler.nodes.NodeProgram;
import compiler.nodes.expression_nodes.NodeExpression;
import compiler.nodes.expression_nodes.binary_nodes.BinaryExpression;
import compiler.nodes.expression_nodes.term_nodes.IdentExpression;
import compiler.nodes.expression_nodes.term_nodes.IntLitExpression;
import compiler.nodes.expression_nodes.term_nodes.NodeTerm;
import compiler.nodes.expression_nodes.term_nodes.ParenExpression;
import compiler.nodes.statement_nodes.NodeLet;
import compiler.nodes.statement_nodes.NodeReturn;
import compiler.nodes.statement_nodes.NodeScope;
import compiler.nodes.statement_nodes.NodeStatement;
import compiler.nodes.statement_nodes.conditionals.NodeIf;
import compiler.nodes.statement_nodes.conditionals.NodeIfPredicate;
import compiler.nodes.statement_nodes.conditionals.NodeIfPredicateElif;
import compiler.nodes.statement_nodes.conditionals.NodeIfPredicateElse;

public class Parser {

    private ArrayList<Token> tokens;
    private int iterator = 0;

    public Parser(ArrayList<Token> tokens) {
        this.tokens = tokens;
    }
    
    public NodeProgram parseProgram() {

        NodeProgram program = new NodeProgram();
        while (peek() != null) {
            NodeStatement statement = parseStatement();
            if (statement == null) {
                System.err.println("<Parser> Invalid statement");
                System.exit(1);
            }
            program.appendStatement(statement);
        }
        return program;
    }

    private NodeTerm parseTerm() {
        
        if (peek() != null && peek().getType().equals(TokenType.INT_LIT)) {
            IntLitExpression term = new IntLitExpression(consume());
            return term;
        } else if (peek() != null && peek().getType().equals(TokenType.IDENT)) { // int x = 5;
            IdentExpression term  = new IdentExpression(consume());
            return term;
        } else if (peek() != null && peek().getType().equals(TokenType.OPEN_PAREN)) {
            consume();
            NodeExpression expression = parseExpression(0);
            if (expression == null) {
                System.err.println("<Parser> Expected expression");
                System.exit(1);
            }
            expect(TokenType.CLOSE_PAREN);
            return new ParenExpression(expression);
        }
        return null;
    }

    private NodeExpression parseExpression(int minimumPrecedence) { // set to 0 as default
        NodeExpression lhs = parseTerm();
        if (lhs == null) return lhs;
        Integer precedenceLevel;
        while (true) {
            Token currentToken = peek();
            if (currentToken == null) break;
            precedenceLevel = Token.getBinaryPrecedenceLevel(currentToken.getType());
            if (precedenceLevel == null || precedenceLevel < minimumPrecedence) break;

            Token operator = consume();
            int nextMinPrec = precedenceLevel + 1;
            NodeExpression rhs = parseExpression(nextMinPrec);
            if (rhs == null) {
                System.err.println("Unable to parse expression");
                System.exit(1);
            }

            BinaryExpression myExpression = new BinaryExpression() {
                
            };
            switch (operator.getType()) {
                case PLUS:
                case STAR:
                case DASH:
                case F_SLASH:
                    myExpression.setOperator(operator.getType());
                    break;
                default:
                    System.err.println("You messed up lmao");
                    System.exit(1);
            }
            myExpression.setLHS(lhs);
            myExpression.setRHS(rhs);
            lhs = myExpression;

        }

        return lhs;

    }

    private NodeStatement parseStatement() {
        if (peek() != null && peek().getType().equals(TokenType.RETURN)) {
            consume();
            NodeExpression expression = parseExpression(0);
            NodeReturn statementNode = new NodeReturn();
            if (expression != null) {
                statementNode.setExpression(expression);
            } else {
                System.err.println("<Parser> Invalid expression");
                System.exit(1);
            }
            expect(TokenType.SEMI);
            return statementNode;
        } else if (peek() != null && peek().getType().equals(TokenType.INIT_INT)) { // int x = 32;
            consume();
            Token ident = expect(TokenType.IDENT);
            expect(TokenType.ASSIGN);
            NodeExpression expression = parseExpression(0);
            if (expression == null) {
                System.err.println("<Parser> Invalid"); System.exit(1);
            } else {
                NodeLet statementNode = new NodeLet(ident, expression);
                expect(TokenType.SEMI);
                return statementNode;
            }
        } else if (peek() != null && peek().getType().equals(TokenType.IF)) { // if () {}
            consume();
            NodeExpression expression = parseExpression(0);
            expect(TokenType.OPEN_CURLY);
            NodeScope scope = new NodeScope();
            NodeStatement statement = parseStatement();
            while (statement != null) {
                scope.addStatement(statement);
                statement = parseStatement();
            }
            expect(TokenType.CLOSE_CURLY);
            // PARSE IF PREDICATE
            return new NodeIf(expression, scope, parseIfPred());

        } else if (peek() != null && peek().getType().equals(TokenType.OPEN_CURLY)) { // Entered a scope
            consume(); // consuming the curly;
            NodeScope scope = new NodeScope();
            NodeStatement statement = parseStatement();
            while (statement != null) {
                scope.addStatement(statement);
                statement = parseStatement();
            }
            expect(TokenType.CLOSE_CURLY);
            return scope;
        }
        return null;
    }

    private NodeIfPredicate parseIfPred() {
        Token currentToken = peek();
        if (currentToken != null && currentToken.getType().equals(TokenType.ELIF)) {
            consume();
            NodeExpression expression = parseExpression(0);
            if (expression == null) {
                System.err.println("Unable to parse expression"); System.exit(1);
            }
            expect(TokenType.OPEN_CURLY);
            NodeScope scope = new NodeScope();
            NodeStatement statement = parseStatement();
            while (statement != null) {
                scope.addStatement(statement);
                statement = parseStatement();
            }
            expect(TokenType.CLOSE_CURLY);
            return new NodeIfPredicateElif(expression, scope, parseIfPred());
        } else if (currentToken != null && currentToken.getType().equals(TokenType.ELSE)) {
            // handle else
            consume();
            expect(TokenType.OPEN_CURLY);
            NodeScope scope = new NodeScope();
            NodeStatement statement = parseStatement();
            while (statement != null) {
                scope.addStatement(statement);
                statement = parseStatement();
            }
            expect(TokenType.CLOSE_CURLY);
            return new NodeIfPredicateElse(scope);
        }

        return null;
    }

    private Token expect(TokenType type) {
        Token currentToken = peek();
        if (currentToken == null) {
            System.err.println(String.format("<Parser> Expected %s, Received nothing", type));
            System.exit(1);
        }
        if (!currentToken.getType().equals(type)) {
            System.err.println(String.format("<Parser> Expected %s, Received %s", type, currentToken.getType()));
            System.exit(1);
        }
        this.iterator++;
        return currentToken;
    }

    private Token consume() {
        if (this.iterator >= tokens.size())
            return null;
        Token current = this.tokens.get(this.iterator);
        this.iterator++;
        return current;
    }

    private Token peek() {
        if (this.iterator >= tokens.size())
            return null;
        return this.tokens.get(this.iterator);
    }

}
