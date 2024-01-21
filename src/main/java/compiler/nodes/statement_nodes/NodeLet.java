package compiler.nodes.statement_nodes;

import compiler.Error;
import compiler.Generator;
import compiler.Token;
import compiler.TokenType;
import compiler.nodes.expression_nodes.NodeExpression;

public class NodeLet implements NodeStatement  {

    private Token type;
    private Token identifier = null;
    private NodeExpression expression = null;
    private boolean isConstant;
    
    public NodeLet(Token identifier, NodeExpression expression, boolean isConstant, Token type) {
        this.identifier = identifier;
        this.expression = expression;
        this.isConstant = isConstant;
        this.type = type;
    }

    public NodeLet() {}

    public Token getType() {
        return this.type;
    }

    public boolean isConstant() {
        return this.isConstant;
    }

    public void setExpression(NodeExpression expression) {
        this.expression = expression;
    }

    public void setIdentifier(Token identifier) {
        this.identifier = identifier;
    }

    public NodeExpression getExpression() {
        return this.expression;
    }

    public Token getIdentifier() {
        return this.identifier;
    }

    @Override
    public String toString() {
        if (identifier == null || expression == null)
            return "{}";

        return String.format("let %s = %s", identifier.getValue(), expression.toString());
    }

    public void operator(Generator generator) {
        String variableName = identifier.getValue();
        TokenType thisType = type.getType();
        switch (thisType) {
            case DECLARE:
                switch (type.getValue()) {
                    case "int":
                        generator.appendContents("    int " + variableName + " = ");
                        break;
                    case "string":
                        generator.appendContents("    char *" + variableName + " = ");
                }
                break;
            default:
                Error.handleError("Unrecognized declarative value: " + thisType);
        }
        expression.operator(generator);
        generator.appendContents(";\n");
    }   

}
