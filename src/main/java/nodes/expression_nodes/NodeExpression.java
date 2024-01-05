package main.java.nodes.expression_nodes;

import main.java.Token;
import main.java.Generator;

public interface NodeExpression {
    
    public Token getToken();
    @Override
    public String toString();
    public void operator(Generator generator);
}
