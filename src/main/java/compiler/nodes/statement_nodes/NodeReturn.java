package compiler.nodes.statement_nodes;

import compiler.Generator;
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
            return "{}";

        return String.format("return %s", expression.toString());
    }

    public void operator(Generator generator) {
        generator.appendContents("    return ");
        expression.operator(generator);
        generator.appendContents(";\n");
    }

}
