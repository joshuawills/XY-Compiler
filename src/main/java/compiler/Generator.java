package compiler;
import java.util.ArrayList;
import java.util.HashMap;

import compiler.nodes.NodeProgram;
import compiler.nodes.NodeStatement;

public class Generator {

    private NodeProgram program;
    private ArrayList<String> assemblyBuffer = new ArrayList<>();
    private int stackSize = 0; // 64 bit int = 1
    private HashMap<String, Integer> variables = new HashMap<>(); // Integer is stack location, String is variable

    public HashMap<String, Integer> getVariables() {
        return this.variables;
    }

    public int getStackSize() {
        return this.stackSize;
    }

    public void addVariable(String name) {
        variables.put(name, this.stackSize);
    }

    public Generator(NodeProgram program) {
        this.program = program;
    } 

    public void appendContents(String contents) {
        assemblyBuffer.add(contents);
    }

    public void push(String register) {
        appendContents("    push " + register);
        this.stackSize++;
    }

    public void pop(String register) {
        appendContents("    pop " + register);
        this.stackSize--;
    }

    public String generateProgram() {

        assemblyBuffer.add("global _start\n_start:");

        // Generate Statements
        for (NodeStatement statement: this.program.getStatements())
            statement.operator(this);

        assemblyBuffer.add("\n    ;; default exit\n    mov rax, 60");
        assemblyBuffer.add("    mov rdi, 0");
        assemblyBuffer.add("    syscall");
        return String.join("\n", assemblyBuffer);
    }


}
