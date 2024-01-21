package compiler.nodes.expression_nodes;

import compiler.Generator;
import compiler.Verifier;

public interface NodeExpression {
    
    @Override
    public String toString();

    public String getType(Verifier v);

    public void operator(Generator generator);
}
