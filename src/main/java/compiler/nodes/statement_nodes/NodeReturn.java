package compiler.nodes.statement_nodes;

import compiler.Generator;
import compiler.nodes.expression_nodes.NodeExpression;

public class NodeReturn implements NodeStatement {

    private NodeExpression expression = null;
    private boolean isMain = false;
    
    public NodeReturn(NodeExpression expression) {
        this.expression = expression;
    }

    public NodeReturn(NodeExpression expression, boolean isMain) {
        this.expression = expression;
        this.isMain = isMain;
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
        if (expression == null)
            return "{}";

        return String.format("return %s", expression.toString());
    }

    public void operator(Generator generator) {

        if (this.isMain) {
            this.expression.operator(generator);
            generator.appendContents("    mov rax, 60");
            generator.pop("rdi");
            generator.appendContents("    syscall ;; " + this.toString());
            return;
        }

        expression.operator(generator);
        generator.push("rax");
    }

}
