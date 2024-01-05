package main.java.nodes.expression_nodes;

import main.java.Generator;
import main.java.Token;

public interface NodeExpression {
    
    public Token getToken();
    @Override
    public String toString();
    public void operator(Generator generator);
}
