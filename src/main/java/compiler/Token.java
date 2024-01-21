package compiler;


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
        // https://www.tutorialspoint.com/cprogramming/c_operators_precedence.htm
        switch (type) {
            case OR_LOGIC:
                return 0;
            case AND_LOGIC:
                return 1;
            case BITWISE_OR:
                return 2;
            case BITWISE_XOR:
                return 3;
            case BITWISE_AND:
                return 4;
            case EQUAL:
            case NOT_EQUAL:
                return 5;
            case LESS_THAN:
            case LESS_EQ:
            case GREATER_EQ:
            case GREATER_THAN:
                return 6;
            case BITWISE_LEFT_SHIFT:
            case BITWISE_RIGHT_SHIFT:
                return 7;
            case PLUS:
            case DASH:
                return 8;
            case STAR:
            case F_SLASH:
            case PERCENT:
                return 9;
            default:
                return -1;
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

    public static boolean isReturnType(TokenType type) {
        return type.equals(TokenType.DECLARE) || type.equals(TokenType.VOID);
    }


}
