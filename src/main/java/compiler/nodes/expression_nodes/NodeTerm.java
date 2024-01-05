package compiler.nodes.expression_nodes;


import compiler.Token;

public abstract class NodeTerm implements NodeExpression {
    
    public Token token = null;
    public NodeExpression expression = null;

    public NodeTerm(Token token) {
        this.token = token;
    }

    public NodeTerm(NodeExpression expression) {
        this.expression = expression;
    }

    public NodeTerm() {
        
    }

    public void setToken(Token token) {
        this.token = token; 
    }

    public Token getToken() {
        return this.token;
    }

    public void setExpression(NodeExpression expression) {
        this.expression = expression;
    }

    public NodeExpression getExpression() {
        return this.expression;
    } 


}
