package compiler.nodes.statement_nodes;


import compiler.Generator;
import compiler.Token;
import compiler.nodes.expression_nodes.BinaryExpression;
import compiler.nodes.expression_nodes.NodeExpression;
import compiler.nodes.expression_nodes.UnaryExpression;
import compiler.nodes.expression_nodes.term_nodes.Assignable;

public class NodeAssign implements NodeStatement {
    
    private Assignable identifier = null;
    private NodeExpression expression = null;
    private Token location;

    public NodeAssign(Assignable identifier, NodeExpression expression, Token location) {
        this.identifier = identifier;
        this.expression = expression;
        this.location = location;
    }

    public NodeAssign(Token location) {
        this.location = location;
    }

    public int getLine() {
        return this.location.getLine();
    }

    public int getCol() {
        return this.location.getCol();
    }

    public Assignable getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Assignable identifier) {
        this.identifier = identifier;
    }

    public NodeExpression getExpression() {
        return expression;
    }

    public void setExpression(NodeExpression expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        if (identifier == null || expression == null)
            return "{}";

        if (expression instanceof BinaryExpression) {
            return String.format("%s = %s", identifier.toString(), expression.toString());
        }
        return String.format("%s%s", identifier.toString(), expression.toString());
    }

    public void operator(Generator generator) {

        String variableName = identifier.convert();
        if (expression instanceof UnaryExpression) {
            generator.appendContents(variableName);
        } else {
            generator.appendContents(variableName + " = ");
        }
        expression.operator(generator);
        generator.appendContents(";");
    } 

}
