package compiler.nodes.statement_nodes.loops;

import compiler.Generator;
import compiler.nodes.expression_nodes.NodeExpression;
import compiler.nodes.statement_nodes.NodeScope;
import compiler.nodes.statement_nodes.NodeStatement;

public class NodeWhile implements NodeStatement{
    
    private NodeExpression expression = null;
    private NodeScope scope = null;

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
        return String.format("while %s %s" , expression.toString(), scope.toString());

    }

    public void operator(Generator generator) {
        String labelTop = generator.createLabel();
        String labelBottom = generator.createLabel();

        generator.appendContents(labelTop + ": ;; return to while " + expression.toString());
        expression.operator(generator);
        generator.pop("rax");
        generator.appendContents("    test rax, rax");
        generator.appendContents("    jz " + labelBottom);
        scope.operator(generator);
        generator.appendContents("    jmp " + labelTop);
        generator.appendContents(labelBottom + ": ");
    }

}
