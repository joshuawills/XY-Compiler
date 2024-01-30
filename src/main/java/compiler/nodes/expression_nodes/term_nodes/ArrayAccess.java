package compiler.nodes.expression_nodes.term_nodes;

import compiler.Generator;
import compiler.Token;
import compiler.Verifier;
import compiler.Error;
import compiler.nodes.expression_nodes.NodeExpression;

public class ArrayAccess extends NodeTerm implements Assignable {
    
    private Token identifier;
    private NodeExpression index;

    public ArrayAccess(Token identifier, NodeExpression index) {
        this.identifier = identifier;
        this.index = index;
    }

    public String getType(Verifier v, Error handler) {
        return v.mapReturnTypes(v.getVariable(identifier.getValue()).getType());
    }

    @Override
    public String toString() {
        return String.format("%s[%s]", identifier.getValue(), index.toString());
    }

    public void operator(Generator generator) {
        generator.appendContents(identifier.getValue());
        generator.appendContents("[");
        index.operator(generator);
        generator.appendContents("]");
    }

    public String convert() {
        return String.format("%s[%s]", identifier.getValue(), index.toString());
    }

}
