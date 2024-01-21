package compiler;

import compiler.nodes.NodeFunction;
import compiler.nodes.NodeProgram;
import compiler.nodes.expression_nodes.NodeExpression;
import compiler.nodes.expression_nodes.term_nodes.FuncCallNode;
import compiler.nodes.expression_nodes.term_nodes.IdentExpression;
import compiler.nodes.expression_nodes.term_nodes.NodeTerm;
import compiler.nodes.statement_nodes.NodeAssign;
import compiler.nodes.statement_nodes.NodeLet;
import compiler.nodes.statement_nodes.NodePrint;
import compiler.nodes.statement_nodes.NodeReturn;
import compiler.nodes.statement_nodes.NodeScan;
import compiler.nodes.statement_nodes.NodeScope;
import compiler.nodes.statement_nodes.NodeStatement;
import compiler.nodes.statement_nodes.conditionals.NodeIf;
import compiler.nodes.statement_nodes.conditionals.NodeIfPredicate;
import compiler.nodes.statement_nodes.conditionals.NodeIfPredicateElif;
import compiler.nodes.statement_nodes.conditionals.NodeIfPredicateElse;
import compiler.nodes.statement_nodes.loops.NodeDo;
import compiler.nodes.statement_nodes.loops.NodeLoop;
import compiler.nodes.statement_nodes.loops.NodeWhile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Verifier {
    
    private NodeProgram program;
    private final HashMap<String, String> configSettings;


    private HashMap<String, Integer> funcCallCounts = new HashMap<>();

    private ArrayList<Variable> variables = new ArrayList<>();
    private ArrayList<Integer> stack = new ArrayList<>();

    public Verifier(NodeProgram program, HashMap<String, String> configSettings) {
        this.program = program;
        this.configSettings = configSettings;
    }

    private void push() {
        stack.add(0);
    }

    private void addVariable(Variable v) {
        this.variables.add(v);
        Integer current = stack.get(stack.size() - 1);
        stack.remove(stack.size() - 1);
        stack.add(current + 1);
    }

    private void pop() {
        Integer size = stack.get(stack.size() - 1);
        stack.remove(stack.size() -1);

        ArrayList<Variable> popped = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            popped.add(variables.get(variables.size() - 1));
            variables.remove(variables.size() - 1);
        }

        for (Variable v: popped) {
            if (v.isMutable() && !v.isReassigned())
                Error.minorError("VERIFIER", "Variable '" + v.getName() + "' is declared as mutable but never reassigned.");
            if (!v.isUsed())
                Error.minorError("VERIFIER", "Variable '" + v.getName() + "' is defined but never used.");
        }

    }

    private boolean varExists(String n) {
        return variables.stream().anyMatch(v -> v.getName().equals(n));
    }

    private String variableReturnType(String n) {
        if (!varExists(n)) return null;
        return mapReturnTypes(variables.stream().filter(v -> v.getName().equals(n)).collect(Collectors.toList()).get(0).getType().getValue());
    }

    public Variable getVariable(String n) {
        if (!varExists(n)) return null;
        return variables.stream().filter(v -> v.getName().equals(n)).collect(Collectors.toList()).get(0);
    }

    public String getFunctionReturnType(String name) {
        List<NodeFunction> functions =  this.program.getNodeFunctions().stream().filter(f -> f.getFunctionName().equals(name)).collect(Collectors.toList());

        if (functions.size() != 1) { return null; }
        return functions.get(0).getReturnType().getValue(); // int, s32, string, void

    }

    private NodeFunction getFunction(String name) {
        List<NodeFunction> functions =  this.program.getNodeFunctions().stream().filter(f -> f.getFunctionName().equals(name)).collect(Collectors.toList());

        if (functions.size() != 1) { return null; }
        return functions.get(0); // int, s32, string, void

    }

    public String mapReturnTypes(String s) {
        switch (s) {
            case "int":
            case "s32":
                return "numeric";
            case "string":
                return "string";
            case "void":
                return "void";
        }
        return "";
    }

    // "numeric", "string", "void"
    private String getExpressionType(NodeExpression expression) {
        String x = expression.getType(this);
        if (expression instanceof FuncCallNode) {
            FuncCallNode expression1 = (FuncCallNode) expression;
            String returnType = verifyFunctionCall(expression1);
            return mapReturnTypes(returnType);
        } else if (expression instanceof IdentExpression) {
            IdentExpression expression2 = (IdentExpression) expression;
            String name = expression2.getToken().getValue();
            if (!varExists(name))
                Error.handleError("VERIFIER", "Reference to undeclared variable: " + name);
            return variableReturnType(name);
        }
        return x;
    }

    // Returns return thing of function
    private String verifyFunctionCall(FuncCallNode func) {
         
        // Need to check that all the CL args map up correctly
        String funcName = func.getFunctionName();
        String returnType = getFunctionReturnType(funcName);
        if (returnType == null)
            Error.handleError("VERIFIER", "Call to undeclared function: " + funcName);
        
        ArrayList<NodeTerm> parametersProvided = func.getParameters();
        LinkedHashMap<String, Token> realParameters = getFunction(funcName).getParameters().getVariables();

        if (parametersProvided.size() != realParameters.size())
            Error.handleError("VERIFIER", String.format("Provided %s arguments to %s function that requires %s arguments", parametersProvided.size(), funcName, realParameters.size()));

        Integer i = 0;
        for (Map.Entry<String, Token> entry : realParameters.entrySet()) {
            String realType = mapReturnTypes(entry.getValue().getValue());
            String providedType = getExpressionType(parametersProvided.get(0));
            if (!realType.equals(providedType))
                Error.handleError("VERIFIER", String.format("Expected arg %s to be of type %s, but received %s", (i + 1), realType, providedType));
            i++;
        }

        for (NodeTerm x: parametersProvided)
            x.getType(this);
        return returnType;
    }

    private void typeChecker() {

        for (NodeFunction f: program.getNodeFunctions()) {

            push();
            String fName = f.getFunctionName();
            Set<String> keys = f.getParameters().getVariables().keySet();
            Token returnT = f.getReturnType();
            for (String p: keys)
                addVariable(new Variable(p, false, f.getParameters().getVariables().get(p)));

            for (NodeStatement s: f.getStatements().getStatements()) {
               scanStatement(s, returnT, fName);
            }
            pop();
        }
    }
        
    private void scanStatement(NodeStatement s, Token returnT, String fName) {

        if (s instanceof NodeReturn) {

            NodeReturn s1 = (NodeReturn) s;
            String type = "void";
            if (s1.getExpression() != null)
                type = getExpressionType(s1.getExpression());

            String returnType = "void";
            if (returnT.getValue() != null)
                returnType = mapReturnTypes(returnT.getValue());

            if (!type.equals(returnType)) {
                Error.handleError("VERIFIER", String.format("Incompatible return types in '%s' function. Expected %s, received %s", fName, returnType, type));
            }

        } else if (s instanceof FuncCallNode) {
            
            verifyFunctionCall((FuncCallNode) s);
        
        } else if (s instanceof NodeAssign) {

            NodeAssign s1 = (NodeAssign) s;
            String name = s1.getIdentifier().getValue();
            if (!varExists(name))
                Error.handleError("VERIFIER", "Attempted reassignment to undeclared variable: " +  name);
            Variable currentVar = getVariable(name);
            if (!currentVar.isMutable())
                Error.handleError("VERIFIER", "Attempted reassignment to a constant variable: " + name);
            currentVar.setReassigned();
            String existingType = variableReturnType(name);
            String assignedType = getExpressionType(s1.getExpression());
            if (!existingType.equals(assignedType))
        
                Error.handleError("VERIFIER", String.format("Incompatible types, assigning %s to variable %s when it's %s", assignedType, name, existingType));
        } else if (s instanceof NodeLet) {

            NodeLet s1 = (NodeLet) s;
            String name = s1.getIdentifier().getValue();
            checkVariable(name);

            if (varExists(name))
                Error.handleError("VERIFIER", "Attempted reassignment to previously declared identifier: " + name);

            String expectedType = mapReturnTypes(s1.getType().getValue());
            String realType = getExpressionType(s1.getExpression());
            if (!expectedType.equals(realType))
                Error.handleError("VERIFIER", String.format("Attempting to assign expression of type %s to variable %s of type %s", realType, name, expectedType));
            addVariable(new Variable(name, !s1.isConstant(), s1.getType()));

        } else if (s instanceof NodePrint) {

            NodePrint s1 = (NodePrint) s;
            s1.setReturnType(getExpressionType(s1.getTerm()));
            if (s1.getTerm().getType(this).equals("void"))
                Error.handleError("VERIFIER", "'out' method can only log types that are numeric or strings, not void");

        } else if (s instanceof NodeScan) {

            if (((NodeScan) s).getType().getValue().equals("void"))
                Error.handleError("VERIFIER", "'in' method can only scan types that are numeric or strings, not void");
            NodeScan s1 = (NodeScan) s;
            addVariable(new Variable(s1.getIdentifier().getValue(), !s1.isConstant(), s1.getType()));

        } else if (s instanceof NodeScope) {
            push();
            for (NodeStatement s1: ((NodeScope) s).getStatements())
                scanStatement(s1, returnT, fName);
            pop();
            
        } else if (s instanceof NodeIf) {
            NodeIf s1 = (NodeIf) s;
            if (!getExpressionType(s1.getExpression()).equals("numeric"))
                Error.handleError("VERIFIER", "An 'if' condition can only evaluate a numeric expression");
        
            push();
            for (NodeStatement s2: ((NodeScope) s1.getScope()).getStatements())
                scanStatement(s2, returnT, fName);
            pop();

            if (s1.getPredicate() == null) return;
            NodeIfPredicate s2 = s1.getPredicate();
            while (true) {

                if (s2 instanceof NodeIfPredicateElif) {
                    NodeIfPredicateElif elif = (NodeIfPredicateElif) s2;
                    if (!getExpressionType(elif.getExpression()).equals("numeric"))
                        Error.handleError("VERIFIER", "An 'else if' condition can only evaluate a numeric expression");
                    push();
                    for (NodeStatement s3: ((NodeScope) elif.getScope()).getStatements())
                        scanStatement(s3, returnT, fName);
                    pop();

                    if (elif.getPredicate() == null) return;
                } else if (s2 instanceof NodeIfPredicateElse) {
                    NodeIfPredicateElse elseWord = (NodeIfPredicateElse) s2;
                    push();
                    for (NodeStatement s3: ((NodeScope) elseWord.getScope()).getStatements())
                        scanStatement(s3, returnT, fName);
                    pop();
                    return;
                }

                s2 = ((NodeIfPredicateElif) s2).getPredicate();
            }

        } else if (s instanceof NodeDo) {
            
            NodeDo s1 = (NodeDo) s;

            push();
            for (NodeStatement s3: ((NodeScope) s1.getScope()).getStatements())
                scanStatement(s3, returnT, fName);
            pop();

            if (!getExpressionType(s1.getExpression()).equals("numeric"))
                Error.handleError("VERIFIER", "A 'do-while' condition can only evaluate a numeric expression");

        } else if (s instanceof NodeWhile) {

            NodeWhile s1 = (NodeWhile) s;
            if (!getExpressionType(s1.getExpression()).equals("numeric"))
                Error.handleError("VERIFIER", "A 'while' condition can only evaluate a numeric expression");

            push();
            for (NodeStatement s3: ((NodeScope) s1.getScope()).getStatements())
                scanStatement(s3, returnT, fName);
            pop();

        } else if (s instanceof NodeLoop) {
            NodeLoop s1 = (NodeLoop) s;
            push();
            for (NodeStatement s3: ((NodeScope) s1.getScope()).getStatements())
                scanStatement(s3, returnT, fName);
            pop();
        }

    }

    private void checkOneMain() {
        Integer c = program.getNodeFunctions().stream().filter(f -> f.getFunctionName().equals("main")).collect(Collectors.toList()).size();
        if (c == 0)
            Error.handleError("VERIFIER", "A main function must be specified");
         else if (c > 1)
            Error.handleError("VERIFIER", "Only one main function must be specified, you have " + c);
    }

    private void checkDuplicateFunctions() {

        for (NodeFunction function: this.program.getNodeFunctions()) {
            String funcName = function.getFunctionName();
            if (funcCallCounts.keySet().contains(funcName)) {
                Integer oldVal = funcCallCounts.get(funcName);
                funcCallCounts.put(funcName, oldVal + 1);
            } else {
                funcCallCounts.put(funcName, 1);
            }
        }

        for (String name: funcCallCounts.keySet()) {
            if (funcCallCounts.get(name) > 1)
                Error.handleError("VERIFIER", "You have multiple functions with the name: " + name);
        }

    }

    public void checkVariable(String variable) {
        if (this.configSettings.containsKey("SNAKE-CASE") && this.configSettings.get("SNAKE-CASE").equals("true")) {
            if (!Pattern.compile("^[a-z]+(_[a-z]+)*$").matcher(variable).find())
                Error.minorError("CONFIG-SPECIFIC", "Variable '" + variable + "' isn't snake_case");
        }

        if (this.configSettings.containsKey("CAMEL-CASE") && this.configSettings.get("CAMEL-CASE").equals("true")) {
            if (!Pattern.compile("^[a-z]+([A-Z][a-z]+)*$").matcher(variable).find())
                Error.minorError("CONFIG-SPECIFIC", "Variable '" + variable + "' isn't camelCase");
        }
    } 

    public void verify() {
        checkOneMain();
        checkDuplicateFunctions();
        typeChecker();
    }

}
