package compiler.nodes.expression_nodes;

import compiler.Generator;
import compiler.TokenType;
import compiler.Verifier;
import compiler.nodes.expression_nodes.term_nodes.ArrayAccess;
import compiler.nodes.expression_nodes.term_nodes.ItExpression;
import compiler.Error;

public class BinaryExpression implements NodeExpression {
    
    private NodeExpression lhs = null;
    private NodeExpression rhs = null;
    private TokenType operator = null;

    public BinaryExpression(TokenType operator) {
        this.operator = operator;
    }

    public BinaryExpression() {}

    public String getType(Verifier v, Error handler) {

        if (!(lhs instanceof ArrayAccess)) {
            String type = lhs.getType(v, handler);
            if (type.equals("it"))  {
                ItExpression x = (ItExpression) lhs;
                x.setDepth(v.getITCount());
                type = "numeric";
                if (v.getITCount() <= 0) Error.handleError("VERIFIER", "Can't use 'it' keyword outside of loop context");
            }
            if (!(type.equals("numeric") || type.equals("char")))
                Error.handleError("VERIFIER", "Inappropriate attempt to use a non-numeric type in a string expression");
        }

        if (!(rhs instanceof ArrayAccess)) {
            String type = rhs.getType(v, handler);
            if (type.equals("it"))  {
                ItExpression x = (ItExpression) rhs;
                x.setDepth(v.getITCount());
                type = "numeric";
                if (v.getITCount() <= 0) Error.handleError("VERIFIER", "Can't use 'it' keyword outside of loop context");
            }
            if (!(type.equals("numeric") || type.equals("char")))
                Error.handleError("VERIFIER", "Inappropriate attempt to use a non-numeric type in a string expression");
        }

        return "numeric";
    }

    public BinaryExpression(NodeExpression one, NodeExpression two) {
        this.lhs = one;
        this.rhs = two;
    }

    public BinaryExpression(NodeExpression one, NodeExpression two, TokenType operator) {
        this.lhs = one;
        this.rhs = two;
        this.operator = operator;
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
            case GREATER_EQ:
                return String.format("%s >= %s", getLHS().toString(), getRHS().toString());
            case GREATER_THAN:
                return String.format("%s > %s", getLHS().toString(), getRHS().toString());
            case LESS_THAN:
                return String.format("%s < %s", getLHS().toString(), getRHS().toString());
            case LESS_EQ:
                return String.format("%s <= %s", getLHS().toString(), getRHS().toString());
            case EQUAL:
                return String.format("%s == %s", getLHS().toString(), getRHS().toString());
            case NOT_EQUAL:
                return String.format("%s != %s", getLHS().toString(), getRHS().toString());
            case AND_LOGIC:
                return String.format("%s && %s", getLHS().toString(), getRHS().toString());
            case OR_LOGIC:
                return String.format("%s || %s", getLHS().toString(), getRHS().toString());
            case PERCENT:
                return String.format("%s %% %s", getLHS().toString(), getRHS().toString());
            case BITWISE_AND:
                return String.format("%s & %s", getLHS().toString(), getRHS().toString());
            case BITWISE_OR:
                return String.format("%s | %s", getLHS().toString(), getRHS().toString());
            case BITWISE_XOR:
                return String.format("%s ^ %s", getLHS().toString(), getRHS().toString());
            case BITWISE_LEFT_SHIFT:
                return String.format("%s << %s", getLHS().toString(), getRHS().toString());
            case BITWISE_RIGHT_SHIFT:
                return String.format("%s >> %s", getLHS().toString(), getRHS().toString());
            default:
                return "{}";
        }
    }

    public void operator(Generator generator) {
        this.getLHS().operator(generator);
        switch (this.operator) {
            case PLUS:
                generator.appendContents(" + ");
                break;
            case DASH:
                generator.appendContents(" - ");
                break;
            case STAR:
                generator.appendContents(" * ");
                break;
            case F_SLASH:
                generator.appendContents(" / ");
                break;
            case AND_LOGIC:
                generator.appendContents(" && ");
                break;
            case OR_LOGIC:
                generator.appendContents(" || ");
                break;
            case LESS_THAN:
                generator.appendContents(" < ");
                break;
            case LESS_EQ:
                generator.appendContents(" <= ");
                break;
            case GREATER_THAN:
                generator.appendContents(" > ");
                break;
            case GREATER_EQ:
                generator.appendContents(" >= ");
                break;
            case EQUAL:
                generator.appendContents(" == ");
                break;
            case NOT_EQUAL:
                generator.appendContents(" != ");
                break;
            case PERCENT:
                generator.appendContents(" % ");
                break;
            case BITWISE_AND:
                generator.appendContents(" & ");
                break;
            case BITWISE_OR:
                generator.appendContents(" | ");
                break;
            case BITWISE_XOR:
                generator.appendContents(" ^ ");
                break;
            case BITWISE_LEFT_SHIFT:
                generator.appendContents(" << ");
                break;
            case BITWISE_RIGHT_SHIFT:
                generator.appendContents(" >> ");
                break;
            default:   
                Error.handleError("GENERATOR", "Unknown operator: " + this.toString());
        } 
        this.getRHS().operator(generator);
    }


}
