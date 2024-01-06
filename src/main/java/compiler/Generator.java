package compiler;
import java.util.ArrayList;

import compiler.nodes.NodeProgram;
import compiler.nodes.statement_nodes.NodeStatement;

public class Generator {

    private NodeProgram program;
    private ArrayList<String> assemblyBuffer = new ArrayList<>();
    private int stackSize = 0; // 64 bit int = 1
    private ArrayList<Variable> variables = new ArrayList<>(); 
    private ArrayList<Integer> scopes = new ArrayList<>();   
    private Integer labelIncrementer = -1;

    public ArrayList<Variable> getVariables() {
        return this.variables;
    }

    public int getStackSize() {
        return this.stackSize;
    }

    public String createLabel() {
        labelIncrementer++;
        return "label" + labelIncrementer.toString();
    }


    public void addVariable(String name) {
        variables.add(new Variable(name, this.stackSize));
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

    public void beginScope() {
        this.scopes.add(this.stackSize);
    }

    public void endScope() {
        // Need to move stack pointer in assembly that many back
        Integer popCount = this.stackSize - this.scopes.get(scopes.size() - 1);
        appendContents("    add rsp, " + popCount * 8);
        this.stackSize -= popCount;
        for (Integer i = 0; i < popCount; i++)
            this.variables.remove(variables.size() - 1);
        this.scopes.remove(scopes.size() - 1);

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
