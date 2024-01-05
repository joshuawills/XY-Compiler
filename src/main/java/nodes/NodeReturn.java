package main.java.nodes;

import java.util.ArrayList;

import main.java.Generator;
import main.java.nodes.expression_nodes.NodeExpression;

public class NodeReturn implements NodeStatement {

    private NodeExpression expression = null;
    
    public NodeReturn(NodeExpression expression) {
        this.expression = expression;
    }

    public NodeReturn() {}

    public void setExpression(NodeExpression expression) {
        this.expression = expression;
    }

    public NodeExpression getExpression() {
        return this.expression;
    }

    @Override
    public String toString() {
        return String.format("{NodeReturn: %s}", expression.getToken().getValue());
    }

    public void operator(Generator generator) {
        this.expression.operator(generator);
        generator.appendContents("    mov rax, 60");
        generator.pop("rdi");
        generator.appendContents("    syscall");
    }

}
