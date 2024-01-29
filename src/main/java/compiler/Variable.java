package compiler;

public class Variable {
    
    private final String name;
    private final Token type;
    private final boolean isMutable;
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
        return String.format("{%s, %s}", name, type.getType());
    }


}
