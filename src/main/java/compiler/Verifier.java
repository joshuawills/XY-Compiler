package compiler;

import compiler.nodes.NodeFunction;
import compiler.nodes.NodeProgram;

import java.util.HashMap;
import java.util.stream.Collectors;

import compiler.Error;

public class Verifier {
    
    private NodeProgram program;

    public Verifier(NodeProgram program) {
        this.program = program;
    }

    private void checkOneMain() {
        Integer mainFuncCount = program.getNodeFunctions().stream().filter(f -> f.getFunctionName().equals("main")).collect(Collectors.toList()).size();
        if (mainFuncCount == 0)
            Error.handleError("VERIFIER", "A main function must be specified");
         else if (mainFuncCount > 1)
            Error.handleError("VERIFIER", "Only one main function must be specified, you have " + mainFuncCount);
    }

    private void checkDuplicateFunctions() {
        HashMap<String, Integer> funcCallCounts = new HashMap<>();

        for (NodeFunction function: this.program.getNodeFunctions()) {
            String funcName = function.getFunctionName();
            if (funcCallCounts.keySet().contains(funcName)) {
                Integer oldVal = funcCallCounts.get(funcName);
                funcCallCounts.put(funcName, oldVal + 1);
            } else {
                funcCallCounts.put(funcName, 1);
            }
        }

        for (String functionName: funcCallCounts.keySet()) {
            if (funcCallCounts.get(functionName) > 1)
                Error.handleError("VERIFIER", "You have multiple functions with the name: " + functionName);
        }

    }

    public void verify() {
        checkOneMain();
        checkDuplicateFunctions();
    }

}
