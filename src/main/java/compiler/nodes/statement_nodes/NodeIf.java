package compiler.nodes.statement_nodes;


import compiler.Generator;
import compiler.nodes.expression_nodes.NodeExpression;

public class NodeIf implements NodeStatement {
    
    private NodeExpression expression = null;
    private NodeScope scope = null;

    public NodeIf(NodeExpression expression, NodeScope scope) {
        this.expression = expression;
        this.scope = scope;
    }
    public NodeIf() {}
    public NodeIf(NodeExpression expression) { this.expression = expression; }
    public NodeIf(NodeScope scope) { this.scope = scope; }

    public NodeExpression getExpression() {
        return this.expression;
    }

    public NodeScope getScope() {
        return this.scope;
    }

    public void setExpression(NodeExpression expression) {
        this.expression = expression;
    }

    public void setScope(NodeScope scope) {
        this.scope = scope;
    }

    @Override
    public String toString() {
        if (expression == null || scope == null)
            return "{}";

        return String.format("if %s %s" , expression.toString(), scope.toString());
    }

    public void operator(Generator generator) {
        expression.operator(generator);
        generator.pop("rax");
        String label = generator.createLabel();
        generator.appendContents("    test rax, rax");
        generator.appendContents("    jz " + label + " ;; if " + expression.toString());
        scope.operator(generator);
        generator.appendContents(label + ":");
    }




}
