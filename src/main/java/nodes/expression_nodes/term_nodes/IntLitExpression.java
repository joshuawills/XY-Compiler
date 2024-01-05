package main.java.nodes.expression_nodes.term_nodes;

import main.java.Token;
import main.java.nodes.expression_nodes.NodeTerm;
import main.java.Generator;

public class IntLitExpression extends NodeTerm {
    
    public IntLitExpression(Token token) {
        super(token);
    }

    public IntLitExpression() {
        super();
    }

    @Override
    public String toString() {
        return String.format("{IntLitExpression: %s}", getToken().toString());
    }

    public void operator(Generator generator) {
        generator.appendContents("    mov rax, " + getToken().getValue());
        generator.push("rax");
    }

}
