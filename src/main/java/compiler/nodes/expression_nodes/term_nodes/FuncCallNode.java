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

        return String.format("%s(%s)", functionName, buffer.substring(0, buffer.length() - 2));
    }

    public void operator(Generator generator) {
        for (NodeTerm term: parameters) {
            term.operator(generator);
            generator.push("rax");
        }
        generator.appendContents("    call " + functionName);

    }


}
