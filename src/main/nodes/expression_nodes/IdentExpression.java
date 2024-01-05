package main.nodes.expression_nodes;

import java.util.ArrayList;

import main.Generator;
import main.Token;

public class IdentExpression implements NodeExpression {
    
    public Token identifier;

    public IdentExpression(Token identifier) {
        this.identifier = identifier; 
    }

    public Token getToken() {
        return this.identifier;
    }
    
    @Override
    public String toString() {
        return String.format("{IdentExpression: %s}", identifier.toString());
    }

    public void operator(Generator generator) {
        String variableName = identifier.getValue();
        if (!generator.getVariables().containsKey(variableName)) { // Doesn't exist
            System.err.println("<Generator> Identifier doesn't exist: " + variableName);
            System.exit(1);
        }

        Integer offset = 8 * (generator.getStackSize() - generator.getVariables().get(variableName) - 1);
        generator.push("QWORD [rsp + " + offset + "]");

    }

}
