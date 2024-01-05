package compiler;

public class Token {
 
    private TokenType type;
    private String value = null;

    public Token() {
        this.type = TokenType.DEFAULT;
    }

    public Token(TokenType type) {
        this.type = type;
    }

    public static Integer getBinaryPrecedenceLevel(TokenType type) {
        switch (type) {
            case ADD:
            case MINUS:
                return 0;
            case TIMES: 
            case DIVIDE:
                return 1;
            default:
                return null;
        }
    }

    @Override
    public String toString() {
        if (value == null)
            return String.format("{type: %s}", this.getType());
        return String.format("{type: %s, val: %s}", this.getType(), this.getValue());
    }

    public void setType(TokenType t) {
        this.type = t;
    }

    public void setValue(String v) {
        this.value = v;
    }

    public Token(TokenType type, String value) {
        this.type = type;
        this.value = value;
    }

    public TokenType getType() {
        return this.type;
    }

    public String getValue() {
        return this.value;
    }

}
