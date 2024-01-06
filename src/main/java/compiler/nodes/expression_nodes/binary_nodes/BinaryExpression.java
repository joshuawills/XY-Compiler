package compiler.nodes.expression_nodes.binary_nodes;

import compiler.Generator;
import compiler.Token;
import compiler.TokenType;
import compiler.nodes.expression_nodes.NodeExpression;

public class BinaryExpression implements NodeExpression {
    
    private NodeExpression lhs = null;
    private NodeExpression rhs = null;
    private TokenType operator = null;

    public BinaryExpression(TokenType operator) {
        this.operator = operator;
    }

    public BinaryExpression() {}

    public BinaryExpression(NodeExpression one, NodeExpression two) {
        this.lhs = one;
        this.rhs = two;
    }

    public void setOperator(TokenType operator) {
        this.operator = operator;
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
    public String toString() {
        switch (operator) {
            case PLUS:
                return String.format("%s + %s", getLHS().toString(), getRHS().toString());
            case DASH:
                return String.format("%s - %s", getLHS().toString(), getRHS().toString());
            case STAR:
                return String.format("%s * %s", getLHS().toString(), getRHS().toString());
            case F_SLASH:
                return String.format("%s / %s", getLHS().toString(), getRHS().toString());
            default:
                return "{}";
        }
    }

    public void operator(Generator generator) {
        this.getRHS().operator(generator);
        this.getLHS().operator(generator);
        generator.pop("rax");
        generator.pop("rbx");
        switch (this.operator) {
            case PLUS:
                generator.appendContents("    add rax, rbx ;; " + this.toString());
                break;
            case DASH:
                generator.appendContents("    sub rax, rbx ;; " + this.toString()); 
                break;
            case STAR:
                generator.appendContents("    mul rbx ;; " + this.toString());
                break;
            case F_SLASH:
                generator.appendContents("    div rbx ;; " + this.toString()); 
                break;
            default:
                System.err.println("How did you manage that?");
                System.exit(1);


        } 
        generator.push("rax");
    }


}
