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
    PERCENT,

    GREATER_THAN,
    GREATER_EQ,
    LESS_THAN,
    LESS_EQ,
    NOT_EQUAL,
    EQUAL,
    AND_LOGIC,
    OR_LOGIC,

    NEGATE,

    OPEN_PAREN,
    CLOSE_PAREN,

    OPEN_CURLY,
    CLOSE_CURLY,

    IF,
    ELIF,
    ELSE,
    WHILE
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
