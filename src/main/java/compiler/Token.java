package compiler;

import java.lang.invoke.CallSite;

public class Token {
    
    private TokenType type;
    private String value = null;
    private Integer line; private Integer col;
    
    public Token(TokenType type, int line, int col) {
        this.type = type;
        this.line = line;
        this.col = col;
    }
    
    public Token(TokenType type, String value, int line, int col) {
        this.type = type;
        this.value = value;
        this.line = line;
        this.col = col;
    }
    public static Integer getBinaryPrecedenceLevel(TokenType type) {
        switch (type) {
            
            case GREATER_EQ:
            case GREATER_THAN:
            case LESS_THAN:
            case LESS_EQ:
                return 0;
            case EQUAL:
            case NOT_EQUAL:
                return 1;
            case AND_LOGIC:
                return 2;
            case OR_LOGIC:
                return 3;
            case PLUS:
            case DASH:
                return 4;
            case STAR: 
            case F_SLASH:
                return 5;
            default:
                return null;
        }
    }

    public Integer getLine() {
        return this.line;
    }

    public Integer getCol() {
        return this.col;
    }

    @Override
    public String toString() {
        if (value == null)
            return String.format("{type: %s (%s, %s)}", this.getType(), this.getLine().toString(), this.getCol().toString());
        return String.format("{type: %s, val: %s (%s, %s)}", this.getType(), this.getValue(), this.getLine().toString(), this.getCol().toString());
    }

    public void setType(TokenType t) {
        this.type = t;
    }

    public void setValue(String v) {
        this.value = v;
    }


    public TokenType getType() {
        return this.type;
    }

    public String getValue() {
        return this.value;
    }

}
