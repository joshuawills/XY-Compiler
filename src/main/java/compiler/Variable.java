package compiler;

public class Variable {
    
    private String name;
    private int stackLocation;
    private boolean isConstant;

    public Variable(String name, int stackLocation, boolean isConstant) {
        this.name = name;
        this.stackLocation = stackLocation;
        this.isConstant = isConstant;
    }

    public String getName() { return this.name; }
    public int getStackLocation() { return this.stackLocation; }
    public boolean isConstant() { return this.isConstant; }
}
