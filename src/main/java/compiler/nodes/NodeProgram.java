package compiler.nodes;

import java.util.ArrayList;


public class NodeProgram {
    
    private ArrayList<NodeFunction> functions = new ArrayList<>();

    public NodeProgram(ArrayList<NodeFunction> functions) {
        this.functions = functions;
    }

    public NodeProgram() {}

    public void appendFunction(NodeFunction f) {
        this.functions.add(f);
    }

    public ArrayList<NodeFunction> getNodeFunctions() {
        return this.functions;
    }
}
