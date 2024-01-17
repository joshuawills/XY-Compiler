package compiler.nodes.statement_nodes.loops;

import compiler.Generator;
import compiler.Error;
import compiler.nodes.statement_nodes.NodeStatement;
public class NodeContinue implements NodeStatement {
    
    public NodeContinue() {}

    @Override 
    public String toString() {
        return "continue";
    }

    public void operator(Generator generator) {
        if (!generator.inLoop())
            Error.handleError("GENERATOR", "Attempted use of 'continue' outside of loop");

        generator.appendContents("    continue;\n");
    }


}
