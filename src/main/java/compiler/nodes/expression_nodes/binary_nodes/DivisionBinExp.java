package compiler.nodes.expression_nodes.binary_nodes;

import compiler.Generator;
import compiler.nodes.expression_nodes.BinaryExpression;
import compiler.nodes.expression_nodes.NodeExpression;

public class DivisionBinExp extends BinaryExpression {
    
    public DivisionBinExp(NodeExpression one, NodeExpression two) {
        super(one, two);
    }

    public DivisionBinExp() {}

    @Override 
    public String toString() {
        return String.format("{%s / %s}", super.getLHS().toString(), super.getRHS().toString());
    }

    public void operator(Generator generator) {
        this.getRHS().operator(generator);
        this.getLHS().operator(generator);
        generator.pop("rax");
        generator.pop("rbx");
        generator.appendContents("    div rbx"); 
        generator.push("rax");
    }

}
