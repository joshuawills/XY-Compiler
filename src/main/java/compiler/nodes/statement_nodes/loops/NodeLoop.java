package compiler.nodes.statement_nodes.loops;

import compiler.Generator;
import compiler.nodes.statement_nodes.NodeScope;
import compiler.nodes.statement_nodes.NodeStatement;

public class NodeLoop implements NodeStatement {
    
    private NodeScope scope = null;
    private String count;

    public NodeLoop(NodeScope scope) {
        this.scope = scope;
    }

    public NodeLoop(NodeScope scope, String count) {
        this.scope = scope;
        this.count = count;
    }

    public NodeScope getScope() {
        return this.scope;
    }

    public String getCount() {
        return this.count;
    }

    @Override
    public String toString() {
        if (scope == null)
            return "{}";
        return String.format("loop%s", scope.toString().replace("\n","\n    "));

    }

    public void operator(Generator generator) {
        if (count == null)
            generator.appendContents("    while (1)\n");
        else
            generator.appendContents("for (int __lc__ = 0; __lc__ < " + count + "; __lc__++)\n");
        scope.operator(generator);
    }

}
