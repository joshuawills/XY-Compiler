package compiler.nodes;

import java.util.ArrayList;
import java.util.HashMap;

import compiler.TokenType;

public class NodeParameters {
    
    // String is the var name, TokenType the type naturally
    private HashMap<String, TokenType> variables = new HashMap<>(); 

    public NodeParameters() {}
    
    public void addVariable(String name, TokenType type) {
        this.variables.put(name, type);
    }

    public HashMap<String, TokenType> getVariables() {
        return this.variables;
    }

    @Override
    public String toString() {
        ArrayList<String> vars = new ArrayList<>();
        for (String x: variables.keySet())
            vars.add(String.format("%s %s", variables.get(x).toString().toLowerCase(), x));
        return String.join(", ", vars);
    }

}
