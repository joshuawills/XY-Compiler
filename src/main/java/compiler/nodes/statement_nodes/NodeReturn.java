package compiler.nodes.statement_nodes;

import compiler.Generator;
import compiler.TokenType;
import compiler.Error;
import compiler.nodes.expression_nodes.NodeExpression;

public class NodeReturn implements NodeStatement {

    private NodeExpression expression = null;
    
    public NodeReturn(NodeExpression expression) {
        this.expression = expression;
    }

    public NodeReturn() {}

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

        TokenType returnType = generator.getCurrentFunction().getReturnType();
        generator.appendContents("    return");
        switch (returnType) {
            case INT:
                if (expression == null)
                    Error.handleError("GENERATOR", "Can't return void in int function: " + generator.getCurrentFunction().getFunctionName());
                generator.appendContents(" ");
                expression.operator(generator);
                break;
            case VOID:
                if (expression != null)
                    Error.handleError("GENERATOR", "Can't return expression in void function: " + generator.getCurrentFunction().getFunctionName());
                break;
            default:
                Error.handleError("GENERATOR", "Unrecognized return type" + returnType.toString());
        }
        generator.appendContents(";\n");
    }

}
