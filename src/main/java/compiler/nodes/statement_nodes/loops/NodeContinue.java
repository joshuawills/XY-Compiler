package compiler.nodes.statement_nodes.loops;

import compiler.Generator;
import compiler.nodes.statement_nodes.NodeStatement;
import compiler.Error;

public class NodeContinue implements NodeStatement {
    
    public NodeContinue() {}

    @Override 
    public String toString() {
        return "continue";
    }

    public void operator(Generator generator) {
        
        String label = generator.getTopLabel();
        if (label == null) {
            Error.handleError("GENERATOR", "Attempted use of continue outside of loop");
        }
        generator.appendContents("    jmp " + label);


    }


}
