import java.util.ArrayList;

public class Lexer {
    
    private String contents;
    private String buffer = "";
    private int iterator = 0;
    private ArrayList<Token> tokens = new ArrayList<>();


    public Lexer(String contents) {
        this.contents = contents;
    }

    public String getContents() {
        return this.contents;
    }

    public void flushBuffer() {
        this.buffer = "";
    }

    public void appendBuffer(Character c) {
        this.buffer = buffer.concat(c.toString());
    }

    public void appendToken(Token t) {
        this.tokens.add(t);
    }

    public static boolean isAlphaNumeric(Character c) {
        return Character.isDigit(c) || Character.isLetter(c);
    }

    public ArrayList<Token> tokenize() {
        while (this.iterator < contents.length()) {
            if (Character.isAlphabetic(peek())) {
                appendBuffer(peek());
                iterate();
                while (peek() != null && isAlphaNumeric(peek())) {
                    appendBuffer(peek()); 
                    iterate();
                }
                handleStr();
                flushBuffer();
            } else if (Character.isDigit(peek())) {
                appendBuffer(peek());
                iterate();
                while (peek() != null && Character.isDigit(peek())) {
                    appendBuffer(peek());
                    iterate();
                }
                appendToken(new Token(TokenType.INT_LIT, buffer));
                flushBuffer();
            } else if (peek().toString().equals(";")) {
                appendToken(new Token(TokenType.SEMI));
                iterate();
            } else if (Character.isWhitespace(peek())) {
                iterate();
            }
        }

        return tokens;
    }

    public void handleStr() {

        Token t = new Token();
        switch (this.buffer) {
            case "return":
                t.setType(TokenType.RETURN);
                break;
            default:
                System.err.println("Invalid string: " + this.buffer);
                System.exit(1);
        }

        appendToken(t);
    }

    public void iterate() {
        this.iterator++;
    }

    public void decrement() {
        this.iterator--;
    }

    public Character peek() {
        if (this.iterator >= contents.length())
            return null;

        return this.contents.charAt(this.iterator);
    }

}
