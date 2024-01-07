package compiler;

public class Error {

    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";
    
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
}
