package compiler.nodes.statement_nodes;

import compiler.Generator;
import compiler.Token;
import compiler.Error;
public class NodeScan implements NodeStatement {
    
    private String output;
    private boolean isConstant;
    private Token identifier;
    private Token type;

    public NodeScan(String output, Token identifier, boolean isConstant, Token type) {
        this.output = output;
        this.identifier = identifier;
        this.isConstant = isConstant;
        this.type = type;
    }

    @Override
    public String toString() {
        return String.format("let %s = in %s", identifier.getValue(), output);
    }

    public Token getType() { 
        return this.type;
    }

    public boolean isConstant() {
        return this.isConstant;
    }

    public Token getIdentifier() {
        return this.identifier;
    }

    public void operator(Generator generator) {
        // Need to add string support, generic variable at the moment for the end
        switch (type.getType()) {
            case DECLARE:
                switch (type.getValue()) {
                    case "int":
                        generator.appendContents("    int " + identifier.getValue() + ";\n");
                        generator.appendContents("    printf(" + output + ");\n");
                        generator.appendContents("    scanf(\"%d\", " + "&" + identifier.getValue() + ");\n");
                        break;
                    case "char":
                        // TODO
                        generator.appendContents("    char " + identifier.getValue() + ";\n");
                        generator.appendContents("    printf(" + output + ");\n");
                        generator.appendContents("    scanf(\"%c\", " + "&" + identifier.getValue() + ");\n");
                        break;
                    case "string": // 256 bytes max
                        String name = identifier.getValue();
                        generator.appendContents("    char " + name + "[256 + 1];\n");
                        generator.appendContents("    printf(" + output + ");\n");
                        generator.appendContents("    fgets(" + name + ", sizeof(" + name + "), stdin);\n");
                    }
                break;
            default:
                Error.handleError("GENERATOR", "Attempting to scan in an unrecognized identifier");
        }

    }

}
