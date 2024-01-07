package compiler.nodes.expression_nodes.term_nodes;

import compiler.Token;
import compiler.Variable;
import compiler.Error;
import java.util.Optional;

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
        if (!generator.getVariables().stream().anyMatch(e -> e.getName().equals(variableName)))
            Error.handleError("GENERATOR", "Identifier doesn't exist: " + variableName);

        Optional<Integer> stackLocation = generator.getVariables()
                .stream()
                .filter(e -> e.getName().equals(variableName))
                .map(Variable::getStackLocation)
                .findFirst();
        if (stackLocation.isPresent()) {
            Integer offset = 8 * (generator.getStackSize() - stackLocation.get() - 1);
            generator.push("QWORD [rsp + " + offset + "] ;; " + this.toString());
        }

    }

}
