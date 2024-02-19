package compiler.nodes.statement_nodes;


import compiler.Generator;
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
        String condition = "__lc__ <" + size;
        generator.appendContents("printf(\"[\");\n");
        generator.appendContents("for (int __lc__ = 0; " + condition + "; __lc__++) {\n");
        generator.appendContents("printf(\"%d\", " + name + "[__lc__]);\n");
        generator.appendContents("if (__lc__ + 1 !=" + size + ") printf(\", \");\n");
        generator.appendContents("}\n");
        generator.appendContents("printf(\"]\\n\");\n");
    }

    private void printStringArr(Generator generator, String name) {

        String top = "sizeof(" + name + ")";
        String size = "sizeof(" + name + ") / sizeof(" + name +  "[0])";
        String condition = "__lc__ <" + size;
        generator.appendContents("printf(\"[\");\n");
        generator.appendContents("if (" + top + "!= 0 ) {");
        generator.appendContents("for (int __lc__ = 0; " + condition + "; __lc__++) {\n");
        generator.appendContents("printf(\"%s\", " + name + "[__lc__]);\n");
        generator.appendContents("if (__lc__ + 1 !=" + size + ") printf(\", \");\n");
        generator.appendContents("}\n");
        generator.appendContents("}\n");
        generator.appendContents("printf(\"]\\n\");\n");
    }

    private void printCharArr(Generator generator, String name) {

        String top = "sizeof(" + name + ")";
        String size = "sizeof(" + name + ") / sizeof(" + name +  "[0])";
        String condition = "__lc__ <" + size;
        generator.appendContents("printf(\"[\");\n");
        generator.appendContents("if (" + top + "!= 0 ) {");
        generator.appendContents("for (int __lc__ = 0; " + condition + "; __lc__++) {\n");
        generator.appendContents("printf(\"%c\", " + name + "[__lc__]);\n");
        generator.appendContents("if (__lc__ + 1 !=" + size + ") printf(\", \");\n");
        generator.appendContents("}\n");
        generator.appendContents("}\n");
        generator.appendContents("printf(\"]\\n\");\n");
    }

    public void operator(Generator generator) {
               
        if (term instanceof StringExpression) {
            generator.appendContents("    printf(" + term.toString() + ");\n");
            return;
        }
        switch (returnType) {
            case "str":
            generator.appendContents("printf(\"%s\\n\", ");
            break;
            case "it":
            case "numeric":
                generator.appendContents("printf(\"%d\\n\", ");
                break;
            case "char":
                generator.appendContents("printf(\"%c\\n\", ");
                break;
            case "array|numeric":
                printNumArr(generator, term.getToken().getValue()); return;
            case "array|str":
                printStringArr(generator, term.getToken().getValue()); return;
            case "array|char":
                printCharArr(generator, term.getToken().getValue()); return;
        }
        term.operator(generator);
        generator.appendContents(");");
    }

}
