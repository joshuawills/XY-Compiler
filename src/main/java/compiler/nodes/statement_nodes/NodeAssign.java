package compiler.nodes.statement_nodes;

import java.util.Optional;

import compiler.Error;
import compiler.Generator;
import compiler.Token;
import compiler.Variable;
import compiler.nodes.expression_nodes.NodeExpression;

public class NodeAssign implements NodeStatement {
    
    private Token identifier = null;

    public NodeAssign(Token identifier, NodeExpression expression) {
        this.identifier = identifier;
        this.expression = expression;
    }

    public NodeAssign() {

    }

    public Token getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Token identifier) {
        this.identifier = identifier;
    }

    public NodeExpression getExpression() {
        return expression;
    }

    public void setExpression(NodeExpression expression) {
        this.expression = expression;
    }

    private NodeExpression expression = null;


    @Override
    public String toString() {
        if (identifier == null || expression == null)
            return "{}";
        return String.format("%s = %s", identifier.getValue(), expression.toString());
    }

    public void operator(Generator generator) {
        String variableName = identifier.getValue();
        if (!generator.getVariables().stream().anyMatch(e -> e.getName().equals(variableName)))
            Error.handleError("GENERATOR", "Attempted reassignment to undeclared identifier: " + variableName);

        if (generator.constant(variableName))
            Error.handleError("GENERATOR", "Attempted reassignment to constant identifier: " + variableName);

        generator.appendContents("    " + variableName + " = ");
        expression.operator(generator);
        generator.appendContents(";\n");

    } 

}
