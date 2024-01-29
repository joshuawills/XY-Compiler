package compiler.nodes.statement_nodes.loops;

import compiler.Generator;
import compiler.nodes.expression_nodes.NodeExpression;
import compiler.nodes.statement_nodes.NodeScope;
import compiler.nodes.statement_nodes.NodeStatement;

public class NodeWhile implements NodeStatement{
    
    private NodeExpression expression;
    private NodeScope scope;

    public NodeWhile(NodeExpression expression, NodeScope scope) {
        this.expression = expression;
        this.scope = scope;
    }    

    public NodeExpression getExpression() {
        return this.expression;
    }

    public NodeScope getScope() {
        return this.scope;
    }

    @Override
    public String toString() {
        if (expression == null || scope == null)
            return "{}";
        return String.format("while %s %s" , expression.toString(), scope.toString().replace("\n", "\n    "));

    }

    public void operator(Generator generator) {
        generator.appendContents("while (");
        expression.operator(generator);
        generator.appendContents(")\n");
        scope.operator(generator);
    }

}
