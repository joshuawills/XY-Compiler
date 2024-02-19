package compiler.nodes.statement_nodes;

import compiler.Generator;
import compiler.Token;
import compiler.Error;
import compiler.nodes.expression_nodes.NodeExpression;

public class NodeReturn implements NodeStatement {

    private NodeExpression expression = null;
    private Token position;

    public NodeReturn(NodeExpression expression, Token position) {
        this.expression = expression;
        this.position = position;
    }

    public NodeReturn(Token position) { this.position = position; }

    public int getLine() {
        return position.getLine();
    }
    
    public int getCol() {
        return position.getCol();
    }

    public void setExpression(NodeExpression expression) {
        this.expression = expression;
    }


    public NodeExpression getExpression() {
        return this.expression;
    }

    @Override
    public String toString() {
        if (expression == null)
            return "return";
        return String.format("return %s", expression.toString());
    }

    public void operator(Generator generator) {

        Token returnToken = generator.getCurrentFunction().getReturnType();
        generator.appendContents("    return");
        switch (returnToken.getType()) {
            case DECLARE:
                switch (returnToken.getValue()) {
                    case "int":
                    case "bool":
                    case "str":
                        generator.appendContents(" ");
                        expression.operator(generator);
                }
                break;
            case VOID:
                break;
            default:
                Error.handleError("GENERATOR", "Unrecognized return type" + returnToken.getType().toString());
        }
        generator.appendContents(";\n");
    }

}
