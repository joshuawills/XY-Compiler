package compiler.nodes.expression_nodes.term_nodes;

import compiler.Generator;
import compiler.Token;
import compiler.TokenType;
import compiler.nodes.expression_nodes.NodeExpression;

public class NegationExpression extends NodeTerm {
    
    private NodeExpression expression;

    public NegationExpression(NodeExpression expression) {
        super(new Token(TokenType.DEFAULT, 0, 0));
        this.expression = expression;
    }

    public NodeExpression getExpression() {
        return this.expression;
    }

    @Override
    public String toString() {
        if (this.expression == null)
            return "{}";
        return String.format("!%s", expression.toString());
    }

    public void operator(Generator generator) {

        generator.appendContents("!");
        expression.operator(generator);
    }

}
