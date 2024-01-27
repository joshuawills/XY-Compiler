package compiler.nodes;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import compiler.Token;
import compiler.TokenType;

public class NodeParameters {
    
    // String is the var name, TokenType the type naturally
    private LinkedHashMap<String, Token> variables = new LinkedHashMap<>(); 

    public NodeParameters() {}
    
    public void addVariable(String name, Token type) {
        this.variables.put(name, type);
    }

    public LinkedHashMap<String, Token> getVariables() {
        return this.variables;
    }

    @Override
    public String toString() {
        ArrayList<String> vars = new ArrayList<>();
        for (String x: variables.keySet()) {
            boolean isArr = variables.get(x).getType().equals(TokenType.ARRAY);
            String var = variables.get(x).getValue().toString().toLowerCase();
            if (var.equals("string")) var = "char*";
            if (isArr)
                var = var.concat(" *");
            vars.add(String.format("%s %s", var, x));
        }
        return String.join(", ", vars);
    }

}
