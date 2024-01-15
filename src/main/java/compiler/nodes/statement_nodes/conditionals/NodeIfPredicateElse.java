package compiler.nodes.statement_nodes.conditionals;

import compiler.Generator;
import compiler.nodes.statement_nodes.NodeScope;

public class NodeIfPredicateElse extends NodeIfPredicate {
    
    private NodeScope scope;

    public NodeIfPredicateElse() {}

    public NodeIfPredicateElse(NodeScope scope) {
        this.scope = scope;
    }

    public NodeScope getScope() {
        return this.scope;
    }

    public void setScope(NodeScope scope) {
        this.scope = scope;
    }

    @Override
    public String toString() {
        if (scope == null)
            return "{}";

        return String.format("else %s", scope.toString());
    }

    public void operator(Generator generator) {
        generator.appendContents("    else\n");
        scope.operator(generator);
    }

}
