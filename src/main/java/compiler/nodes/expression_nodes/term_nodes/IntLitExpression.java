package compiler.nodes.expression_nodes.term_nodes;

import compiler.Token;
import compiler.Verifier;
import compiler.Generator;
import compiler.Error;

public class IntLitExpression extends NodeTerm {
    
    public IntLitExpression(Token token) {
        super(token);
    }

    public IntLitExpression() {
        super();
    }

    // should be handled specifically in Verifier class
    public String getType(Verifier v, Error handler) {
        return "numeric";
    }

    @Override
    public String toString() {
        if (getToken() == null || getToken().getValue() == null)
            return "{}";
        return String.format("%s", getToken().getValue().toString());
    }

    public void operator(Generator generator) {
        generator.appendContents(getToken().getValue().toString());
    }

}
