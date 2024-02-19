package compiler.nodes;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import compiler.Token;
import compiler.TokenType;

public class NodeParameters {
    
    // String is the var name, TokenType the type naturally
    private LinkedHashMap<String, Token> variables = new LinkedHashMap<>(); 
    private ArrayList<Boolean> mutable = new ArrayList<>();

    public NodeParameters() {}
    
    public void addVariable(String name, Token type, boolean isMutable) {
        this.variables.put(name, type);
        this.mutable.add(isMutable);
    }

    public LinkedHashMap<String, Token> getVariables() {
        return this.variables;
    }

    public Boolean isMutable(Integer i) {
        return mutable.get(i);
    }

    @Override
    public String toString() {
        ArrayList<String> vars = new ArrayList<>();
        for (String x: variables.keySet()) {
            boolean isArr = variables.get(x).getType().equals(TokenType.ARR);
            String var = variables.get(x).getValue().toString().toLowerCase();
            if (var.equals("str")) var = "char*";
            if (isArr)
                var = var.concat(" *");
            vars.add(String.format("%s %s", var, x));
        }
        return String.join(", ", vars);
    }

}
