package main.java.nodes.expression_nodes.term_nodes;

import main.java.Token;
import main.java.nodes.expression_nodes.NodeTerm;
import main.java.Generator;


public class IdentExpression extends NodeTerm {
    
    public IdentExpression(Token token) {
        super(token); 
    }

    public IdentExpression() {
        super();
    }
    
    @Override
    public String toString() {
        return String.format("{IdentExpression: %s}", getToken().toString());
    }

    public void operator(Generator generator) {
        String variableName = getToken().getValue();
        if (!generator.getVariables().containsKey(variableName)) { // Doesn't exist
            System.err.println("<Generator> Identifier doesn't exist: " + variableName);
            System.exit(1);
        }

        Integer offset = 8 * (generator.getStackSize() - generator.getVariables().get(variableName) - 1);
        generator.push("QWORD [rsp + " + offset + "]");

    }

}
