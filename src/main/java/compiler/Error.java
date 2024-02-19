package compiler;

public class Error
{

    private String fileContents;
    private String fileName;
    private Integer numLines;

    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLUE = "\u001b[34m";
    private static final String ANSI_YELLOW = "\u001B[33m";

    public Error(String fileContents, String fileName) {
        this.fileContents = fileContents;
        this.fileName = fileName;
        this.numLines = fileContents.split("\n").length;
    }
    
    public static void handleError(String type, String error, Integer exit_code) {
        System.err.println(ANSI_RED + type.toUpperCase() + " ERROR: " + ANSI_RESET + error);
        System.exit(exit_code);
    }

    public static void handleError(String type, String error) {
        System.err.println(ANSI_RED + type.toUpperCase() + " ERROR: " + ANSI_RESET + error);
        System.exit(1);
    }

    public static void handleError(String error) {
        System.err.println(ANSI_RED + " ERROR: " + ANSI_RESET + error);
        System.exit(1);
    }

    public static void minorError(String type, String error) {
        System.err.println(ANSI_BLUE + type.toUpperCase() + " ERROR: " + ANSI_RESET + error);
    }

    public static void minorError(String error) {
        System.err.println(ANSI_BLUE + " ERROR: " + ANSI_RESET + error);
        System.exit(1);
    }

    public void logLines(int line, int col) {
        System.err.println(ANSI_YELLOW + this.fileName + ":" + line + ":" + col + ANSI_RESET + ":");
        for (Integer i = line - 2; i <= line + 2; i++) {
            if (i >= 1 && i <= this.numLines) {
                System.err.println(String.format("%5s | " + this.fileContents.split("\n")[i - 1], i));
            } 
        } 
        System.err.println("");
    }

    // Remove above
    public void invalidIdentName(String name, int line, int col) {
        System.err.println(ANSI_RED + "error: invalid identifier name" + ANSI_RESET);
        System.err.println(String.format("The name '%s' is a reserved keyword in XY. Please choose another identifier", name));
        logLines(line, col);
        System.exit(1);
    } 

    public void unknownPunctuation(String name, int line, int col) {
        System.err.println(ANSI_RED + "error: unknown punctuation" + ANSI_RESET);
        System.err.println(String.format("The punctuation '%s' is not recognized by the XY compiler. Please refer to the formal docs", name));
        logLines(line, col);
        System.exit(1);
    }

    public void unknownOperator(TokenType operator, int line, int col) {
        System.err.println(ANSI_RED + "error: unknown operator" + ANSI_RESET);
        System.err.println(String.format("The operator '%s' is not an appropriate operator for an expression. Please refer to the formal docs", operator));
        logLines(line, col);
        System.exit(1);
    }

    public void funcCallInForLoopInit(int line, int col) {
        System.err.println(ANSI_RED + "error: no func-call in for loop initialization" + ANSI_RESET);
        logLines(line, col);
        System.exit(1);
    }

    public void arrayAccessInForLoopInit(int line, int col) {
        System.err.println(ANSI_RED + "error: no array-access in for loop initialization" + ANSI_RESET);
        logLines(line, col);
        System.exit(1);
    }

    public void forLoopInit(int line, int col)  {
        System.err.println(ANSI_RED + "error: for loops' initializer can only be a variable assignment/declaration" + ANSI_RESET);
        logLines(line, col);
        System.exit(1);
    }

    public void receivedWrongToken(TokenType expected, TokenType received, int line, int col) {
        System.err.println(ANSI_RED + "error: received incorrect token in parser" + ANSI_RESET);
        System.err.println(String.format("The XY parser expected to receive a token '%s', but instead received '%s'", expected, received));
        logLines(line, col);
        System.exit(1);
    }

    public void scanArray(int line, int col) {
        System.err.println(ANSI_RED + "error: can't scan in an array" + ANSI_RESET);
        logLines(line, col);
        System.exit(1);
    }

    public void NoTermParse(int line, int col) {
        System.err.println(ANSI_RED + "error: can't parse term in func call" + ANSI_RESET);
        logLines(line, col);
        System.exit(1);
    }

    public void unexpectedTokenParameters(TokenType received, int line, int col) {
        System.err.println(ANSI_RED + "error: unexpected token when parsing parameters" + ANSI_RESET);
        System.err.println("Expected to receive COMMA or CLOSE_PAREN but received " + received);
        logLines(line, col);
        System.exit(1);
    }

    public void undeclaredVariable(String name, int line, int col) {
        System.err.println(ANSI_RED + "error: accessing undeclared variable" + ANSI_RESET);
        System.err.println(String.format("Variable '%s' is used but has not been declared", name));        
        logLines(line, col);
        System.exit(1);
    }

    public void reassigningMutable(String name, int line, int col) {
        System.err.println(ANSI_RED + "error: re-assigning a constant variable" + ANSI_RESET);
        System.err.println(String.format("Variable '%s' is not declared as mutable so it's state may not be modified", name));        
        logLines(line, col);
        System.exit(1);
    }

    public void undeclaredFunction(String name, int line, int col) {
        System.err.println(ANSI_RED + "error: accessing undeclared function" + ANSI_RESET);
        System.err.println(String.format("Function '%s' is called but has not been declared", name));        
        logLines(line, col);
        System.exit(1);
    }

    public void wrongNumArgumentsFunction(String name, int expected, int received, int line, int col) {
        System.err.println(ANSI_RED + "error: args parsed wrong to function" + ANSI_RESET);
        System.err.println(String.format("Function '%s' requires %s arguments but received %s arguments", name, expected, received));        
        logLines(line, col);
        System.exit(1);
    }
    
    public void expectedMutable(int argNum, int line, int col) {
        System.err.println(ANSI_RED + "error: function parameter needs to be mutable" + ANSI_RESET);
        System.err.println(String.format("Expected arg %s to be mutable", argNum));        
        logLines(line, col);
        System.exit(1);
    }

    public void preExistingVariable(String name, int line, int col) {
        System.err.println(ANSI_RED + "error: defined variable already exists" + ANSI_RESET);
        System.err.println(String.format("Attempted declaration of variable '%s' that was previously defined in scope", name));        
        logLines(line, col);
        System.exit(1);
    }

    public void itKeyword(int line, int col) {
        System.err.println(ANSI_RED + "error: 'it' keyword may only be used in a loop scope" + ANSI_RESET);
        logLines(line, col);
        System.exit(1);
    }

    public void incompatibleReturnTypes(String fName, String expected, String received, int line, int col) {
        System.err.println(ANSI_RED + "error: incompatible return types" + ANSI_RESET);
        System.err.println(String.format("In '%s' function, '%s' return type is expected, but '%s' return type was received", fName, expected, received));
        logLines(line, col);
        System.exit(1);
    }

    public void unnecessaryMutable(String name, int line, int col) {
        System.err.println(ANSI_BLUE + "error: unnecessary mutable declaration" + ANSI_RESET);
        System.err.println(String.format("Variable '%s' is declared as mutable but never reassigned", name));        
        logLines(line, col);
    }

    public void unusedVariable(String name, int line, int col) {
        System.err.println(ANSI_BLUE + "error: unused variable" + ANSI_RESET);
        System.err.println(String.format("Variable '%s' is declared but never used", name));        
        logLines(line, col);
    }


}
