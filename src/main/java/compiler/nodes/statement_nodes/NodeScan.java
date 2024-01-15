package compiler.nodes.statement_nodes;

import compiler.Generator;
import compiler.Token;

public class NodeScan implements NodeStatement {
    
    private String output;
    private boolean isConstant;
    private Token identifier;

    public NodeScan(String output, Token identifier, boolean isConstant) {
        this.output = output;
        this.identifier = identifier;
        this.isConstant = isConstant;
    }

    @Override
    public String toString() {
        return String.format("int %s = in %s", output, identifier.getValue());
    }

    public void operator(Generator generator) {
        generator.addVariable(identifier.getValue(), this.isConstant);
        generator.appendContents("    int " + identifier.getValue() + ";\n");
        generator.appendContents("    printf(" + output + ");\n");
        generator.appendContents("    scanf(\"%d\", " + "&" + identifier.getValue() + ");\n");
    }

}
