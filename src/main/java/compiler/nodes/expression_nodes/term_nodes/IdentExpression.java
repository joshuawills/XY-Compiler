package compiler.nodes.expression_nodes.term_nodes;

import compiler.Token;
import compiler.Variable;
import compiler.Verifier;
import compiler.Generator;
import compiler.Error;


public class IdentExpression extends NodeTerm implements Assignable {
    
    public IdentExpression(Token token) {
        super(token); 
    }

    public IdentExpression() {
        super();
    }
    // should be handled specifically in Verifier class
    public String getType(Verifier v, Error handler) {
        String variableName = getToken().getValue();
        Variable x = v.getVariable(variableName);

        if (x == null)
            handler.undeclaredVariable(variableName, getToken().getLine(), getToken().getCol());

        x.setUsed();
        return v.mapReturnTypes(x.getType());
    }
    @Override
    public String toString() {
        if (getToken() == null || getToken().getValue() == null)
            return "{}";
        return String.format("%s", getToken().getValue().toString());
    }

    public void operator(Generator generator) {
        String variableName = getToken().getValue();
        generator.appendContents(variableName);

    }

    public String convert() {
        return getToken().getValue();
    }

}
