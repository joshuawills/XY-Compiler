package compiler;
import java.util.ArrayList;

public class Lexer {
    
    private String contents;
    private String buffer = "";
    private int iterator = 0;
    private ArrayList<Token> tokens = new ArrayList<>();

    public Lexer(String contents) {
        this.contents = contents;
    }
    
    private static boolean isAlphaNumeric(Character c) { return Character.isDigit(c) || Character.isLetter(c); }
    private void flushBuffer() { this.buffer = ""; }
    private void appendBuffer(Character c) { this.buffer = buffer.concat(c.toString()); }
    private void appendToken(Token t) { this.tokens.add(t); }

    public ArrayList<Token> tokenize() {
        while (this.iterator < this.contents.length()) {
            if (Character.isAlphabetic(peek())) {

                appendBuffer(consume());
                while (peek() != null && isAlphaNumeric(peek()))
                    appendBuffer(consume()); 
                handleStr();

            } else if (Character.isDigit(peek())) {

                appendBuffer(consume());
                while (peek() != null && Character.isDigit(peek()))
                    appendBuffer(consume());
                appendToken(new Token(TokenType.INT_LIT, buffer));

            } else if (peek().toString().equals(";")) {

                appendToken(new Token(TokenType.SEMI));
                consume();
            
            } else if (peek().toString().equals("=")) { // handle double equals and stuff as well lol
            
                appendToken(new Token(TokenType.ASSIGN));
                consume();
            
            } else if (peek().toString().equals("+")) {

                appendToken(new Token(TokenType.ADD));
                consume();
                                
            }  else if (peek().toString().equals("*")) {

                appendToken(new Token(TokenType.TIMES));
                consume();

            } 
            else if (Character.isWhitespace(peek())) {
            
                consume();
            
            } else if (isCommentStandard()) {
                handleCommentsStandard();
            } else if (isCommentMulti()) {
                handleCommentsMulti();
            }
            flushBuffer();
        }
        return this.tokens;
    }

    private boolean isCommentStandard() {
        return peek().toString().equals("/") && peek(1) != null && peek(1).toString().equals("/");
    }

    private boolean isCommentMulti() {
        return peek().toString().equals("/") && peek(1) != null && peek(1).toString().equals("*");
    }

    private void handleCommentsMulti() {
        consume();
        consume();
        while (peek() != null && !peek().toString().equals("*") && peek(1) != null && !peek().toString().equals("/"))
            consume();
        consume();
        consume();
    }

    private void handleCommentsStandard() {
        while (peek() != null && !peek().toString().equals("\n"))
            consume();
        consume();
    }

    private void handleStr() {
        Token t = new Token();
        switch (this.buffer) {
            case "return":
                t.setType(TokenType.RETURN);
                break;
            case "int":
                t.setType(TokenType.INT_TYPE);
                break;
            default:
                t.setType(TokenType.STRING);
                t.setValue(buffer);
                break;
        }
        appendToken(t);
    }

    private Character consume() {
        Character current = null;
        if (this.iterator < contents.length())
            current = this.contents.charAt(this.iterator);
        this.iterator++;
        return current;
    }

    private Character peek(int over) {
         if (this.iterator + over >= contents.length())
            return null;
        return this.contents.charAt(this.iterator + over);
    }

    private Character peek() {
        if (this.iterator >= contents.length())
            return null;
        return this.contents.charAt(this.iterator);
    }

}
