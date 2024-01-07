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

    public String addString(String stringContents) {
        String label = this.createLabel(); 
        assemblyBuffer.add(5, "    " + label + " db " + stringContents + ", 0");
        return label;
    }

    public String generateProgram() {

        assemblyBuffer.add("default rel");
        assemblyBuffer.add("extern printf, exit");
        assemblyBuffer.add("section .rodata");
        assemblyBuffer.add("    formatInt db \"%#d\", 10, 0");
        assemblyBuffer.add("    formatStr db \"%s\", 10, 0");
        assemblyBuffer.add("section .text");
        assemblyBuffer.add("global main\n");
        assemblyBuffer.add("main:");



        // Generate Statements
        for (NodeStatement statement: this.program.getStatements())
            statement.operator(this);

        if (this.printMacro) {
            assemblyBuffer.add(0, "    digitSpacePos resb 8 ; enough space to store a register\n");
            assemblyBuffer.add(0, "    digitSpace resb 100 ; storing the string itself");
            assemblyBuffer.add(0, "section .bss");

        }

        assemblyBuffer.add("\n    ;; default exit\n    mov rax, 60");
        assemblyBuffer.add("    mov rdi, 0");
        assemblyBuffer.add("    syscall");
        return String.join("\n", assemblyBuffer);
    }


}
