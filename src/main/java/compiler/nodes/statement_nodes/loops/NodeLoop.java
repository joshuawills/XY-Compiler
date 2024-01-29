package compiler.nodes.statement_nodes.loops;

import compiler.Generator;
import compiler.nodes.statement_nodes.NodeScope;
import compiler.nodes.statement_nodes.NodeStatement;

public class NodeLoop implements NodeStatement {
    
    private NodeScope scope = null;
    private String count;
    private Integer depth;

    public NodeLoop(NodeScope scope) {
        this.scope = scope;
    }

    public NodeLoop(NodeScope scope, String count) {
        this.scope = scope;
        this.count = count;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
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
        String keyword = "__lc__" + depth + "_";
        if (count == null)
            generator.appendContents("for (int " + keyword + " = 0;;" + keyword + "++)\n");
        else
            generator.appendContents("for (int " + keyword + " = 0; " + keyword + " < " + count + "; " + keyword + "++)\n");
        scope.operator(generator);
    }

}
