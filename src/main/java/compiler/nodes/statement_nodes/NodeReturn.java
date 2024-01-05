package compiler.nodes.statement_nodes;

import compiler.Generator;
import compiler.nodes.expression_nodes.NodeExpression;

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
        if (expression == null || expression.getToken() == null)
            return "{NodeReturn}";

        return String.format("{NodeReturn: %s}", expression.getToken().getValue());
    }

    public void operator(Generator generator) {
        this.expression.operator(generator);
        generator.appendContents("    mov rax, 60");
        generator.pop("rdi");
        generator.appendContents("    syscall");
    }

}
