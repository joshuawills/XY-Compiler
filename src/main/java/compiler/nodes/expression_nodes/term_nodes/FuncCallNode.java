package compiler.nodes.expression_nodes.term_nodes;

import java.util.ArrayList;

import compiler.Generator;
import compiler.TokenType;
import compiler.nodes.statement_nodes.NodeStatement;
import compiler.Error;
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

    public void operator(Generator generator) {

        // Check you're not assigning a void value to an int or whatever
        if (!this.isIsolatedCall) {
            if (!generator.getFunction(functionName).getReturnType().equals(TokenType.INT))
                Error.handleError("GENERATOR", "Function '" + functionName + "' can't be assigned to an int.");

        }

        if (!generator.getFunctionNames().contains(functionName))      
            Error.handleError("GENERATOR", "Function '" + functionName + "' doesn't exist.");

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

        generator.addFunctionCall(functionName);

    }


}
