package compiler.nodes.statement_nodes.loops;

import compiler.Generator;
import compiler.nodes.expression_nodes.NodeExpression;
import compiler.nodes.statement_nodes.NodeScope;
import compiler.nodes.statement_nodes.NodeStatement;

public class NodeDo implements NodeStatement {
    
    private NodeExpression expression = null;
    private NodeScope scope = null;

    public NodeDo(NodeExpression expression, NodeScope scope) {
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
        return String.format("do %s while %s" , scope.toString().replace("\n", "\n   "), expression.toString());
    }

    public void operator(Generator generator) {
        generator.addLoop();
        generator.appendContents("    do\n");
        scope.operator(generator);
        generator.appendContents("    while (");
        expression.operator(generator);
        generator.appendContents(");\n");
        generator.removeLoop();
    }

}
