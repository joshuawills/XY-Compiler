package compiler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import compiler.nodes.NodeFunction;
import compiler.nodes.NodeProgram;

public class Generator {

    private NodeProgram program;
    private ArrayList<String> assemblyBuffer = new ArrayList<>();
    private int stackSize = 0; // 64 bit int = 1
    private ArrayList<Variable> variables = new ArrayList<>(); 
    private ArrayList<Integer> scopes = new ArrayList<>();   
    private HashMap<String, String> configSettings = new HashMap<>();
    
    /* 
    * Two considerations
    * 1: Variable Scope
    * 2: Just if the function's used should be easier
    */
    private ArrayList<String> temporarilyUnused = new ArrayList<>();
    private ArrayList<String> unusedVariables = new ArrayList<>();
    private ArrayList<String> functionCalled = new ArrayList<>();

    public void addFunctionCall(String functionName) {
        if (!functionCalled.contains(functionName))
            functionCalled.add(functionName);
    }

    public void setUsed(String varName) {
        temporarilyUnused.remove(varName);
    } 

    public ArrayList<Variable> getVariables() {
        return this.variables;
    }

    public int getStackSize() {
        return this.stackSize;
    }

    public void checkVariable(String variable) {
        if (this.configSettings.containsKey("SNAKE-CASE") && this.configSettings.get("SNAKE-CASE").equals("true")) {
            if (!Pattern.compile("^[a-zA-Z]+(_[a-zA-Z]+)*$").matcher(variable).find())
                Error.minorError("CONFIG-SPECIFIC", "Variable '" + variable + "' isn't snake_case");
        }

        if (this.configSettings.containsKey("CAMEL-CASE") && this.configSettings.get("CAMEL-CASE").equals("true")) {
            if (!Pattern.compile("^[a-z]+([A-Z][a-z]+)*$").matcher(variable).find())
                Error.minorError("CONFIG-SPECIFIC", "Variable '" + variable + "' isn't camelCase");
        }
    } 


    public boolean constant(String var) {
        Variable variable = variables.stream().filter(v -> v.getName().equals(var)).collect(Collectors.toList()).get(0);
        return variable.isConstant();
    }

    public void addVariable(String name, boolean isConstant) {
        Variable var = new Variable(name, this.stackSize, isConstant);
        temporarilyUnused.add(var.getName());
        variables.add(var);
        Integer currentSize = this.scopes.get(scopes.size() - 1);
        scopes.remove(scopes.size() - 1);
        scopes.add(currentSize + 1);
    }

    public void beginScope() {
        this.scopes.add(0);
    }

    public void endScope() {
        for (String unused: temporarilyUnused)
            unusedVariables.add(unused);
        temporarilyUnused.clear();
        Integer finalSize = scopes.get(scopes.size() - 1);
        scopes.remove(scopes.size() - 1);
        for (int i = 0; i < finalSize; i++)
            variables.remove(variables.size() - 1);
    }

    public Generator(NodeProgram program, HashMap<String, String> configSettings) {
        this.program = program;
        this.configSettings = configSettings;    
    } 

    public void appendContents(String contents) {
        assemblyBuffer.add(contents);
    }

    public String generateProgram() {

        NodeProgram program = this.program;

        this.appendContents("#include <stdio.h>\n\n");
        ArrayList<String> allFuncNames = new ArrayList<>();

        List<NodeFunction> nonMain = program.getNodeFunctions().stream().filter(f -> !f.getFunctionName().equals("main")).collect(Collectors.toList());
        for (NodeFunction function: nonMain) {
            allFuncNames.add(function.getFunctionName());
            function.operator(this);
        }

        NodeFunction mainFunction = program.getNodeFunctions().stream().filter(f -> f.getFunctionName().equals("main")).collect(Collectors.toList()).get(0);
        mainFunction.operator(this);

        if (!configSettings.containsKey("FLAG-UNUSED") ||configSettings.containsKey("FLAG-UNUSED") && configSettings.get("FLAG-UNUSED").equals("true")) {
            for (String funcName: allFuncNames) {
                if (!functionCalled.contains(funcName) && !funcName.startsWith("_"))
                    Error.minorError("CONFIG-SPECIFIC", "Unused function '" + funcName + "'.");
            }
            for (String varName: unusedVariables) {
                if (!varName.startsWith("_"))
                    Error.minorError("CONFIG-SPECIFIC", "Unused variable '" + varName + "'.");
            }
        }

        return String.join("", assemblyBuffer);
    }


}
