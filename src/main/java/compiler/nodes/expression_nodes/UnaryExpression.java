package compiler.nodes.expression_nodes;

import compiler.Generator;
import compiler.TokenType;
import compiler.Verifier;

public class UnaryExpression implements NodeExpression {
    
    private TokenType operator;
    private NodeExpression expression = null;

    public UnaryExpression(TokenType operator) {
        this.operator = operator;
    }

    public UnaryExpression(TokenType operator, NodeExpression expression) {
        this.operator = operator;
        this.expression = expression;
    }

    public String getType(Verifier v) {
        if (expression != null)
            expression.getType(v);
        return "numeric";
    }

    @Override
    public String toString() {
        switch (this.operator) {
            case PLUS_EQUAL: 
                return " += " + expression.toString();
            case DASH_EQUAL: 
                return " -= " + expression.toString();
            case STAR_EQUAL: 
                return " *= " + expression.toString();
            case F_SLASH_EQUAL: 
                return " /= " + expression.toString();
            case INCREMENT:
                return "++";
            case DECREMENT:
                return "--";
            default:
                break;
        }
        return "";
    }

    public void operator(Generator generator) {
        switch (this.operator) {
            case PLUS_EQUAL: 
                generator.appendContents(" += ");
                this.expression.operator(generator); break;
            case DASH_EQUAL: 
                generator.appendContents(" -= ");
                this.expression.operator(generator); break;
            case STAR_EQUAL: 
                generator.appendContents(" *= ");
                this.expression.operator(generator); break;
            case F_SLASH_EQUAL: 
                generator.appendContents(" /= ");
                this.expression.operator(generator); break;
            case INCREMENT:
                generator.appendContents("++"); break;
            case DECREMENT:
                generator.appendContents("--"); break;
            default: break;
        }
    }

}
