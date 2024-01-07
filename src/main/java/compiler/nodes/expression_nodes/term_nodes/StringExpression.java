package compiler.nodes.expression_nodes.term_nodes;

import compiler.Generator;
import compiler.Token;

public class StringExpression extends NodeTerm {
    
      public StringExpression(Token token) {
        super(token);
    }

    public StringExpression() {
        super();
    }

    @Override
    public String toString() {
        if (getToken() == null || getToken().getValue() == null)
            return "{}";
        return String.format("%s", getToken().getValue().toString());
    }

    public void operator(Generator generator) {
        generator.appendContents("    mov rax, " + getToken().getValue());
        generator.push("rax");
    }
}
