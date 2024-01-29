package compiler.nodes.statement_nodes.loops;

import compiler.Generator;
import compiler.nodes.expression_nodes.NodeExpression;
import compiler.nodes.statement_nodes.NodeScope;
import compiler.nodes.statement_nodes.NodeStatement;

public class NodeFor implements NodeStatement {
    
    private NodeStatement initializer;
    private NodeExpression condition;
    private NodeStatement iterator;
    private NodeScope scope;
    
    public NodeFor(NodeStatement initializer, NodeExpression condition, NodeStatement iterator, NodeScope scope) {
        this.initializer = initializer;
        this.condition = condition;
        this.iterator = iterator;
        this.scope = scope;
    }

    public NodeStatement getInitializer() {
        return this.initializer;
    }

    public NodeExpression getCondition() {
        return this.condition;
    }

    public NodeStatement getIterator() {
        return this.iterator;
    }

    public NodeScope getScope() {
        return this.scope;
    }

    @Override 
    public String toString() {
        String init = (initializer == null) ? "": initializer.toString();
        String cond = (condition == null) ? "": condition.toString();
        String iter = (iterator == null) ? "": iterator.toString();
        return String.format("for (%s; %s; %s) %s", init, cond, iter, scope.toString().replace("\n", "\n    "));
    }

    public void operator(Generator generator) {
     
        generator.appendContents("for (");
        if (initializer != null)
            initializer.operator(generator);
        generator.removeSemicolon();
        generator.appendContents("; ");
        if (condition != null)
            condition.operator(generator);
        generator.removeSemicolon();
        generator.appendContents("; ");
        if (iterator != null)
            iterator.operator(generator);
        generator.removeSemicolon();
        generator.appendContents(")\n");
        scope.operator(generator);
    }
}
