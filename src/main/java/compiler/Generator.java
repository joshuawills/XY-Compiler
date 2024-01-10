package compiler;
import java.util.ArrayList;
import java.util.stream.Collectors;

import compiler.nodes.NodeProgram;
import compiler.nodes.statement_nodes.NodeStatement;

public class Generator {

    private NodeProgram program;
    private ArrayList<String> assemblyBuffer = new ArrayList<>();
    private int stackSize = 0; // 64 bit int = 1
    private ArrayList<Variable> variables = new ArrayList<>(); 
    private ArrayList<Integer> scopes = new ArrayList<>();   
    private Integer labelIncrementer = -1;
    private String endLabel = null;
    private Boolean printMacro = false;

    public void setMacro() {
        this.printMacro = true;
    }

    public ArrayList<Variable> getVariables() {
        return this.variables;
    }

    public void setEndLabel(String endLabel) {
        this.endLabel = endLabel;
    } 

    public String getEndLabel() {
        return this.endLabel;
    }

    public int getStackSize() {
        return this.stackSize;
    }

    public String createLabel() {
        labelIncrementer++;
        return "label" + labelIncrementer.toString();
    }

    public boolean constant(String var) {
        
        Variable variable =variables.stream().filter(v -> v.getName().equals(var)).collect(Collectors.toList()).get(0);
        return variable.isConstant();
    }

    public void addVariable(String name, boolean isConstant) {
        variables.add(new Variable(name, this.stackSize, isConstant));
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

    public String addString(String stringContents) {
        String label = this.createLabel(); 
        assemblyBuffer.add(1, "    " + label + "_len equ $ - " + label);
        assemblyBuffer.add(1, "    " + label + " db " + stringContents + ", 10");
        return label;
    }

    public String generateProgram() {

        assemblyBuffer.add("section .data");
        assemblyBuffer.add("section .text");
        assemblyBuffer.add("global _start\n");
        assemblyBuffer.add("_start:");

        // Generate Statements
        for (NodeStatement statement: this.program.getStatements())
            statement.operator(this);

        assemblyBuffer.add(0, "    digitSpacePos resb 8 ; enough space to store a register\n");
        assemblyBuffer.add(0, "    digitSpace resb 100 ; storing the string itself");
        assemblyBuffer.add(0, "section .bss");

        assemblyBuffer.add("\n    ;; default exit\n    mov rax, 60");
        assemblyBuffer.add("    mov rdi, 0");
        assemblyBuffer.add("    syscall");
        return String.join("\n", assemblyBuffer);
    }


}
