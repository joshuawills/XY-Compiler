package compiler;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import compiler.nodes.NodeFunction;
import compiler.nodes.NodeProgram;

public class Generator {

    private NodeProgram program;
    private ArrayList<String> assemblyBuffer = new ArrayList<>();
    private int stackSize = 0; // 64 bit int = 1
    private ArrayList<Variable> variables = new ArrayList<>(); 
    private ArrayList<Integer> scopes = new ArrayList<>();   

    public ArrayList<Variable> getVariables() {
        return this.variables;
    }

    public int getStackSize() {
        return this.stackSize;
    }

    public boolean constant(String var) {
        Variable variable = variables.stream().filter(v -> v.getName().equals(var)).collect(Collectors.toList()).get(0);
        return variable.isConstant();
    }

    public void addVariable(String name, boolean isConstant) {
        variables.add(new Variable(name, this.stackSize, isConstant));
        Integer currentSize = this.scopes.get(scopes.size() - 1);
        scopes.remove(scopes.size() - 1);
        scopes.add(currentSize + 1);
    }

    public void beginScope() {
        this.scopes.add(0);
    }

    public void endScope() {
        Integer finalSize = scopes.get(scopes.size() - 1);
        scopes.remove(scopes.size() - 1);
        for (int i = 0; i < finalSize; i++)
            variables.remove(variables.size() - 1);
    }

    public Generator(NodeProgram program) {
        this.program = program;
    } 

    public void appendContents(String contents) {
        assemblyBuffer.add(contents);
    }

    public String generateProgram() {

        NodeProgram program = this.program;

        this.appendContents("#include <stdio.h>\n\n");

        List<NodeFunction> nonMain = program.getNodeFunctions().stream().filter(f -> !f.getFunctionName().equals("main")).collect(Collectors.toList());
        for (NodeFunction function: nonMain)
            function.operator(this);

        NodeFunction mainFunction = program.getNodeFunctions().stream().filter(f -> f.getFunctionName().equals("main")).collect(Collectors.toList()).get(0);
        mainFunction.operator(this);

        return String.join("", assemblyBuffer);
    }


}
