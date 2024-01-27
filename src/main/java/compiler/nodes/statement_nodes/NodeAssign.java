package compiler.nodes.statement_nodes;


import compiler.Generator;
import compiler.Token;
import compiler.nodes.expression_nodes.BinaryExpression;
import compiler.nodes.expression_nodes.NodeExpression;
import compiler.nodes.expression_nodes.UnaryExpression;
import compiler.nodes.expression_nodes.term_nodes.Assignable;
import compiler.nodes.expression_nodes.term_nodes.IdentExpression;

public class NodeAssign implements NodeStatement {
    
    private Assignable identifier = null;

    public NodeAssign(Assignable identifier, NodeExpression expression) {
        this.identifier = identifier;
        this.expression = expression;
    }

    public NodeAssign() {

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

    private NodeExpression expression = null;


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
