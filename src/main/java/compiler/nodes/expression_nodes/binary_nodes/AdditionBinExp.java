package compiler.nodes.expression_nodes.binary_nodes;

import compiler.Generator;
import compiler.nodes.expression_nodes.BinaryExpression;
import compiler.nodes.expression_nodes.NodeExpression;

public class AdditionBinExp extends BinaryExpression {
    
    public AdditionBinExp(NodeExpression one, NodeExpression two) {
        super(one, two);
    }

    public AdditionBinExp() {}

    @Override 
    public String toString() {
        return String.format("{%s + %s}", super.getLHS().toString(), super.getRHS().toString());
    }

    public void operator(Generator generator) {
        this.getLHS().operator(generator);
        this.getRHS().operator(generator);
        generator.pop("rax");
        generator.pop("rbx");
        generator.appendContents("    add rax, rbx");
        generator.push("rax");
    }

}
