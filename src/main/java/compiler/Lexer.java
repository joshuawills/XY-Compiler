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
    private void appendToken(TokenType t) { this.tokens.add(new Token(t)); consume(); }
    private boolean isNumber() { return (Character.isDigit(peek())) || (peek().toString().equals("-") && peek(1) != null && Character.isDigit(peek(1))); }

    public ArrayList<Token> tokenize() {
        while (this.iterator < this.contents.length()) {
        
            if (Character.isAlphabetic(peek())) {
                handleStr();
                continue;
            }

            if (isNumber()) {
                handleDigit();
                continue;
            }

            if (Character.isWhitespace(peek())) {
                consume();
                continue;
            }

            if (isCommentStandard()) {
                handleCommentsStandard();
                continue;
            }

            if (isCommentMulti()) {
                handleCommentsMulti();
                continue;
            }

            switch (peek().toString()) {
                case ";":
                    appendToken(TokenType.SEMI); break;
                case "=":
                    appendToken(TokenType.ASSIGN); break;
                case "+":
                    appendToken(TokenType.PLUS); break;
                case "*":
                    appendToken(TokenType.STAR); break;
                case "-":
                    appendToken(TokenType.DASH); break;
                case "/":
                    appendToken(TokenType.F_SLASH); break;
                case "(":
                    appendToken(TokenType.OPEN_PAREN); break;
                case ")":
                    appendToken(TokenType.CLOSE_PAREN); break;
                case "{":
                    appendToken(TokenType.OPEN_CURLY); break;
                case "}":
                    appendToken(TokenType.CLOSE_CURLY); break;
                default:
                    System.err.println("Unknown token: " + peek()); 
                    System.exit(1);
            }


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

    private void handleDigit() {
        
        appendBuffer(consume());

        if (peek().toString().equals("-")) appendBuffer(peek());

        while (peek() != null && Character.isDigit(peek()))
            appendBuffer(consume());
        this.tokens.add(new Token(TokenType.INT_LIT, buffer));
        flushBuffer();

    }

    private void handleStr() {

        appendBuffer(consume());
        while (peek() != null && isAlphaNumeric(peek()))
            appendBuffer(consume()); 

        switch (this.buffer) {
            case "return":
                appendToken(TokenType.RETURN); break;
            case "int":
            case "i32":
                appendToken(TokenType.INIT_INT); break;
            case "if":
                appendToken(TokenType.IF); break;
            case "elif":
                appendToken(TokenType.ELIF); break;
            case "else":
                appendToken(TokenType.ELSE); break;
            default:
                this.tokens.add(new Token(TokenType.IDENT, buffer));
                break;
        }
        flushBuffer();

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
