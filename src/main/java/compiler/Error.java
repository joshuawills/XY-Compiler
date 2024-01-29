package compiler;

public class Error
{

    private String fileContents;
    private String fileName;
    private Integer numLines;

    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLUE = "\u001b[34m";

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
        System.err.println(this.fileName + ":" + line + ":" + col + ":");
        for (Integer i = line - 2; i <= line + 2; i++) {
            if (i >= 1 && i <= this.numLines) {
                System.err.println(String.format("%5s | " + this.fileContents.split("\n")[i - 1], i));
            } 
        } 
        System.err.println("");
    }

    // Remove above
    public void invalidIdentName(String name, int line, int col) {
        System.err.println(ANSI_RED + "INVALID IDENTIFIER NAME" + ANSI_RESET);
        System.err.println(String.format("The name '%s' is a reserved keyword in XY. Please choose another identifier", name));
        logLines(line, col);
        System.exit(1);
    } 

    public void unknownPunctuation(String name, int line, int col) {
        System.err.println(ANSI_RED + "UNKNOWN PUNCTUATION" + ANSI_RESET);
        System.err.println(String.format("The punctuation '%s' is unrecognized by the XY compiler. Please refer to the formal docs", name));
        logLines(line, col);
        System.exit(1);
    }

    public void unknownOperator(TokenType operator, int line, int col) {
        System.err.println(ANSI_RED + "UNKNOWN OPERATOR" + ANSI_RESET);
        System.err.println(String.format("The operator '%s' is not an appropriate operator for an expression. Please refer to the formal docs", operator));
        logLines(line, col);
        System.exit(1);
    }

    public void funcCallInForLoopInit(int line, int col) {
        System.err.println(ANSI_RED + "NO FUNC-CALL IN FOR LOOP INITIALIZATION" + ANSI_RESET);
        logLines(line, col);
        System.exit(1);
    }

    public void arrayAccessInForLoopInit(int line, int col) {
        System.err.println(ANSI_RED + "NO ARRAY-ACCESS IN FOR LOOP INITIALIZATION" + ANSI_RESET);
        logLines(line, col);
        System.exit(1);
    }

    public void forLoopInit(int line, int col)  {
        System.err.println(ANSI_RED + "FOR LOOP'S INITIALIZER CAN ONLY BE A VARIABLE ASSIGNMENT/DECLARATION" + ANSI_RESET);
        logLines(line, col);
        System.exit(1);
    }

    public void receivedWrongToken(TokenType expected, TokenType received, int line, int col) {
        System.err.println(ANSI_RED + "RECEIVED INCORRECT TOKEN IN PARSER" + ANSI_RESET);
        System.err.println(String.format("The XY parser expected to receive a token '%s', but instead received '%s'", expected, received));
        logLines(line, col);
        System.exit(1);
    }

    public void scanArray(int line, int col) {
        System.err.println(ANSI_RED + "CAN'T SCAN IN AN ARRAY" + ANSI_RESET);
        logLines(line, col);
        System.exit(1);
    }

    public void NoTermParse(int line, int col) {
        System.err.println(ANSI_RED + "CAN'T PARSE TERM IN FUNC CALL" + ANSI_RESET);
        logLines(line, col);
        System.exit(1);
    }

    public void unexpectedTokenParameters(TokenType received, int line, int col) {
        System.err.println(ANSI_RED + "UNEXPECTED TOKEN WHEN PARSING PARAMETERS" + ANSI_RESET);
        System.err.println("Expected to receive COMMA or CLOSE_PAREN but received " + received);
        logLines(line, col);
        System.exit(1);
    }

    public void unnecessaryMutable(String name, int line, int col) {
        System.err.println(ANSI_BLUE + "UNNECESSARY MUTABLE DECLARATION" + ANSI_RESET);
        System.err.println(String.format("Variable '%s' is declared as mutable but never reassigned", name));        
        logLines(line, col);
    }

    public void unusedVariable(String name, int line, int col) {
        System.err.println(ANSI_BLUE + "UNUSED VARIABLE" + ANSI_RESET);
        System.err.println(String.format("Variable '%s' is declared but never used", name));        
        logLines(line, col);
    }


}
