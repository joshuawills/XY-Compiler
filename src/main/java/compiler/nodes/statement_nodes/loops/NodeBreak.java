package compiler.nodes.statement_nodes.loops;

import compiler.Generator;
import compiler.nodes.statement_nodes.NodeStatement;
public class NodeBreak implements NodeStatement {
    
    public NodeBreak() {}

    @Override 
    public String toString() {
        return "break";
    }

    public void operator(Generator generator) {
        generator.appendContents("    break;\n");
    }


}
