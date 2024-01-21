package compiler.nodes.expression_nodes.term_nodes;

import java.util.ArrayList;

import compiler.Generator;
import compiler.Verifier;
import compiler.nodes.statement_nodes.NodeStatement;
public class FuncCallNode extends NodeTerm implements NodeStatement {

    private String functionName;
    private ArrayList<NodeTerm> parameters;
    private boolean isIsolatedCall = false;

    public FuncCallNode(String functionName, ArrayList<NodeTerm> parameters) {
        this.functionName = functionName;
        this.parameters = parameters;
    }

    
    public FuncCallNode(String functionName, ArrayList<NodeTerm> parameters, boolean isIsolatedCall) {
        this.functionName = functionName;
        this.parameters = parameters;
        this.isIsolatedCall = isIsolatedCall;
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
    
    public String getType(Verifier v) {
        return v.mapReturnTypes(v.getFunctionReturnType(functionName));
    }

    public String getFunctionName() {
        return this.functionName;
    }

    public ArrayList<NodeTerm> getParameters() {
        return this.parameters;
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

        if (this.isIsolatedCall)
            generator.appendContents(";\n");
    }


}
