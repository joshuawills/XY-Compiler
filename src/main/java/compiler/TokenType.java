package compiler;

public enum TokenType {
    DEFAULT,
    SEMI,

    INT_LIT,
    
    RETURN,
    INIT_INT,
    ASSIGN,
    IDENT,

    PLUS,
    DASH,
    STAR,
    F_SLASH,

    GREATER_THAN,
    GREATER_EQ,
    LESS_THAN,
    LESS_EQ,
    NOT_EQUAL,
    EQUAL,
    AND_LOGIC,
    OR_LOGIC,

    NEGATE,

    TRUE,
    FALSE,

    OPEN_PAREN,
    CLOSE_PAREN,

    OPEN_CURLY,
    CLOSE_CURLY,

    IF,
    ELIF,
    ELSE,
    WHILE,

    OUT,
    PRINT_S,

    STRING,

    INCREMENT,
    DECREMENT,

    PLUS_EQUAL,
    DASH_EQUAL,
    STAR_EQUAL,
    F_SLASH_EQUAL,
    
    MUT
}

/* 
 * Precedence level for operators:
 *  Division
 *  Multiplication
 *  Addition
 *  Subtraction
 */

//  (): Parentheses
// *, /: Multiplication and Division
// +, -: Addition and Subtraction
// <, >, <=, >=: Relational operators
