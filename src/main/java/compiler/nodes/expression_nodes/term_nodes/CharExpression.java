package compiler.nodes.expression_nodes.term_nodes;

import compiler.Generator;
import compiler.Token;
import compiler.Error;
import compiler.Verifier;

public class CharExpression extends NodeTerm {
    
    public CharExpression(Token token) {
        super(token);
    }

    public String getType(Verifier v, Error handler) {
        return "char";
    }

    @Override 
    public String toString() {
        if (getToken() == null || getToken().getValue() == null)
            return "{}";
        return getToken().getValue().toString();
    }

    public void operator(Generator generator) {
        generator.appendContents(getToken().getValue());
    }

}
