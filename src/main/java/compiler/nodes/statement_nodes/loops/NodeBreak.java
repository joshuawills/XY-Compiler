package compiler.nodes.statement_nodes.loops;

import compiler.Generator;
import compiler.nodes.statement_nodes.NodeStatement;
import compiler.Error;

public class NodeBreak implements NodeStatement {
    
    public NodeBreak() {}

    @Override 
    public String toString() {
        return "break";
    }

    public void operator(Generator generator) {
        
        String label = generator.getBottomLabel();
        if (label == null) {
            Error.handleError("GENERATOR", "Attempted use of break outside of loop");
        }
        generator.appendContents("    jmp " + label);

    }


}
