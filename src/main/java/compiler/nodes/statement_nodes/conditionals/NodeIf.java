package compiler.nodes.statement_nodes.conditionals;


import compiler.Generator;
import compiler.nodes.expression_nodes.NodeExpression;
import compiler.nodes.statement_nodes.NodeScope;
import compiler.nodes.statement_nodes.NodeStatement;

public class NodeIf implements NodeStatement {
    
    private NodeExpression expression = null;
    private NodeScope scope = null;
    private NodeIfPredicate predicate = null;

    public NodeIf(NodeExpression expression, NodeScope scope, NodeIfPredicate predicate) {
        this.expression = expression;
        this.scope = scope;
        this.predicate = predicate;
    }
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

    public NodeIfPredicate getPredicate() {
        return this.predicate;
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

        if (this.predicate == null)
            return String.format("if %s %s" , expression.toString(), scope.toString().replace("\n","\n    "));
        else
            return String.format("if %s %s %s" , expression.toString(), scope.toString().replace("\n","\n   "), predicate.toString());

    }

    public void operator(Generator generator) {

        generator.appendContents("    if (");
        expression.operator(generator);
        generator.appendContents(")\n");
        scope.operator(generator);
        if (predicate != null)
            predicate.operator(generator);

    }

}
