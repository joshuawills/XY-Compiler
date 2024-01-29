package compiler;
import java.util.ArrayList;
import java.util.HashMap;

import compiler.nodes.NodeFunction;
import compiler.nodes.NodeParameters;
import compiler.nodes.NodeProgram;
import compiler.nodes.expression_nodes.BinaryExpression;
import compiler.nodes.expression_nodes.NodeExpression;
import compiler.nodes.expression_nodes.UnaryExpression;
import compiler.nodes.expression_nodes.term_nodes.ArrayAccess;
import compiler.nodes.expression_nodes.term_nodes.ArrayExpression;
import compiler.nodes.expression_nodes.term_nodes.CharExpression;
import compiler.nodes.expression_nodes.term_nodes.FuncCallNode;
import compiler.nodes.expression_nodes.term_nodes.IdentExpression;
import compiler.nodes.expression_nodes.term_nodes.IntLitExpression;
import compiler.nodes.expression_nodes.term_nodes.ItExpression;
import compiler.nodes.expression_nodes.term_nodes.NegationExpression;
import compiler.nodes.expression_nodes.term_nodes.NodeTerm;
import compiler.nodes.expression_nodes.term_nodes.ParenExpression;
import compiler.nodes.expression_nodes.term_nodes.StringExpression;
import compiler.nodes.statement_nodes.NodeAssign;
import compiler.nodes.statement_nodes.NodeLet;
import compiler.nodes.statement_nodes.NodePrint;
import compiler.nodes.statement_nodes.NodeReturn;
import compiler.nodes.statement_nodes.NodeScan;
import compiler.nodes.statement_nodes.NodeScope;
import compiler.nodes.statement_nodes.NodeStatement;
import compiler.nodes.statement_nodes.conditionals.NodeIf;
import compiler.nodes.statement_nodes.conditionals.NodeIfPredicate;
import compiler.nodes.statement_nodes.conditionals.NodeIfPredicateElif;
import compiler.nodes.statement_nodes.conditionals.NodeIfPredicateElse;
import compiler.nodes.statement_nodes.loops.NodeBreak;
import compiler.nodes.statement_nodes.loops.NodeContinue;
import compiler.nodes.statement_nodes.loops.NodeDo;
import compiler.nodes.statement_nodes.loops.NodeFor;
import compiler.nodes.statement_nodes.loops.NodeLoop;
import compiler.nodes.statement_nodes.loops.NodeWhile;

public class Parser {

    private final ArrayList<Token> tokens;
    private int iterator = 0;
    private final HashMap<String, String> configSettings;

    public Parser(ArrayList<Token> tokens, HashMap<String, String> configSettings) {
        this.tokens = tokens;
        this.configSettings = configSettings;
    }

    public NodeProgram parseProgram() {

        NodeProgram program = new NodeProgram();
        while (peek() != null)
            program.appendFunction(parseFunction());
        return program;

    }
    
    private NodeParameters parseParameters() {

        NodeParameters p = new NodeParameters();
        expect(TokenType.OPEN_PAREN);

        if (tryConsume(TokenType.CLOSE_PAREN) != null)
            return p;
    
        while (true) {

            boolean isMutable = (tryConsume(TokenType.MUT) != null);
            Token token = expect(TokenType.DECLARE, TokenType.ARRAY);
            String name;
            if (token.getType().equals(TokenType.ARRAY)) {
                expect(TokenType.LESS_THAN);
                String inner = expect(TokenType.DECLARE).getValue();
                expect(TokenType.GREATER_THAN);
                token.setValue(inner);
            }
            name = expect(TokenType.IDENT).getValue();
            if (tryConsume(TokenType.CLOSE_PAREN) != null) {
                p.addVariable(name, token, isMutable);
                break;
            }
            expect(TokenType.COMMA);
            p.addVariable(name, token, isMutable);
        }
        return p;
    }

    private NodeFunction parseFunction() {
        expect(TokenType.DEFINE);
        String functionName = expect(TokenType.IDENT).getValue();
        NodeParameters p = parseParameters();
        expect(TokenType.ARROW);
        Token returnToken = expect(TokenType.DECLARE, TokenType.VOID);
        NodeScope scope = parseScope();
        return new NodeFunction(scope, functionName, returnToken, p);
    }


    private NodeTerm parseTerm() {
        
        Token t = peek();
        NodeExpression e;
        if (t == null) return null;

        switch (t.getType()) {
            case INT_LIT:
                return new IntLitExpression(consume());

            case STRING_LIT:
                return new StringExpression(consume());

            case CHAR_LIT:
                return new CharExpression(consume());

            case IT:
                consume(); return new ItExpression(); 
                
            case OPEN_PAREN:
                consume();
                e = parseExpression(0);
                if (e == null)
                    Error.handleError("PARSING", "Expected expression");
                expect(TokenType.CLOSE_PAREN);
                return new ParenExpression(e);
                
            case NEGATE:
                consume();
                e = parseTerm();
                if (e == null)
                    Error.handleError("PARSING", "Expected expression");
                return new NegationExpression(e);
                
            case IDENT:
                // Not a func call
                Token nextToken = peek(1);
                if (nextToken == null || (!nextToken.getType().equals(TokenType.OPEN_PAREN) && !nextToken.getType().equals(TokenType.LEFT_SQUARE))) {
                    return new IdentExpression(consume());
                }
                
                // Array Access
                if (nextToken.getType().equals(TokenType.LEFT_SQUARE)) {
                    consume(); consume();
                    NodeExpression expression = parseExpression(0);
                    expect(TokenType.RIGHT_SQUARE);
                    return new ArrayAccess(t, expression);
                }
                
                // Is a func call
                return handleFuncCall();
                
            case LEFT_SQUARE:
                consume();  
                ArrayList<NodeExpression> expressions = new ArrayList<>();

                if (tryConsume(TokenType.RIGHT_SQUARE) != null)
                    return new ArrayExpression(expressions);

                while (true) {
                    expressions.add(parseExpression(0));
                    if (tryConsume(TokenType.RIGHT_SQUARE) != null)
                        break;
                    expect(TokenType.COMMA);
                }
                return new ArrayExpression(expressions);

            default:
                return null;
        }
    }

    
    private NodeExpression parseExpression(int minimumPrecedence) { // set to 0 as default
        NodeExpression lhs = parseTerm();
        if (lhs == null) return null;
        Integer level;
        while (true) {
            Token currentToken = peek();
            if (currentToken == null) break;
            level = Token.getBinaryPrecedenceLevel(currentToken.getType());
            if (level == null || level < minimumPrecedence) break;
            
            Token operator = consume();
            NodeExpression rhs = parseExpression(level + 1);
            if (rhs == null)
                Error.handleError("PARSING", "Unable to parse expression");
            
            BinaryExpression myExpression = new BinaryExpression();
            switch (operator.getType()) {
                case GREATER_EQ:
                case GREATER_THAN:
                case LESS_THAN:
                case LESS_EQ:
                case EQUAL:
                case NOT_EQUAL:
                case AND_LOGIC:
                case OR_LOGIC:
                case PLUS:
                case STAR:
                case DASH:
                case F_SLASH:
                case PERCENT:
                case BITWISE_AND:
                case BITWISE_LEFT_SHIFT:
                case BITWISE_OR:
                case BITWISE_RIGHT_SHIFT:
                case BITWISE_XOR:
                    myExpression.setOperator(operator.getType());
                    break;
                default:
                    Error.handleError("PARSING", "Unknown operator");
            }
            
            myExpression.setLHS(lhs);
            myExpression.setRHS(rhs);
            lhs = myExpression;
        }
        return lhs;
    }
    
    private NodeStatement parseIterator() {

        Token t = consume();
        Token ident;
        NodeExpression expression;
        if (t == null) return null;

        switch (t.getType()) {
            case IDENT:
                ident = t;
                if (peek() != null && peek().getType().equals(TokenType.OPEN_PAREN))
                    Error.handleError("PARSER", "No func call in for-loop iterator");
                if (peek() != null && peek().getType().equals(TokenType.LEFT_SQUARE))
                    Error.handleError("PARSER", "No array-access setting in for-loop initialization");
                t = peek();
                switch (t.getType()) {
                case INCREMENT:
                case DECREMENT:
                    consume();
                    expression = new UnaryExpression(t.getType());
                    break;
                case PLUS_EQUAL:
                case DASH_EQUAL:
                case STAR_EQUAL:
                case F_SLASH_EQUAL:
                    consume();
                    NodeExpression rhs = parseExpression(0);
                    expression = new UnaryExpression(t.getType(), rhs);
                    break;
                default:
                    expect(TokenType.ASSIGN);
                    expression = parseExpression(0);
                }
                return new NodeAssign(new IdentExpression(ident), expression);
            default:
                Error.handleError("PARSING", "Unrecognized iterator for a for-loop.");            
        }
        return null;
    }

    private NodeStatement parseInitializer() {

        Token t = consume();
        Token ident;
        NodeExpression expression;
        if (t == null) return null;

        switch (t.getType()) {
            case MUT:
            case DECLARE:
                // Declare
                boolean isConstant = true;
                if (t.getType().equals(TokenType.MUT)) {
                    isConstant = false;
                    t = expect(TokenType.DECLARE);
                }
                ident = expect(TokenType.IDENT);
                expect(TokenType.ASSIGN);
                expression = parseExpression(0);
                return new NodeLet(ident, expression, isConstant, t);

            case IDENT:
                ident = t;
                if (peek() != null && peek().getType().equals(TokenType.OPEN_PAREN)) {
                    Error.handleError("PARSER", "No func call in for-loop initialization");
                }
                if (peek() != null && peek().getType().equals(TokenType.LEFT_SQUARE)) {
                    Error.handleError("PARSER", "No array-access setting in for-loop initialization");
                }
                t = peek();
                switch (t.getType()) {
                case INCREMENT:
                case DECREMENT:
                    consume();
                    expression = new UnaryExpression(t.getType());
                    break;
                case PLUS_EQUAL:
                case DASH_EQUAL:
                case STAR_EQUAL:
                case F_SLASH_EQUAL:
                    consume();
                    NodeExpression rhs = parseExpression(0);
                    expression = new UnaryExpression(t.getType(), rhs);
                break;
                default:
                    expect(TokenType.ASSIGN);
                    expression = parseExpression(0);
                }
                return new NodeAssign(new IdentExpression(ident), expression);
            default:
                Error.handleError("PARSER", "For loop's initializer can only be a variable declaration");
                return null;
        }

    }

    private NodeStatement parseStatement() {
        
        Token t = consume();
        if (t == null) return null;
        NodeExpression expression;
        Token ident;
        NodeScope scope;
        
        switch (t.getType()) {
            case RETURN:
                if (tryConsume(TokenType.SEMI) != null)
                    return new NodeReturn();
                
                expression = parseExpression(0);
                if (expression == null) {
                    if (peek() == null)
                        Error.handleError("Parsing", "Invalid expression near EOF");
                    else
                        Error.handleError("Parsing", "Invalid expression\n    line: " + peek(-1).getLine() + ", col: " + peek(-1).getCol());
                }
                expect(TokenType.SEMI);
                return new NodeReturn(expression);
            
            case MUT:
            case DECLARE:
            case ARRAY:

                // Declare
                boolean isConstant = true;
                if (t.getType().equals(TokenType.MUT)) {
                    isConstant = false;
                    t = expect(TokenType.DECLARE, TokenType.ARRAY);
                }

                if (t.getType().equals(TokenType.ARRAY)) {
                    expect(TokenType.LESS_THAN);
                    Token inner = expect(TokenType.DECLARE);
                    t.setValue(inner.getValue());
                    expect(TokenType.GREATER_THAN);
                }

                ident = expect(TokenType.IDENT);
                expect(TokenType.ASSIGN);
                if (tryConsume(TokenType.IN) != null) {
                    if (t.getType().equals(TokenType.ARRAY))
                        Error.handleError("PARSER", "Can't scan an array");
                    String value = expect(TokenType.STRING_LIT).getValue();
                    expect(TokenType.SEMI);
                    return new NodeScan(value, ident, isConstant, t);
                }
                expression = parseExpression(0);
                expect(TokenType.SEMI);
                return new NodeLet(ident, expression, isConstant, t);
            
            case IF:
                checkParens("if");
                expression = parseExpression(0);
                checkCurly("if");
                return new NodeIf(expression, parseScope(), parseIfPred());
            
            case WHILE:
                checkParens("while");
                expression = parseExpression(0);
                checkCurly("while");
                return new NodeWhile(expression, parseScope());
            
            case FOR:
                expect(TokenType.OPEN_PAREN);
                NodeStatement initializer;
                if (tryConsume(TokenType.SEMI) != null) {
                    initializer = null;
                } else {
                    initializer = parseInitializer();
                    expect(TokenType.SEMI);
                }
                if (tryConsume(TokenType.SEMI) != null) {
                    expression = null;
                } else {
                    expression = parseExpression(0);
                    expect(TokenType.SEMI);
                }
                NodeStatement iterator;
                // The iterator needs work TODO
                if (tryConsume(TokenType.CLOSE_PAREN) != null) {
                    iterator = null;
                } else {
                    iterator = parseIterator();
                    expect(TokenType.CLOSE_PAREN);
                }
                scope = parseScope();
                return new NodeFor(initializer, expression, iterator, scope);

            case LOOP:
                if (peek() != null && peek().getType().equals(TokenType.INT_LIT)) {
                    String count = peek().getValue();
                    consume();
                    return new NodeLoop(parseScope(), count);
                }
                return new NodeLoop(parseScope());
            
            case DO:
                checkCurly("do-while");
                scope = parseScope();
                expect(TokenType.WHILE);
                checkParens("do-while");
                expression = parseExpression(0);
                expect(TokenType.SEMI);
                return new NodeDo(expression, scope);
            
            case CONTINUE:
                expect(TokenType.SEMI);
                return new NodeContinue();
            
            case BREAK:
                expect(TokenType.SEMI);
                return new NodeBreak();
            
            case OUT:
                NodeTerm token = parseTerm();
                expect(TokenType.SEMI);
                return new NodePrint(token);
            
            case OPEN_CURLY:
                scope = parseScope();
                expect(TokenType.CLOSE_CURLY);
                return scope;
            
            case IDENT:
                ident = t;
                boolean isArrayAccess = false;
                if (peek() != null && peek().getType().equals(TokenType.OPEN_PAREN)) {
                    // Func call
                    this.iterator--;
                    FuncCallNode x =  handleFuncCall();
                    x.setIsolated();
                    expect(TokenType.SEMI);
                    return x;
                }
                NodeExpression index = null;
                if (peek() != null && peek().getType().equals(TokenType.LEFT_SQUARE)) {
                    isArrayAccess = true;
                    consume();
                    index = parseExpression(0);
                    expect(TokenType.RIGHT_SQUARE);
                }
                
                t = peek();
                switch (t.getType()) {
                case INCREMENT:
                case DECREMENT:
                    consume();
                    expression = new UnaryExpression(t.getType());
                    break;
                case PLUS_EQUAL:
                case DASH_EQUAL:
                case STAR_EQUAL:
                case F_SLASH_EQUAL:
                    consume();
                    NodeExpression rhs = parseExpression(0);
                    expression = new UnaryExpression(t.getType(), rhs);
                break;
                default:
                    expect(TokenType.ASSIGN);
                    expression = parseExpression(0);
                }

                expect(TokenType.SEMI);
                if (!isArrayAccess)
                    return new NodeAssign(new IdentExpression(ident), expression);
                else 
                    return new NodeAssign(new ArrayAccess(ident, index), expression);
            default:
                this.iterator--; // decrement from original consumption at start of method
                return null;
        }
    }
    
    private FuncCallNode handleFuncCall() {
        String funcName = consume().getValue();
        expect(TokenType.OPEN_PAREN);
        if (tryConsume(TokenType.CLOSE_PAREN) != null) {
            return new FuncCallNode(funcName, new ArrayList<NodeTerm>());
        }

        ArrayList<NodeTerm> parameters = new ArrayList<>();
        while (true) {
            NodeTerm term = parseTerm();
            parameters.add(term);
            if (term == null)
                Error.handleError("PARSING", "Unable to parse term");
            if (tryConsume(TokenType.COMMA) != null) {
                continue;
            } else if (tryConsume(TokenType.CLOSE_PAREN) != null) {
                return new FuncCallNode(funcName, parameters);
            } else {
                Error.handleError("PARSING", "Unexpected token" + consume().toString());
            }
        }
    }

    private NodeScope parseScope() {
        NodeScope scope = new NodeScope();
        if (tryConsume(TokenType.OPEN_CURLY) != null) {
            NodeStatement statement = parseStatement();
            while (statement != null) {
                scope.addStatement(statement);
                statement = parseStatement();
            }
            expect(TokenType.CLOSE_CURLY);
        } else {
            NodeStatement statement = parseStatement();
            scope.addStatement(statement);
        }
        return scope;
    }

    private NodeIfPredicate parseIfPred() {
        Token currentToken = peek();
        if (currentToken != null && currentToken.getType().equals(TokenType.ELIF)) {
            consume();
            checkParens("else if");
            NodeExpression expression = parseExpression(0);
            if (expression == null) {
                if (peek() == null) {
                    Error.handleError("Parsing", "Unable to parse expression");
                } else {
                    Error.handleError("Parsing", "Unable to parse expression\n    line: " + peek(0).getLine() + ", col: " + peek(0).getCol());
                }
            }
            checkCurly("else if");
            return new NodeIfPredicateElif(expression, parseScope(), parseIfPred());
        } else if (currentToken != null && currentToken.getType().equals(TokenType.ELSE)) {
            consume();
            checkCurly("else");
            return new NodeIfPredicateElse(parseScope());
        }
        return null;
    }

    private Token expect(TokenType type) {
        Token currentToken = peek();
        if (currentToken == null)
            Error.handleError("Parsing", String.format("Expected %s, Received nothing", type).concat("\n    line: " + peek().getLine() + ", col: " + peek().getCol()));
        if (!currentToken.getType().equals(type)) {
            Error.handleError("Parsing", String.format("Expected %s, Received %s", type, currentToken.getType()).concat("\n    line: " + peek().getLine() + ", col: " + peek().getCol()));
        }
        this.iterator++;
        return currentToken;
    }

    private Token expect(TokenType typeOne, TokenType typeTwo) {
        Token currentToken = peek();
        if (currentToken == null)
            Error.handleError("Parsing", String.format("Expected %s, Received nothing", typeOne).concat("\n    line: " + peek().getLine() + ", col: " + peek().getCol()));
        if (!(currentToken.getType().equals(typeOne) || currentToken.getType().equals(typeTwo))) {
            Error.handleError("Parsing", String.format("Expected %s, Received %s", typeOne, currentToken.getType()).concat("\n    line: " + peek().getLine() + ", col: " + peek().getCol()));
        }
        this.iterator++;
        return currentToken;
    }

    private Token consume() {
        if (this.iterator >= tokens.size())
            return null;
        Token c = this.tokens.get(this.iterator);
        this.iterator++;
        return c;
    }

    private Token peek() {
        if (this.iterator >= tokens.size())
            return null;
        return this.tokens.get(this.iterator);
    }

    private Token peek(int var) {
        if (this.iterator + var < 0 || this.iterator + var >= tokens.size())
            return null;
        return this.tokens.get(this.iterator + var);
    }

    private Token tryConsume(TokenType type) {
        if (peek() != null && peek().getType().equals(type))
            return consume();
        return null;
    }

    private void checkParens(String area) {
        if (configSettings.containsKey("MANDATE-BRACKETS") && configSettings.get("MANDATE-BRACKETS").equals("true")) {
            if (peek() != null && !peek().getType().equals(TokenType.OPEN_PAREN))
                Error.minorError("CONFIG-SPECIFIC", "Missing () around " + area + " statement\n    line: " + peek(0).getLine() + ", col: " + peek(0).getCol());
        }
    }

    private void checkCurly(String area) {
        if (configSettings.containsKey("MANDATE-BRACKETS") && configSettings.get("MANDATE-BRACKETS").equals("true")) {
            if (peek() != null && !peek().getType().equals(TokenType.OPEN_PAREN))
                Error.minorError("CONFIG-SPECIFIC", "Missing {} around " + area + " statement\n    line: " + peek(0).getLine() + ", col: " + peek(0).getCol());
        }
    }


}
