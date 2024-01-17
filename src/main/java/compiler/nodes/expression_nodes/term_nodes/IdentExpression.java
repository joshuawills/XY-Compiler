package compiler.nodes.expression_nodes.term_nodes;

import compiler.Token;
import compiler.Error;

import compiler.Generator;


public class IdentExpression extends NodeTerm {
    
    public IdentExpression(Token token) {
        super(token); 
    }

    public IdentExpression() {
        super();
    }
    
    @Override
    public String toString() {
        if (getToken() == null || getToken().getValue() == null)
            return "{}";
        return String.format("%s", getToken().getValue().toString());
    }

    public void operator(Generator generator) {
        String variableName = getToken().getValue();
        generator.setUsed(variableName);
        if (!generator.getVariables().stream().anyMatch(e -> e.getName().equals(variableName)))
            Error.handleError("GENERATOR", "Identifier doesn't exist: " + variableName);

        generator.appendContents(variableName);

    }

}
