package compiler;

import compiler.nodes.NodeFunction;
import compiler.nodes.NodeProgram;
import compiler.nodes.expression_nodes.NodeExpression;
import compiler.nodes.expression_nodes.term_nodes.ArrayAccess;
import compiler.nodes.expression_nodes.term_nodes.FuncCallNode;
import compiler.nodes.expression_nodes.term_nodes.IdentExpression;
import compiler.nodes.expression_nodes.term_nodes.ItExpression;
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
import compiler.nodes.statement_nodes.loops.NodeBreak;
import compiler.nodes.statement_nodes.loops.NodeContinue;
import compiler.nodes.statement_nodes.loops.NodeDo;
import compiler.nodes.statement_nodes.loops.NodeFor;
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
    private int loopDepth = 0;
    private int ITcount = 0;
    private Error handler;

    private ArrayList<Variable> variables = new ArrayList<>();
    private ArrayList<Integer> stack = new ArrayList<>();

    public Verifier(NodeProgram program, HashMap<String, String> configSettings, Error handler) {
        this.program = program;
        this.configSettings = configSettings;
        this.handler = handler;
    }

    public int getITCount() {
        return this.ITcount;
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
                handler.unnecessaryMutable(v.getName(), v.getLine(), v.getCol());
            if (!v.isUsed())
                handler.unusedVariable(v.getName(), v.getLine(), v.getCol());
        }

    }

    private boolean varExists(String n) {
        return variables.stream().anyMatch(v -> v.getName().equals(n));
    }

     private boolean isMutable(String n) {
        return variables.stream().filter(v -> v.getName().equals(n)).collect(Collectors.toList()).get(0).isMutable();
    }

    private String variableReturnType(String n) {
        if (!varExists(n)) return null;
        return mapReturnTypes(variables.stream().filter(v -> v.getName().equals(n)).collect(Collectors.toList()).get(0).getType());
    }

    public Variable getVariable(String n) {
        if (!varExists(n)) return null;
        return variables.stream().filter(v -> v.getName().equals(n)).collect(Collectors.toList()).get(0);
    }

    public Token getFunctionReturnType(String name) {
        List<NodeFunction> functions =  this.program.getNodeFunctions().stream().filter(f -> f.getFunctionName().equals(name)).collect(Collectors.toList());
        if (functions.size() != 1) { return null; }
        return functions.get(0).getReturnType(); // int, s32, string, void

    }

    private NodeFunction getFunction(String name) {
        List<NodeFunction> functions =  this.program.getNodeFunctions().stream().filter(f -> f.getFunctionName().equals(name)).collect(Collectors.toList());
        if (functions.size() != 1) { return null; }
        return functions.get(0); // int, s32, string, void

    }

    public String mapReturnTypes(Token s) {
        String buffer = "";
        if (s.getType().equals(TokenType.ARR)) 
            buffer = buffer.concat("array|");

        if (s.getType().equals(TokenType.VOID)) {
            if (buffer.equals("array|"))
                    Error.handleError("VERIFIER", "Can't construct an array<void>");
            return "void";
        }

        switch (s.getValue()) {
            case "int":
            case "bool":
            case "it":
                return buffer.concat("numeric");
            case "str":
                return buffer.concat("str");
            case "char":
                return buffer.concat("char");
            case "void":
                if (buffer.equals("array|"))
                    Error.handleError("VERIFIER", "Can't construct an array<void>");
                return "void";
        }
        return "";
    }

    private String getExpressionType(NodeExpression expression) {
        String x = expression.getType(this, handler);
        if (expression instanceof FuncCallNode) {
            FuncCallNode expression1 = (FuncCallNode) expression;
            Token returnType = verifyFunctionCall(expression1);
            return mapReturnTypes(returnType);
        } else if (expression instanceof IdentExpression) {
            IdentExpression expression2 = (IdentExpression) expression;
            String name = expression2.getToken().getValue();
            if (!varExists(name))
                handler.undeclaredVariable(name, expression2.getToken().getLine(), expression2.getToken().getCol());
            return variableReturnType(name);
        } else if (expression instanceof ArrayAccess) {
            x = x.split("\\|")[1];
        }
        return x;
    }

    private Token verifyFunctionCall(FuncCallNode func) {
         
        Token identifier = func.getIdentifier();
        String funcName = identifier.getValue();
        Token returnType = getFunctionReturnType(funcName);
        if (returnType == null)
            handler.undeclaredFunction(funcName, identifier.getLine(), identifier.getCol());
        
        ArrayList<NodeTerm> parametersProvided = func.getParameters();
        LinkedHashMap<String, Token> realParameters = getFunction(funcName).getParameters().getVariables();
        if (parametersProvided.size() != realParameters.size())
            handler.wrongNumArgumentsFunction(funcName, realParameters.size(), parametersProvided.size(), identifier.getLine(), identifier.getCol());

        Integer i = 0;
        for (Map.Entry<String, Token> entry : realParameters.entrySet()) {
            String realType = mapReturnTypes(entry.getValue());
            Boolean isRealMutable = getFunction(funcName).getParameters().isMutable(i);
            
            if (parametersProvided.get(i) instanceof IdentExpression) {
                Token x = ((IdentExpression) parametersProvided.get(i)).getToken();
                String varName = x.getValue();
                Boolean isProvidedMutable = isMutable(varName);
                if (isRealMutable && !isProvidedMutable)
                    handler.expectedMutable((i + 1), x.getLine(), x.getCol());

            } else {
                String providedType = getExpressionType(parametersProvided.get(i));
                if (providedType.equals("it") && ITcount <= 0)
                    handler.itKeyword(parametersProvided.get(i).getToken().getLine(), parametersProvided.get(i).getToken().getCol());
                if (providedType.equals("it"))  {
                    ItExpression x = (ItExpression) parametersProvided.get(i);
                    x.setDepth(ITcount);
                    providedType = "numeric";
                }
                if (!realType.equals(providedType))
                    Error.handleError("VERIFIER", String.format("Expected arg %s to be of type %s, but received %s", (i + 1), realType, providedType));
            }

            String providedType = getExpressionType(parametersProvided.get(i));
            if (providedType.equals("it") && ITcount <= 0)
                handler.itKeyword(parametersProvided.get(i).getToken().getLine(), parametersProvided.get(i).getToken().getCol());
            if (providedType.equals("it")) {
                ItExpression x = (ItExpression) parametersProvided.get(i);
                x.setDepth(ITcount);
                providedType = "numeric";  
            } 
            if (!realType.equals(providedType))
                Error.handleError("VERIFIER", String.format("Expected arg %s to be of type %s, but received %s", (i + 1), realType, providedType));
            i++;
        }

        for (NodeTerm x: parametersProvided)
            x.getType(this, handler);
        return returnType;
    }

    private void typeChecker() {

        for (NodeFunction f: program.getNodeFunctions()) {

            push();
            String fName = f.getFunctionName();
            Set<String> keys = f.getParameters().getVariables().keySet();
            Token returnT = f.getReturnType();
            int i = 0;
            for (String p: keys) {
                Token token = f.getParameters().getVariables().get(p);
                addVariable(new Variable(p, f.getParameters().isMutable(i), token, token.getLine(), token.getCol()));
                i++;
            }

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
                returnType = mapReturnTypes(returnT);

            if (!type.equals(returnType))
                handler.incompatibleReturnTypes(fName, returnType, type, s1.getLine(), s1.getCol());

        } else if (s instanceof FuncCallNode) {
            
            verifyFunctionCall((FuncCallNode) s);
        
        } else if (s instanceof NodeAssign) {

            NodeAssign s1 = (NodeAssign) s;
            String name = s1.getIdentifier().convert();
            boolean access = (s1.getIdentifier() instanceof ArrayAccess);
            if (access)
                name = name.split("\\[")[0];
            if (!varExists(name))
                handler.undeclaredVariable(name, s1.getLine(), s1.getCol());
            Variable currentVar = getVariable(name);
            if (!currentVar.isMutable())
                handler.reassigningMutable(name, s1.getLine(), s1.getCol());
            currentVar.setReassigned();
            String existingType = (access) ? variableReturnType(name).split("\\|")[1]: variableReturnType(name);
            String assignedType = getExpressionType(s1.getExpression());

            if (assignedType.equals("it") && ITcount <= 0)
                handler.itKeyword(s1.getLine(), s1.getCol());
            if (assignedType.equals("it")) {
                ItExpression x = (ItExpression) s1.getExpression();
                x.setDepth(ITcount);
                assignedType = "numeric";
            }
            if (!(existingType.equals("char") && assignedType.equals("numeric")) || (existingType.equals("numeric") && assignedType.equals("char"))) {
                if (!existingType.equals(assignedType))
                    Error.handleError("VERIFIER", String.format("Incompatible types, assigning %s to variable %s when it's %s", assignedType, name, existingType));
            }
        
            } else if (s instanceof NodeLet) {

                NodeLet s1 = (NodeLet) s;
                String name = s1.getIdentifier().getValue();
                checkVariable(name);

                if (varExists(name))
                    handler.preExistingVariable(name, s1.getIdentifier().getLine(), s1.getIdentifier().getCol());

                String expectedType = mapReturnTypes(s1.getType());
                String realType = getExpressionType(s1.getExpression());

                if (realType.equals("it") && ITcount <= 0)
                    handler.itKeyword(s1.getIdentifier().getLine(), s1.getIdentifier().getCol());
                if (realType.equals("it")) {
                    ItExpression x = (ItExpression) s1.getExpression();
                    x.setDepth(ITcount);
                    realType = "numeric";
                }

                if (!realType.endsWith("any") && !expectedType.equals(realType)) // real type is to do
                    Error.handleError("VERIFIER", String.format("Attempting to assign expression of type %s to variable %s of type %s", realType, name, expectedType));
                Token identifier = s1.getIdentifier();
                addVariable(new Variable(name, !s1.isConstant(), s1.getType(), identifier.getLine(), identifier.getCol()));

        } else if (s instanceof NodePrint) {

            NodePrint s1 = (NodePrint) s;

            if (getExpressionType(s1.getTerm()).equals("it")) {
                ItExpression x = (ItExpression) s1.getTerm();
                x.setDepth(ITcount);

                if (ITcount <= 0)
                    handler.itKeyword(x.getToken().getLine(), x.getToken().getCol());
            }

            s1.setReturnType(getExpressionType(s1.getTerm()));
            if (s1.getTerm().getType(this, handler).equals("void"))
                Error.handleError("VERIFIER", "'out' method can only log types that are numeric or strings, not void");

        } else if (s instanceof NodeScan) {

            if (((NodeScan) s).getType().getValue().equals("void"))
                Error.handleError("VERIFIER", "'in' method can only scan types that are numeric or strings, not void");
            NodeScan s1 = (NodeScan) s;
            Token identifier = s1.getIdentifier();
            addVariable(new Variable(identifier.getValue(), !s1.isConstant(), s1.getType(), identifier.getLine(), identifier.getCol()));

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
            this.loopDepth++;
            for (NodeStatement s3: ((NodeScope) s1.getScope()).getStatements())
                scanStatement(s3, returnT, fName);
            this.loopDepth--;
            pop();

            if (!getExpressionType(s1.getExpression()).equals("numeric"))
                Error.handleError("VERIFIER", "A 'do-while' condition can only evaluate a numeric expression");

        } else if (s instanceof NodeWhile) {

            NodeWhile s1 = (NodeWhile) s;
            if (!getExpressionType(s1.getExpression()).equals("numeric"))
                Error.handleError("VERIFIER", "A 'while' condition can only evaluate a numeric expression");

            push();
            this.loopDepth++;
            for (NodeStatement s3: ((NodeScope) s1.getScope()).getStatements())
                scanStatement(s3, returnT, fName);
            this.loopDepth--;
            pop();

        } else if (s instanceof NodeFor) {
            NodeFor s1 = (NodeFor) s;
            push();
            this.loopDepth++;
            
            if (s1.getInitializer() != null)
                scanStatement(s1.getInitializer(), returnT, fName);
            if (s1.getCondition() != null) {
                if (!getExpressionType(s1.getCondition()).equals("numeric"))
                    Error.handleError("VERIFIER", "A 'for' loop can only evaluate a numeric expression");
            }
            if (s1.getIterator() != null)
                scanStatement(s1.getIterator(), returnT, fName);
            for (NodeStatement s3: ((NodeScope) s1.getScope()).getStatements())
                scanStatement(s3, returnT, fName);

            this.loopDepth--;
            pop();

        } else if (s instanceof NodeLoop) {
            NodeLoop s1 = (NodeLoop) s;

            if (s1.getCount() != null) {
                Integer realCount = Integer.parseInt(s1.getCount());
                if (realCount <= 0)
                    Error.handleError("VERIFIER", "Can't provide a non-natural number as a loop count specifier");
            }

            push();
            this.loopDepth++;
            this.ITcount++;
            s1.setDepth(ITcount);
            for (NodeStatement s3: ((NodeScope) s1.getScope()).getStatements())
                scanStatement(s3, returnT, fName);
            this.ITcount--;
            this.loopDepth--;
            pop();
        } else if (s instanceof NodeContinue) {

            if (this.loopDepth <= 0)
                Error.handleError("VERIFIER", "A 'continue' statement may only be used in a loop");

        } else if (s instanceof NodeBreak) {

            if (this.loopDepth <= 0)
                Error.handleError("VERIFIER", "A 'break' statement may only be used in a loop");

        }

    }

    private void checkOneMain() {
        Integer c = program.getNodeFunctions().stream().filter(f -> f.getFunctionName().equals("main")).collect(Collectors.toList()).size();
        if (c == 0)
            Error.handleError("VERIFIER", "A main function must be specified");
         else if (c > 1)
            Error.handleError("VERIFIER", "Only one main function must be specified, you have " + c);
    
        if (!getFunction("main").getReturnType().getValue().equals("int"))
            Error.handleError("VERIFIER", "Main function must return an int");

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
