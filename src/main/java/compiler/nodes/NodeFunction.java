package compiler.nodes;

import compiler.Generator;
import compiler.TokenType;
import compiler.nodes.statement_nodes.NodeScope;
import compiler.nodes.statement_nodes.NodeStatement;

public class NodeFunction {
    
    private String functionName = null;
    private NodeScope statements = null;
    private NodeParameters parameters = null;
    private TokenType returnType = null;

    public NodeFunction(NodeScope statements, String functionName, TokenType returnType, NodeParameters parameters) {
        this.statements = statements;
        this.functionName = functionName;
        this.returnType = returnType;
        this.parameters = parameters;
    }

    public NodeFunction() {}

    @Override
    public String toString() {
        return String.format("define %s (%s) -> %s\n%s\nenddefine\n", functionName, parameters.toString(), returnType.toString().toLowerCase(), statements.toString());
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

    public TokenType getReturnType() {
        return this.returnType;
    }

    public void operator(Generator generator) {
        generator.checkVariable(functionName);
        generator.setCurrentFunction(this);
        String returnValue = (returnType == null) ? "void" : returnType.toString().toLowerCase();
        String funcDefinition = String.format("%s %s(%s)\n", returnValue, functionName, parameters.toString());
        generator.appendContents(funcDefinition);
        generator.beginScope();
        for (String parameter: this.parameters.getVariables().keySet())
            generator.addVariable(parameter, false);
        generator.appendContents("    {\n");
        for (NodeStatement statement: statements.getStatements())
            statement.operator(generator);
        generator.appendContents("    }\n");
        generator.endScope();

    }


}
