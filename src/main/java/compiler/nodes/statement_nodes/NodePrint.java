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

    private void printNumArr(Generator generator, String name) {
        String size = "sizeof(" + name + ") / sizeof(" + name +  "[0])";
        String condition = "i <" + size;
        generator.appendContents("printf(\"[\");\n");
        generator.appendContents("for (int i = 0; " + condition + "; i++) {\n");
        generator.appendContents("printf(\"%d\", " + name + "[i]);\n");
        generator.appendContents("if (i + 1 !=" + size + ") printf(\", \");\n");
        generator.appendContents("}\n");
        generator.appendContents("printf(\"]\\n\");\n");
    }

    private void printStringArr(Generator generator, String name) {

        String top = "sizeof(" + name + ")";
        String size = "sizeof(" + name + ") / sizeof(" + name +  "[0])";
        String condition = "i <" + size;
        generator.appendContents("printf(\"[\");\n");
        generator.appendContents("if (" + top + "!= 0 ) {");
        generator.appendContents("for (int i = 0; " + condition + "; i++) {\n");
        generator.appendContents("printf(\"%s\", " + name + "[i]);\n");
        generator.appendContents("if (i + 1 !=" + size + ") printf(\", \");\n");
        generator.appendContents("}\n");
        generator.appendContents("}\n");
        generator.appendContents("printf(\"]\\n\");\n");
    }

    private void printCharArr(Generator generator, String name) {

        String top = "sizeof(" + name + ")";
        String size = "sizeof(" + name + ") / sizeof(" + name +  "[0])";
        String condition = "i <" + size;
        generator.appendContents("printf(\"[\");\n");
        generator.appendContents("if (" + top + "!= 0 ) {");
        generator.appendContents("for (int i = 0; " + condition + "; i++) {\n");
        generator.appendContents("printf(\"%c\", " + name + "[i]);\n");
        generator.appendContents("if (i + 1 !=" + size + ") printf(\", \");\n");
        generator.appendContents("}\n");
        generator.appendContents("}\n");
        generator.appendContents("printf(\"]\\n\");\n");
    }

    public void operator(Generator generator) {
                                                        
        
        if (term instanceof StringExpression) {
            generator.appendContents("    printf(" + term.toString() + ");\n");
            return;
        }
        
        if (term instanceof IdentExpression || term instanceof ArrayAccess) {
            String name = term.getToken().getValue();
            switch (returnType) {
                case "numeric":
                case "array|numeric":
                    if (returnType.equals("array|numeric") && (term instanceof IdentExpression)) {
                        printNumArr(generator, name);
                        return;
                    }
                    generator.appendContents("printf(\"%d\\n\", ");
                    break;
                case "string":
                case "array|string":
                    if (returnType.equals("array|string") && (term instanceof IdentExpression)) {
                        printStringArr(generator, name);
                        return;
                    }
                    generator.appendContents("printf(\"%s\\n\", ");
                    break;
                case "char":
                case "array|char":
                    if (returnType.equals("array|char") && (term instanceof IdentExpression)) {
                        printCharArr(generator, name);
                        return;
                    }
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
