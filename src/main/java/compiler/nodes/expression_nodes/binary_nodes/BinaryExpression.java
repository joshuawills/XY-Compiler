package compiler.nodes.expression_nodes.binary_nodes;

import compiler.Generator;
import compiler.Token;
import compiler.TokenType;
import compiler.Error;
import compiler.nodes.expression_nodes.NodeExpression;

public class BinaryExpression implements NodeExpression {
    
    private NodeExpression lhs = null;
    private NodeExpression rhs = null;
    private TokenType operator = null;

    public BinaryExpression(TokenType operator) {
        this.operator = operator;
    }

    public BinaryExpression() {}

    public BinaryExpression(NodeExpression one, NodeExpression two) {
        this.lhs = one;
        this.rhs = two;
    }

    public void setOperator(TokenType operator) {
        this.operator = operator;
    }

    public void setLHS(NodeExpression lhs) {
        this.lhs = lhs;
    }

    public void setRHS(NodeExpression rhs) {
        this.rhs = rhs;
    }

    public NodeExpression getLHS() {
        return this.lhs;
    }

    public NodeExpression getRHS() {
        return this.rhs;
    }

    public Token getToken() {
        return lhs.getToken();
    }

    @Override 
    public String toString() {
        switch (operator) {
            case PLUS:
                return String.format("%s + %s", getLHS().toString(), getRHS().toString());
            case DASH:
                return String.format("%s - %s", getLHS().toString(), getRHS().toString());
            case STAR:
                return String.format("%s * %s", getLHS().toString(), getRHS().toString());
            case F_SLASH:
                return String.format("%s / %s", getLHS().toString(), getRHS().toString());
            case GREATER_EQ:
                return String.format("%s >= %s", getLHS().toString(), getRHS().toString());
            case GREATER_THAN:
                return String.format("%s > %s", getLHS().toString(), getRHS().toString());
            case LESS_THAN:
                return String.format("%s < %s", getLHS().toString(), getRHS().toString());
            case LESS_EQ:
                return String.format("%s <= %s", getLHS().toString(), getRHS().toString());
            case EQUAL:
                return String.format("%s == %s", getLHS().toString(), getRHS().toString());
            case NOT_EQUAL:
                return String.format("%s != %s", getLHS().toString(), getRHS().toString());
            case AND_LOGIC:
                return String.format("%s && %s", getLHS().toString(), getRHS().toString());
            case OR_LOGIC:
                return String.format("%s || %s", getLHS().toString(), getRHS().toString());
            case PERCENT:
                return String.format("%s %% %s", getLHS().toString(), getRHS().toString());
            default:
                return "{}";
        }
    }

    public void operator(Generator generator) {
        this.getRHS().operator(generator);
        this.getLHS().operator(generator);
        generator.pop("rax");
        generator.pop("rbx");
        String labelOne = null;
        String labelTwo = null;
        switch (this.operator) {
            case PLUS:
                generator.appendContents("    add rax, rbx ;; " + this.toString());
                break;
            case DASH:
                generator.appendContents("    sub rax, rbx ;; " + this.toString()); 
                break;
            case STAR:
                generator.appendContents("    mul rbx ;; " + this.toString());
                break;
            case F_SLASH:
                generator.appendContents("    div rbx ;; " + this.toString()); 
                break;
            case AND_LOGIC:
                generator.appendContents("    and rax, rbx ;; " + this.toString()); 
                break;
            case OR_LOGIC:
                generator.appendContents("    or rax, rbx ;; " + this.toString()); 
                break;
            case PERCENT:
                generator.appendContents("    div rbx ;; " + this.toString());
                generator.appendContents("    mov rax, rdx");
                break;       
            case LESS_THAN:
                labelOne = generator.createLabel();
                labelTwo = generator.createLabel();
                generator.appendContents("    cmp rax, rbx");
                generator.appendContents("    jge " + labelOne);
                generator.appendContents("    mov eax, 1");
                generator.appendContents("    jmp " + labelTwo);
                generator.appendContents(labelOne + ": ");
                generator.appendContents("    mov eax, 0");
                generator.appendContents(labelTwo + ": ");
                break;
            case LESS_EQ:
                labelOne = generator.createLabel();
                labelTwo = generator.createLabel();
                generator.appendContents("    cmp rax, rbx");
                generator.appendContents("    jg " + labelOne);
                generator.appendContents("    mov eax, 1");
                generator.appendContents("    jmp " + labelTwo);
                generator.appendContents(labelOne + ": ");
                generator.appendContents("    mov eax, 0");
                generator.appendContents(labelTwo + ": ");
                break;
            case GREATER_THAN:
                labelOne = generator.createLabel();
                labelTwo = generator.createLabel();
                generator.appendContents("    cmp rax, rbx");
                generator.appendContents("    jle " + labelOne);
                generator.appendContents("    mov eax, 1");
                generator.appendContents("    jmp " + labelTwo);
                generator.appendContents(labelOne + ": ");
                generator.appendContents("    mov eax, 0");
                generator.appendContents(labelTwo + ": ");
                break;
            case GREATER_EQ:
                labelOne = generator.createLabel();
                labelTwo = generator.createLabel();
                generator.appendContents("    cmp rax, rbx");
                generator.appendContents("    jl " + labelOne);
                generator.appendContents("    mov eax, 1");
                generator.appendContents("    jmp " + labelTwo);
                generator.appendContents(labelOne + ": ");
                generator.appendContents("    mov eax, 0");
                generator.appendContents(labelTwo + ": ");
                break;
            case EQUAL:
                labelOne = generator.createLabel();
                labelTwo = generator.createLabel();
                generator.appendContents("    cmp rax, rbx");
                generator.appendContents("    jne " + labelOne);
                generator.appendContents("    mov eax, 1");
                generator.appendContents("    jmp " + labelTwo);
                generator.appendContents(labelOne + ": ");
                generator.appendContents("    mov eax, 0");
                generator.appendContents(labelTwo + ": ");
                break;
            case NOT_EQUAL:
                labelOne = generator.createLabel();
                labelTwo = generator.createLabel();
                generator.appendContents("    cmp rax, rbx");
                generator.appendContents("    je " + labelOne);
                generator.appendContents("    mov eax, 1");
                generator.appendContents("    jmp " + labelTwo);
                generator.appendContents(labelOne + ": ");
                generator.appendContents("    mov eax, 0");
                generator.appendContents(labelTwo + ": ");
                break;
            default:   
                Error.handleError("GENERATOR", "Unknown operator: " + this.toString());


        } 
        generator.push("rax");
    }


}
