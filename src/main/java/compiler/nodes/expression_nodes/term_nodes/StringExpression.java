package compiler.nodes.expression_nodes.term_nodes;

import compiler.Generator;
import compiler.Token;
import compiler.Error;
import compiler.Verifier;

public class StringExpression extends NodeTerm {
    
    public StringExpression(Token token) {
        super(token);
    }

    public StringExpression() {
        super();
    }

    public String getType(Verifier v, Error handler) {
        return "string";
    }

    @Override
    public String toString() {
        if (getToken() == null || getToken().getValue() == null)
            return "{}";
        return String.format("%s", getToken().getValue().toString());
    }

    public void operator(Generator generator) {
        generator.appendContents(getToken().getValue());
    }
}
