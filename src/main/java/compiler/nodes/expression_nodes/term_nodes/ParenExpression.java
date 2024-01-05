package compiler.nodes.expression_nodes.term_nodes;

import compiler.Generator;
import compiler.nodes.expression_nodes.NodeExpression;
import compiler.nodes.expression_nodes.NodeTerm;

public class ParenExpression extends NodeTerm {
 
    public ParenExpression(NodeExpression expression) {
        super(expression);
    }

    public ParenExpression() {
        super();
    }

    @Override
    public String toString() {
        return String.format("{ParenExpression: %s}", getExpression().toString());
    }

    public void operator(Generator generator) {
        getExpression().operator(generator);
    }

}
