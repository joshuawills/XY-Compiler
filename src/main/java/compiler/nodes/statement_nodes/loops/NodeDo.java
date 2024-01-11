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
        return String.format("do %s while %s" , scope.toString(), expression.toString());
    }

    public void operator(Generator generator) {
        String labelTop = generator.createLabel();
        String labelBottom = generator.createLabel();

        generator.addTopLabel(labelTop);
        generator.addBottomLabel(labelBottom);

        generator.appendContents(labelTop + ": ;; return to do while " + expression.toString());
        scope.operator(generator);
        expression.operator(generator);
        generator.pop("rax");
        generator.appendContents("    test rax, rax");
        generator.appendContents("    jnz " + labelTop);
        generator.appendContents(labelBottom + ":");

        generator.exitLoop();
    }

}
