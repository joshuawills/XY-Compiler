package compiler.nodes.statement_nodes.conditionals;

import compiler.Generator;
import compiler.nodes.expression_nodes.NodeExpression;
import compiler.nodes.statement_nodes.NodeScope;

public class NodeIfPredicateElif extends NodeIfPredicate {
    
    private NodeExpression expression = null;
    private NodeScope scope = null;
    private NodeIfPredicate predicate = null; // Optional

    public NodeIfPredicateElif() {}

    public NodeIfPredicateElif(NodeExpression expression, NodeScope scope) {
        this.expression = expression;
        this.scope = scope;
    }

    public NodeIfPredicateElif(NodeExpression expression, NodeScope scope, NodeIfPredicate predicate) {
        this.expression = expression;
        this.scope = scope;
        this.predicate = predicate;
    }

    public NodeExpression getExpression() {
        return this.expression;
    }

    public void setExpression(NodeExpression expression) {
        this.expression = expression;
    } 

    public void setScope(NodeScope scope) {
        this.scope = scope;
    }

    public void setPredicate(NodeIfPredicate predicate) {
        this.predicate = predicate;
    }

    public NodeScope getScope() {
        return this.scope;
    }

    public NodeIfPredicate getPredicate() {
        return this.predicate;
    }

    @Override
    public String toString() {
        if (expression == null || scope == null)
        return "{}";

    if (this.predicate == null)
        return String.format("else if %s %s" , expression.toString(), scope.toString());
    else
        return String.format("else if %s %s %s" , expression.toString(), scope.toString(), predicate.toString());
    }

    public void operator(Generator generator) {
        generator.appendContents("    else if (");
        expression.operator(generator);
        generator.appendContents(") \n");
        scope.operator(generator);
        if (predicate != null)
            predicate.operator(generator);
    }

}
