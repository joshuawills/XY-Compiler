package compiler.nodes;

import compiler.Generator;
import compiler.Token;
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

    @Override
    public String toString() {
        return String.format("define %s (%s) -> %s %s", functionName, parameters.toString(), returnType.getType().toString().toLowerCase(), String.join("\n", statements.toString()));
    }

    public void operator(Generator generator) {

        generator.checkVariable(functionName);

        String returnValue = (returnType.getType() == null) ? "void" : returnType.getType().toString().toLowerCase();
        String funcDefinition = String.format("%s %s(%s)\n", returnValue, functionName, parameters.toString());
        generator.appendContents(funcDefinition);

        generator.beginScope();
        
        for (String parameter: this.parameters.getVariables().keySet()) {
            generator.addVariable(parameter, false);
        }

        generator.appendContents("    {\n");
        for (NodeStatement statement: statements.getStatements()) {
            statement.operator(generator);
        }
        generator.appendContents("    }\n");
        generator.endScope();

    }


}