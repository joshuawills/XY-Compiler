package compiler.nodes.statement_nodes;


import compiler.Generator;
import compiler.nodes.expression_nodes.term_nodes.ArrayAccess;
import compiler.nodes.expression_nodes.term_nodes.IdentExpression;
import compiler.nodes.expression_nodes.term_nodes.NodeTerm;
import compiler.nodes.expression_nodes.term_nodes.StringExpression;

public class NodePrint implements NodeStatement {
    
    private NodeTerm term;
    String returnType = null;

    public NodePrint(NodeTerm term) {
        this.term = term;
    }

    public void setReturnType(String t) {
        this.returnType = t;
    }

    public NodeTerm getTerm() {
        return this.term;
    }

    @Override
    public String toString() {
        if (term == null)
            return "{}";

        return String.format("out %s", term.toString());
    }

    public void operator(Generator generator) {
                                                        
        
        if (term instanceof StringExpression) {
            generator.appendContents("    printf(" + term.toString() + ");\n");
            return;
        }
        
        if (term instanceof IdentExpression || term instanceof ArrayAccess) {
            switch (returnType) {
                case "numeric":
                case "array|numeric":
                    generator.appendContents("printf(\"%d\\n\", ");
                    break;
                case "string":
                case "array|string":
                    generator.appendContents("printf(\"%s\\n\", ");
                    break;
                case "char":
                case "array|char":
                    generator.appendContents("printf(\"%c\\n\", ");
                    break;
                default:
            }
        } else {

            generator.appendContents("printf(\"%d\\n\", ");
        }
        term.operator(generator);
        generator.appendContents(");");
    }

}
