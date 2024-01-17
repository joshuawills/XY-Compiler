package compiler.nodes.statement_nodes.loops;

import compiler.Generator;
import compiler.Error;
import compiler.nodes.statement_nodes.NodeStatement;
public class NodeBreak implements NodeStatement {
    
    public NodeBreak() {}

    @Override 
    public String toString() {
        return "break";
    }

    public void operator(Generator generator) {
        if (!generator.inLoop())
            Error.handleError("GENERATOR", "Attempted use of 'break' outside of loop");

        generator.appendContents("    break;\n");
    }


}
