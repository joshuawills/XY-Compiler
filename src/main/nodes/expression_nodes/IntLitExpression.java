package main.nodes.expression_nodes;

import java.util.ArrayList;

import main.Generator;
import main.Token;

public class IntLitExpression implements NodeExpression {
    
    public Token intLiteral;

    public IntLitExpression(Token intLiteral) {
        this.intLiteral = intLiteral;
    }

    public Token getToken() {
        return this.intLiteral;
    }
    
    @Override
    public String toString() {
        return String.format("{IntLitExpression: %s}", intLiteral.toString());
    }

    public void operator(Generator generator) {
        generator.appendContents("    mov rax, " + intLiteral.getValue());
        generator.push("rax");
    }

}
