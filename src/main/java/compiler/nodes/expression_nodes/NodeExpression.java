package compiler.nodes.expression_nodes;

import compiler.Generator;

public interface NodeExpression {
    
    @Override
    public String toString();

    public void operator(Generator generator);
}
