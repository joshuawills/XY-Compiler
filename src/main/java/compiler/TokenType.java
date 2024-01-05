package compiler;

public enum TokenType {
    INT_LIT,
    SEMI,
    RETURN,
    DEFAULT,
    INT_TYPE,
    ASSIGN,
    STRING,

    ADD,
    MINUS,
    TIMES,
    DIVIDE,

    OPEN_PAREN,
    CLOSE_PAREN,

    PRINT_I
}
/* 
 * Precedence level for operators:
 *  Division
 *  Multiplication
 *  Addition
 *  Subtraction
 */
