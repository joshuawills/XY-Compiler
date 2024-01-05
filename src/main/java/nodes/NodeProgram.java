package main.java.nodes;

import java.util.ArrayList;

public class NodeProgram {
    
    private ArrayList<NodeStatement> statements = new ArrayList<>();

    public NodeProgram(ArrayList<NodeStatement> statements) {
        this.statements = statements;
    }

    public NodeProgram() {}

    public void appendStatement(NodeStatement s) {
        this.statements.add(s);
    }

    public ArrayList<NodeStatement> getStatements() {
        return this.statements;
    }
}
