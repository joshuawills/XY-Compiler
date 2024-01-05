package main.java.nodes.expression_nodes.binary_nodes;

import main.java.Generator;
import main.java.nodes.expression_nodes.BinaryExpression;
import main.java.nodes.expression_nodes.NodeExpression;

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
        // to do   
    }

}
