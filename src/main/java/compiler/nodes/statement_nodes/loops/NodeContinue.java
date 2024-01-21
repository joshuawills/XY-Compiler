package compiler.nodes.statement_nodes.loops;

import compiler.Generator;
import compiler.nodes.statement_nodes.NodeStatement;
public class NodeContinue implements NodeStatement {
    
    public NodeContinue() {}

    @Override 
    public String toString() {
        return "continue";
    }

    public void operator(Generator generator) {
        generator.appendContents("    continue;\n");
    }


}
