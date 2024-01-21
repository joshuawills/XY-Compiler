package compiler;

public class Variable {
    
    private String name;
    private Token type;
    private boolean isMutable = false;
    private boolean isReassigned = false;
    private boolean isUsed = false;

    public Variable(String name, boolean isMutable, Token type) {
        this.name = name;
        this.isMutable = isMutable;
        this.type = type;
    }

    public String getName() { return this.name; }
    public Token getType() { return this.type; }
    public boolean isMutable() { return this.isMutable; }
    public boolean isReassigned() { return this.isReassigned; }
    public boolean isUsed() { return this.isUsed; }

    public void setReassigned() { this.isReassigned = true; }
    public void setUsed() { this.isUsed = true; }

    @Override
    public String toString() {
        String x = String.format("{%s, %s}", name, type.getType());
        return x;
    }


}
