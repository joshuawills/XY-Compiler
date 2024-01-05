package main.nodes.expression_nodes;
import java.util.ArrayList;

import main.Generator;
import main.Token;

public interface NodeExpression {
    
    public Token getToken();
    @Override
    public String toString();
    public void operator(Generator generator);
}
