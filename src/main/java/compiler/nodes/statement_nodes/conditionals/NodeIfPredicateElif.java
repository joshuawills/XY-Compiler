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
        expression.operator(generator);
        generator.pop("rax");
        String label = generator.createLabel();
        generator.appendContents("    test rax, rax");
        if (predicate == null) {
            generator.appendContents("    jz " + generator.getEndLabel() + " ;; elif" + expression.toString());
        } else {
            generator.appendContents("    jz " + label + " ;; elif" + expression.toString());
        }
        scope.operator(generator);
        generator.appendContents("    jmp " + generator.getEndLabel());
        if (this.predicate != null) {
            generator.appendContents(label + ":");
            predicate.operator(generator);            
        }
    }

}
