package main.java;
import java.util.ArrayList;

import main.java.nodes.NodeLet;
import main.java.nodes.NodeProgram;
import main.java.nodes.NodeReturn;
import main.java.nodes.NodeStatement;
import main.java.nodes.expression_nodes.BinaryExpression;
import main.java.nodes.expression_nodes.NodeExpression;
import main.java.nodes.expression_nodes.NodeTerm;
import main.java.nodes.expression_nodes.binary_nodes.AdditionBinExp;
import main.java.nodes.expression_nodes.term_nodes.IdentExpression;
import main.java.nodes.expression_nodes.term_nodes.IntLitExpression;

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

    private BinaryExpression parseBinExpression() { // RETURN NULL IF FAILS TODO
        NodeExpression lhs = parseExpression();
        if (lhs == null) return null;

        if (peek() != null && peek().getType().equals(TokenType.ADD)) { // Addition
            System.out.println("IN HERE");
            BinaryExpression additionExpr = new AdditionBinExp();
            additionExpr.setLHS(lhs);
            consume(); // Consume addition
            NodeExpression rhs = parseExpression();
            if (rhs == null) return null;
            additionExpr.setRHS(rhs);
            return additionExpr;
        } else {
            System.err.println("<Parser> Unsupported binary expression");
            System.exit(1);
        }

        return null;
    }

    private NodeTerm parseTerm() {
        
        System.out.println("A: " + peek().getType());
        if (peek() != null && peek().getType().equals(TokenType.INT_LIT)) {
            IntLitExpression term = new IntLitExpression(consume());
            return term;
        } else if (peek() != null && peek().getType().equals(TokenType.INT_TYPE)) { // int x = 5;
            IdentExpression term  = new IdentExpression(consume());
            return term;
        }
        return null;
    }

    private NodeExpression parseExpression() {
        NodeTerm term = parseTerm();
        System.out.println("CRITICAL: " + term.toString());
        if (term != null) {
            Token additionToken = tryConsume(TokenType.ADD);
            if (additionToken != null) {
                AdditionBinExp addExp = new AdditionBinExp();
                addExp.setLHS(term);
                consume(); // consume addition
                NodeExpression rhs = parseExpression();
                if (rhs == null) return null;
                addExp.setRHS(rhs);
                return addExp;
            } else {
                return term;
            }
        }
        return null;
    }

    private NodeStatement parseStatement() {
        if (peek() != null && peek().getType().equals(TokenType.RETURN)) {
            consume();
            NodeExpression expression = parseExpression();
            NodeReturn statementNode = new NodeReturn();
            if (expression != null) {
                statementNode.setExpression(expression);
            } else {
                System.err.println("<Parser> Invalid expression");
                System.exit(1);
            }
            expect(TokenType.SEMI);
            return statementNode;
        } else if (peek() != null && peek().getType().equals(TokenType.INT_TYPE)) { // int x = 32;
            consume();
            Token ident = expect(TokenType.STRING);
            expect(TokenType.ASSIGN);
            NodeExpression expression = parseExpression();
            if (expression == null) {
                System.err.println("<Parser> Invalid");
            } else {
                NodeLet statementNode = new NodeLet(ident, expression);
                expect(TokenType.SEMI);
                return statementNode;
            }
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

    private Token tryConsume(TokenType type) {
        if (peek() != null && peek().getType().equals(type)) {
            return consume();
        }
        return null;
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
