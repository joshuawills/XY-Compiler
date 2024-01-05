package main.java.nodes.expression_nodes;

import main.java.Token;

public abstract class NodeTerm implements NodeExpression {
    
    public Token token = null;

    public NodeTerm(Token token) {
        this.token = token;
    }

    public NodeTerm() {
        
    }

    public void setToken(Token token) {
        this.token = token; 
    }

    public Token getToken() {
        return this.token;
    }


}
