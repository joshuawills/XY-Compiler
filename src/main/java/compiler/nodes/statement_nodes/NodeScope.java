package compiler.nodes.statement_nodes;

import java.util.ArrayList;

import compiler.Generator;

public class NodeScope implements NodeStatement {

    private ArrayList<NodeStatement> statements = new ArrayList<>();

    public NodeScope() {}

    @Override
    public String toString() {

        String buffer = new String("{\n");
        for (NodeStatement x: statements) {
            buffer = buffer.concat("\n    " + x.toString());
        }
        buffer = buffer.concat("\n}");
        return buffer;
    }

    public NodeScope(ArrayList<NodeStatement> statements) {
        this.statements = statements;
    }

    public void addStatement(NodeStatement statement) {
        statements.add(statement);
    }

    public ArrayList<NodeStatement> getStatements() {
        return this.statements;
    }

    public void operator(Generator generator) {
        generator.beginScope();
        generator.appendContents("    {\n");
        for (NodeStatement statement: statements) {
            statement.operator(generator);
        }
        generator.appendContents("    }\n");
        generator.endScope();
    }

}
