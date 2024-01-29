package compiler;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import compiler.nodes.NodeFunction;
import compiler.nodes.NodeProgram;

public class Generator {

    private final NodeProgram program;
    private final ArrayList<String> assemblyBuffer = new ArrayList<>();
    private NodeFunction currentFunction = null;
    public void setCurrentFunction(NodeFunction function) { this.currentFunction = function; }
    public NodeFunction getCurrentFunction() { return this.currentFunction; }

    public Generator(NodeProgram program) {
        this.program = program;
    } 

    public void removeSemicolon() {
        if (assemblyBuffer.size() == 0)
            return;
        String lastBuffer = assemblyBuffer.get(assemblyBuffer.size() - 1);
        if (lastBuffer.endsWith(";"))
            lastBuffer = lastBuffer.substring(0, lastBuffer.length() - 1);
        assemblyBuffer.remove(assemblyBuffer.size() - 1);
        assemblyBuffer.add(lastBuffer);
    }
 
    public void appendContents(String contents) { assemblyBuffer.add(contents); }

    public String generateProgram() {
        NodeProgram program = this.program;
        this.appendContents("#include <stdio.h>\n\n");
        List<NodeFunction> nonMain = program.getNodeFunctions().stream().filter(f -> !f.getFunctionName().equals("main")).collect(Collectors.toList());
        
        // Generating non-main functions
        for (NodeFunction function: nonMain)
            function.operator(this);

        // Generating the main function
        NodeFunction mainFunction = program.getNodeFunctions().stream().filter(f -> f.getFunctionName().equals("main")).collect(Collectors.toList()).get(0);
        mainFunction.operator(this);

        return String.join("", assemblyBuffer);
    }


}
