package compiler.nodes.expression_nodes.term_nodes;

import java.util.ArrayList;

import compiler.Generator;
import compiler.Verifier;
import compiler.Error;
import compiler.nodes.expression_nodes.NodeExpression;

public class ArrayExpression extends NodeTerm {
    
    private ArrayList<NodeExpression> expressions = new ArrayList<>();

    public ArrayExpression(ArrayList<NodeExpression> expressions) {
        this.expressions = expressions; 
    }

    public ArrayList<NodeExpression> getExpressions() {
        return this.expressions;
    }

    @Override 
    public String toString() {
        String buffer="[";
        Integer i = 0;
        while (i < expressions.size()) {
            buffer = buffer.concat(expressions.get(i).toString());
            if (i != expressions.size() - 1)
                buffer = buffer.concat(", ");
            i++;
        }
        buffer = buffer.concat("]");
        return buffer;
    }


    public String getType(Verifier v, Error handler) {
        String buffer = "array|";
        if (expressions.size() == 0) {
            return buffer.concat("any");
        } else if (expressions.size() == 1) {
            return buffer.concat(expressions.get(0).getType(v, handler));
        }

        String typeOne = expressions.get(0).getType(v, handler);
        Integer i = 1;
        while (i < expressions.size()) {

            if (!expressions.get(i).getType(v, handler).equals(typeOne))
                Error.handleError("VERIFIER", "Attempting to assign multiple types to a single array");
            i++;
        }
        return buffer.concat(typeOne);
    }

    public void operator(Generator generator) {

        generator.appendContents("{");

        Integer i = 0 ;
        while (i < expressions.size()) {

            expressions.get(i).operator(generator);
            if (i != expressions.size() - 1)
                generator.appendContents(", ");

            i++;
        }

        generator.appendContents("}");


    }

}
