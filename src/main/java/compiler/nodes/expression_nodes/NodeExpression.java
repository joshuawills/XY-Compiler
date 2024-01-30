package compiler.nodes.expression_nodes;

import compiler.Generator;
import compiler.Verifier;
import compiler.Error;

public interface NodeExpression {
    
    @Override
    public String toString();

    public String getType(Verifier v, Error handler);

    public void operator(Generator generator);
}
