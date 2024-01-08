package compiler.nodes.statement_nodes;

import compiler.Generator;
import compiler.nodes.expression_nodes.term_nodes.NodeTerm;

public class NodePrint implements NodeStatement {
    
    private NodeTerm term;
    private boolean isString = false;

    public NodePrint(NodeTerm term) {
        this.term = term;
    }

    public NodePrint(NodeTerm term, boolean isString) {
        this.term = term;
        this.isString = isString;
    }

    public NodeTerm getTerm() {
        return this.term;
    }

    @Override
    public String toString() {
        if (term == null)
            return "{}";

        return String.format("out %s", term.toString());
    }

    public void operator(Generator generator) {
        generator.setMacro();
        if (isString) {
            String label = generator.addString(getTerm().getToken().getValue());
            generator.appendContents("    mov rax, 1 ; write syscall");
            generator.appendContents("    mov rdi, 1");
            generator.appendContents("    mov rsi, " + label);
            generator.appendContents("    mov rdx, " + label + "_len");
            generator.appendContents("    syscall");
        } else {
            term.operator(generator);
            generator.pop("rax");
            generator.appendContents("    call _printRAX");
        }
    }

}
