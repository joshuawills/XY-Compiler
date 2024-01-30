package compiler.nodes.expression_nodes.term_nodes;

import java.util.ArrayList;

import compiler.Generator;
import compiler.Token;
import compiler.Verifier;
import compiler.Error;
import compiler.nodes.statement_nodes.NodeStatement;
public class FuncCallNode extends NodeTerm implements NodeStatement {

    private Token identifier; // function name
    private ArrayList<NodeTerm> parameters;
    private boolean isIsolatedCall = false;

    public FuncCallNode(Token identifier, ArrayList<NodeTerm> parameters) {
        this.identifier = identifier;
        this.parameters = parameters;
    }

    public FuncCallNode(Token identifier, ArrayList<NodeTerm> parameters, boolean isIsolatedCall) {
        this.identifier = identifier;
        this.parameters = parameters;
        this.isIsolatedCall = isIsolatedCall;
    }

    public void setIsolated() {
        this.isIsolatedCall = true;
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
        
        return String.format("%s(%s)", identifier.getValue(), buffer);
    }
    
    public String getType(Verifier v, Error handler) {
        Token returnType = v.getFunctionReturnType(identifier.getValue());
        if (returnType == null)
            handler.undeclaredFunction(getFunctionName(), identifier.getLine(), identifier.getCol());
        return v.mapReturnTypes(returnType);
    }

    public String getFunctionName() {
        return this.identifier.getValue();
    }

    public Token getIdentifier() {
        return this.identifier;
    }

    public ArrayList<NodeTerm> getParameters() {
        return this.parameters;
    }

    public void operator(Generator generator) {
        
        generator.appendContents(identifier.getValue() + "(");
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
