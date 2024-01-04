import java.util.ArrayList;

public class Generator {
    
    public static String tokensToASM(ArrayList<Token> tokens) {

        ArrayList<String> output = new ArrayList<>();
        output.add("global _start");
        output.add("start:");

        for (int i = 0; i < tokens.size(); i++) {
            Token currentToken = tokens.get(i);
            if (currentToken.getType().equals(TokenType.RETURN)) {
                if (i + 1 < tokens.size() && tokens.get(i + 1).getType().equals(TokenType.INT_LIT)) {
                    if (i + 2 < tokens.size() && tokens.get(i + 2).getType().equals(TokenType.SEMI)) {
                        output.add("    mov rax, 60");
                        output.add("    mov rdi, " + tokens.get(i + 1).getValue());
                        output.add("    syscall");
                    }
                }
            }
        }

        return String.join("\n", output);
    }

}
