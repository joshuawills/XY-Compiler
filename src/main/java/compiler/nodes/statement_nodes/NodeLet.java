package compiler.nodes.statement_nodes;

import compiler.Error;
import compiler.Generator;
import compiler.Token;
import compiler.nodes.expression_nodes.NodeExpression;

public class NodeLet implements NodeStatement  {

    private Token identifier = null;
    private NodeExpression expression = null;
    
    public NodeLet(Token identifier, NodeExpression expression) {
        this.identifier = identifier;
        this.expression = expression;
    }

    public NodeLet() {}

    public void setExpression(NodeExpression expression) {
        this.expression = expression;
    }

    public void setIdentifier(Token identifier) {
        this.identifier = identifier;
    }

    public NodeExpression getExpression() {
        return this.expression;
    }

    public Token getIdentifier() {
        return this.identifier;
    }

    @Override
    public String toString() {
        if (identifier == null || expression == null)
            return "{}";

        return String.format("let %s = %s", identifier.getValue(), expression.toString());
    }

    public void operator(Generator generator) {
        String variableName = identifier.getValue();
        if (generator.getVariables().stream().anyMatch(e -> e.getName().equals(variableName)))
            Error.handleError("GENERATOR", "Attempted redeclaration of previously declared identifier: " + variableName);
        
        generator.addVariable(variableName);
        this.expression.operator(generator);
    }   

}
