package compiler.nodes;

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
        return String.format("{NodeLet: %s = %s}", identifier.getValue(), expression.toString());
    }

    public void operator(Generator generator) {
        String variableName = identifier.getValue();
        if (generator.getVariables().containsKey(variableName)) { // Already defined
            System.err.println("<Generator> Identifier already declared: " + variableName);
            System.exit(1);
        }
        generator.addVariable(variableName);
        this.expression.operator(generator);
    }   

}
