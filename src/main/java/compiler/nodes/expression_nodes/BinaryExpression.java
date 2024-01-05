package compiler.nodes.expression_nodes;

import compiler.Generator;
import compiler.Token;

public abstract class BinaryExpression implements NodeExpression {
    
    private NodeExpression lhs = null;
    private NodeExpression rhs = null;

    public BinaryExpression() {
        
    }

    public BinaryExpression(NodeExpression one, NodeExpression two) {
        this.lhs = one;
        this.rhs = two;
    }

    public void setLHS(NodeExpression lhs) {
        this.lhs = lhs;
    }

    public void setRHS(NodeExpression rhs) {
        this.rhs = rhs;
    }

    public NodeExpression getLHS() {
        return this.lhs;
    }

    public NodeExpression getRHS() {
        return this.rhs;
    }

    public Token getToken() {
        return lhs.getToken();
    }

    @Override 
    public abstract String toString();

    public abstract void operator(Generator generator);


}
