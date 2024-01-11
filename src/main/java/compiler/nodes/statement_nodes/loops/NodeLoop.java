package compiler.nodes.statement_nodes.loops;

import compiler.Generator;
import compiler.nodes.statement_nodes.NodeScope;
import compiler.nodes.statement_nodes.NodeStatement;

public class NodeLoop implements NodeStatement {
    
    private NodeScope scope = null;

    public NodeLoop(NodeScope scope) {
        this.scope = scope;
    }

    public NodeScope getScope() {
        return this.scope;
    }

    @Override
    public String toString() {
        if (scope == null)
            return "{}";
        return String.format("loop %s", scope.toString());

    }

    public void operator(Generator generator) {

        String labelTop = generator.createLabel();
        String labelBottom = generator.createLabel();

        generator.addTopLabel(labelTop);
        generator.addBottomLabel(labelBottom);

        generator.appendContents(labelTop + ": ;; return to loop ");
        scope.operator(generator);
        generator.appendContents("    jmp " + labelTop);
        generator.appendContents(labelBottom + ": ");
        generator.exitLoop();

        
    }

}
