package compiler;
import java.util.ArrayList;

public class Lexer {
    
    private String contents;
    private String buffer = "";
    private int position = 0;
    private ArrayList<Token> tokens = new ArrayList<>();

    private int line = 1; private int col = 1;

    private void resetCol() { this.col = 1; }
    private void incrementLine() { this.line++; resetCol(); }
    private void incrementCol() { this.col++; }

    public Lexer(String contents) {
        this.contents = contents;
    }
    
    private static boolean isAlphaNumeric(Character c) { return Character.isDigit(c) || Character.isLetter(c) || c.equals('_'); }
    private void flushBuffer() { this.buffer = ""; }
    private void appendBuffer(Character c) { this.buffer = buffer.concat(c.toString()); }
    private void appendToken(TokenType t) { this.tokens.add(new Token(t, line, col)); consume(); }
    private void appendTokenNoConsume(TokenType t, int real_line, int real_col) { this.tokens.add(new Token(t, real_line, real_col)); }

    private boolean isNumber() { return (Character.isDigit(peek())) || (peek().toString().equals("-") && peek(1) != null && Character.isDigit(peek(1))); }
    
    public ArrayList<Token> tokenize() {
        
        while (this.position < this.contents.length()) {

            if (Character.isAlphabetic(peek()) || peek().equals('_')) {
                handleStr();
                continue;
            }

            if (isNumber()) {
                handleDigit();
                continue;
            }

            if (peek().equals('"')) {
                handleString();
                continue;
            }

            if (peek().equals('\'')) {
                handleChar();
                continue;
            }

            if (peek().equals('\n')) {
                consume();
                this.incrementLine();
                this.col = 1;
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

            String value = this.peekAhead(2);
            if (value != null) {
                switch (value) {
                    case ">=":
                        appendTokenNoConsume(TokenType.GREATER_EQ, line, col); 
                        consume(); consume(); continue;
                    case "<=":
                        appendTokenNoConsume(TokenType.LESS_EQ, line, col); 
                        consume(); consume(); continue;
                    case "==":
                        appendTokenNoConsume(TokenType.EQUAL, line, col); 
                        consume(); consume(); continue;
                    case "!=":
                        appendTokenNoConsume(TokenType.NOT_EQUAL, line, col); 
                        consume(); consume(); continue;
                    case "&&":
                        appendTokenNoConsume(TokenType.AND_LOGIC, line, col); 
                        consume(); consume(); continue;
                    case "||":
                        appendTokenNoConsume(TokenType.OR_LOGIC, line, col); 
                        consume(); consume(); continue;
                    case "++":
                        appendTokenNoConsume(TokenType.INCREMENT, line, col); 
                        consume(); consume(); continue;
                    case "--":
                        appendTokenNoConsume(TokenType.DECREMENT, line, col); 
                        consume(); consume(); continue;
                    case "+=":
                        appendTokenNoConsume(TokenType.PLUS_EQUAL, line, col); 
                        consume(); consume(); continue;
                    case "-=":
                        appendTokenNoConsume(TokenType.DASH_EQUAL, line, col); 
                        consume(); consume(); continue;
                    case "*=":
                        appendTokenNoConsume(TokenType.STAR_EQUAL, line, col); 
                        consume(); consume(); continue;
                    case "/=":
                        appendTokenNoConsume(TokenType.F_SLASH_EQUAL, line, col);
                        consume(); consume(); continue;
                    case "->":
                        appendTokenNoConsume(TokenType.ARROW, line, col);
                        consume(); consume(); continue;
                    case "<<":
                        appendTokenNoConsume(TokenType.BITWISE_LEFT_SHIFT, line, col);
                        consume(); consume(); continue;
                    case ">>":
                        appendTokenNoConsume(TokenType.BITWISE_RIGHT_SHIFT, line, col);
                        consume(); consume(); continue;
                }
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
                case "[":
                    appendToken(TokenType.LEFT_SQUARE); break;
                case "]":
                    appendToken(TokenType.RIGHT_SQUARE); break;
                case "<":
                    appendToken(TokenType.LESS_THAN); break;
                case ">":
                    appendToken(TokenType.GREATER_THAN); break;
                case "!":
                    appendToken(TokenType.NEGATE); break;
                case ",":
                    appendToken(TokenType.COMMA); break;
                case "%":
                    appendToken(TokenType.PERCENT); break;
                case "&":
                    appendToken(TokenType.BITWISE_AND); break;
                case "|":
                    appendToken(TokenType.BITWISE_OR); break;
                case "^":
                    appendToken(TokenType.BITWISE_XOR); break;
                default:
                    Error.handleError("LEXER", "Unknown punctuation (" + peek() + ")\n    line: " + this.line + ", col: " + this.col);
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
        incrementLine();
        resetCol();
    }
    
    private void handleString() {
        int real_column = this.col;
        appendBuffer(consume());

        while (peek() != null && !peek().equals('"'))
            appendBuffer(consume());
        appendBuffer(consume());
        this.tokens.add(new Token(TokenType.STRING_LIT, buffer, line, real_column));
        flushBuffer();
    }

    private void handleChar() {
        int real_column = this.col;
        appendBuffer(consume());

        while (peek() != null && !peek().equals('\''))
            appendBuffer(consume());
        appendBuffer(consume());
        this.tokens.add(new Token(TokenType.CHAR_LIT, buffer, line, real_column));
        flushBuffer();
    }

    private void handleDigit() {
        int real_column = this.col;
        appendBuffer(consume());

        if (peek().toString().equals("-")) appendBuffer(peek());

        while (peek() != null && Character.isDigit(peek()))
            appendBuffer(consume());
        this.tokens.add(new Token(TokenType.INT_LIT, buffer, line, real_column));
        flushBuffer();

    }

    private void handleStr() {
        int real_column = this.col;
        appendBuffer(consume());
        while (peek() != null && isAlphaNumeric(peek()))
            appendBuffer(consume()); 

        if (this.buffer.equals("else")) {
            String potentialIf = peekAhead(3);
            if (potentialIf != null && potentialIf.equals(" if")) {
                this.buffer = this.buffer.concat(potentialIf);
                consume(); consume(); consume();
            }
        }

        switch (this.buffer) {
            case "return":
                appendTokenNoConsume(TokenType.RETURN, this.line, real_column); break;
            case "define":
                appendTokenNoConsume(TokenType.DEFINE, this.line, real_column); break;
            case "array":
                this.tokens.add(new Token(TokenType.ARRAY, this.line, real_column)); break;
            case "int":
            case "s32":
                this.tokens.add(new Token(TokenType.DECLARE, "int", line, real_column)); break;
            case "bool":
            case "string":
            case "char":
                this.tokens.add(new Token(TokenType.DECLARE, this.buffer, line, real_column)); break;
            case "if":
                appendTokenNoConsume(TokenType.IF, this.line, real_column); break;
            case "else if":
                appendTokenNoConsume(TokenType.ELIF, this.line, real_column); break;
            case "else":
                appendTokenNoConsume(TokenType.ELSE, this.line, real_column); break;
            case "while": 
                appendTokenNoConsume(TokenType.WHILE, this.line, real_column); break;
            case "mut": 
                appendTokenNoConsume(TokenType.MUT, this.line, real_column); break;
            case "out":
                appendTokenNoConsume(TokenType.OUT, this.line, real_column); break;
            case "in":
                appendTokenNoConsume(TokenType.IN, this.line, real_column); break;
            case "do":
                appendTokenNoConsume(TokenType.DO, this.line, real_column); break;
            case "continue":
                appendTokenNoConsume(TokenType.CONTINUE, this.line, real_column); break;
            case "break":
                appendTokenNoConsume(TokenType.BREAK, this.line, real_column); break;
            case "loop":
                appendTokenNoConsume(TokenType.LOOP, this.line, real_column); break;
            case "void":
                appendTokenNoConsume(TokenType.VOID, this.line, real_column); break;
            case "true":
                this.tokens.add(new Token(TokenType.INT_LIT, "1", line, real_column)); break;
            case "false":
                this.tokens.add(new Token(TokenType.INT_LIT, "0", line, real_column)); break;
            default:
                this.tokens.add(new Token(TokenType.IDENT, buffer, line, real_column));
                break;
        }
        flushBuffer();

    }

    private Character consume() {
        Character current = null;
        if (this.position < contents.length())
            current = this.contents.charAt(this.position);
        this.position++;
        this.incrementCol();
        return current;
    }

    private String peekAhead(int over) {
        if (this.position + over >= contents.length())
            return null;
        return this.contents.substring(this.position, this.position + over);
    }

    private Character peek(int over) {
         if (this.position + over >= contents.length())
            return null;
        return this.contents.charAt(this.position + over);
    }

    private Character peek() {
        if (this.position >= contents.length())
            return null;
        return this.contents.charAt(this.position);
    }

}
