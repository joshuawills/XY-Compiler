package compiler.nodes.expression_nodes;

import compiler.Generator;
import compiler.Token;

public interface NodeExpression {
    
    public Token getToken();
    @Override
    public String toString();
    public void operator(Generator generator);
}
