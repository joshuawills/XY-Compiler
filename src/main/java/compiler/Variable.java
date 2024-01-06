package compiler;

public class Variable {
    
    private String name;
    private int stackLocation;

    public Variable(String name, int stackLocation) {
        this.name = name;
        this.stackLocation = stackLocation;
    }

    public String getName() { return this.name; }
    public int getStackLocation() { return this.stackLocation; }
}
