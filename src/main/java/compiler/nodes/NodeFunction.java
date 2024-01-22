package compiler.nodes;

import compiler.Generator;
import compiler.Token;
import compiler.TokenType;
import compiler.nodes.statement_nodes.NodeScope;
import compiler.nodes.statement_nodes.NodeStatement;

public class NodeFunction {
    
    private String functionName = null;
    private NodeScope statements = null;
    private NodeParameters parameters = null;
    private Token returnType = null;

    public NodeFunction(NodeScope statements, String functionName, Token returnType, NodeParameters parameters) {
        this.statements = statements;
        this.functionName = functionName;
        this.returnType = returnType;
        this.parameters = parameters;
    }

    public NodeFunction() {}

    @Override
    public String toString() {
        if (returnType.getType().equals(TokenType.VOID))
            return String.format("define %s (%s) -> void\n%s\nenddefine\n", functionName, parameters.toString(), statements.toString());
        return String.format("define %s (%s) -> %s\n%s\nenddefine\n", functionName, parameters.toString(), returnType.getValue().toString().toLowerCase(), statements.toString());
    }

    public void appendStatement(NodeStatement s) {
        this.statements.addStatement(s);
    }

    public NodeParameters getParameters() {
        return this.parameters;
    }

    public NodeScope getStatements() {
        return this.statements;
    }

    public String getFunctionName() {
        return this.functionName;
    }

    public Token getReturnType() {
        return this.returnType;
    }

    public void operator(Generator generator) {
        generator.setCurrentFunction(this);
        String returnValue = (returnType.getValue() == null) ? "void" : returnType.getValue().toString().toLowerCase();
        if (returnValue.equals("string"))
            returnValue = "char *";
        if (returnValue.equals("bool"))
            returnValue = "int";

        String funcDefinition = String.format("%s %s(%s)\n", returnValue, functionName, parameters.toString());
        generator.appendContents(funcDefinition);
        generator.appendContents("    {\n");
        for (NodeStatement statement: statements.getStatements())
            statement.operator(generator);
        generator.appendContents("    }\n");

    }


}
