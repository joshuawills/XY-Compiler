package compiler.nodes.statement_nodes.conditionals;

import compiler.Generator;
import compiler.nodes.statement_nodes.NodeStatement;

public abstract class NodeIfPredicate implements NodeStatement {
    
    public abstract void operator(Generator generator);

}
