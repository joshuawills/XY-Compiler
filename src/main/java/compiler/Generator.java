package compiler;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import compiler.nodes.NodeFunction;
import compiler.nodes.NodeProgram;

public class Generator {

    private NodeProgram program;
    private ArrayList<String> assemblyBuffer = new ArrayList<>();
    private NodeFunction currentFunction = null;
    public void setCurrentFunction(NodeFunction function) { this.currentFunction = function; }
    public NodeFunction getCurrentFunction() { return this.currentFunction; }

    private ArrayList<String> allFunctionNames = new ArrayList<>();

    public NodeFunction getFunction(String name) {
        return program.getNodeFunctions().stream().filter(f -> f.getFunctionName().equals(name)).collect(Collectors.toList()).get(0);
    }

    public ArrayList<String> getFunctionNames() {
        return this.allFunctionNames;
    }

    public Generator(NodeProgram program) {
        this.program = program;
    } 

    public void appendContents(String contents) { assemblyBuffer.add(contents); }

    public String generateProgram() {
        NodeProgram program = this.program;
        this.appendContents("#include <stdio.h>\n\n");
        ArrayList<String> allFuncNames = new ArrayList<>();
        List<NodeFunction> nonMain = program.getNodeFunctions().stream().filter(f -> !f.getFunctionName().equals("main")).collect(Collectors.toList());
        
        // Noting down function names to identify inappropriate function calls
        for (NodeFunction function: nonMain)
            allFunctionNames.add(function.getFunctionName());
        
        // Generating non-main functions
        for (NodeFunction function: nonMain) {
            allFuncNames.add(function.getFunctionName());
            function.operator(this);
        }

        // Generating the main function
        NodeFunction mainFunction = program.getNodeFunctions().stream().filter(f -> f.getFunctionName().equals("main")).collect(Collectors.toList()).get(0);
        mainFunction.operator(this);

        return String.join("", assemblyBuffer);
    }


}
