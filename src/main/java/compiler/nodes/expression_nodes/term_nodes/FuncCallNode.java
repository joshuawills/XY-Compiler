package compiler.nodes.expression_nodes.term_nodes;

import java.util.ArrayList;

import compiler.Generator;

public class FuncCallNode extends NodeTerm {

    private String functionName;
    private ArrayList<NodeTerm> parameters;

    public FuncCallNode(String functionName, ArrayList<NodeTerm> parameters) {
        this.functionName = functionName;
        this.parameters = parameters;
    }

    @Override 
    public String toString() {
        String buffer = "";
        for (NodeTerm term: this.parameters) {
            buffer = buffer.concat(term.toString() + ", ");
        }
        if (buffer.length() > 2) {
            buffer = buffer.substring(0, buffer.length() - 2);
        }

        return String.format("%s(%s)", functionName, buffer);
    }

    public void operator(Generator generator) {
        generator.appendContents(functionName + "(");
        int i = 0;
        for (NodeTerm term: parameters) {
            term.operator(generator);
            if (i != parameters.size() - 1) generator.appendContents(", ");
            i++;
        }
        generator.appendContents(")");

    }


}
