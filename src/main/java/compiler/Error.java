package compiler;

public class Error {

    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLUE = "\u001b[34m";
    
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
}
