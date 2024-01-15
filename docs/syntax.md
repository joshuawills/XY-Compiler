# Formal XY Syntax

The xy programming language follows a standard C-like language syntax, with a few notable distinctions. To understand all of these differences succinctly, please read through the documentation for the syntax;

## Key Features

- all statements must end with a semicolon (**;**)
- scope is defined using curly braces (**{**, **}**)
- round braces are not required for conditionals
- program will exit code 0 by default if not provided

##  Functions

All code must be abstracted into functions. Program flow begins at the main function.
Function syntax is as follows

```
define <funcName>(<parameters>) -> <returnType> {
    <scope>
}
```

## Variable declaration

In XY, all variable declarations are constant by default. Variables can only be reassigned if they are specifically denoted as being mutable, which is done with the **mut** keyword.

```xy
int x = 5;      // This cannot be re-declared
mut int y = 5;  // This can be re-declared
y += 1;         // As shown here
```

Currently the only supported data types are **int's**. I plan to add more soon.

## Control Flow

There are several control flow operators already added to the XY language as listed below.

1. While loops

```xy
mut int x = 0;
while x < 10 {
    out x;
    x++;
}
```

2. If, else if and else

```xy
int y = 0;
if y > 0 {
    out "This is a position number";
} else if y == 0 {
    out "This is the number 0";
} else {
    out "This is a negative number";
}
```

3. Do-While loops

```xy
mut int x = 0;
do {
    out x;
} while x != 0;
```

4. Loop

*an infinite loop*

```xy
mut int x = 0;
loop {
    out x;
    if x == 5 { break; }
    x++;
}
```

5. break and continue

*Only apply to the innermost-loop*

## Writing to stdout

Currently the only "print" method supported is **out**, that can log static strings or any kind of integer

## Exit code

Using return <arithmetic operation\> will specify the exit code for your program.

## Arithmetic operators

Addition, subtraction, division and multiplication are all supported. XY supports the standard hierarchy of operations, as shown below (1 being the highest priority)

1. Brackets
2. Division and multiplication
3. Addition and subtraction

If two operations are of equal precedence the first one to be evaluated is the left-most one

e.g.
```xy
    out (2 * 3 - 1);   // 5
    out (2 * (3 - 1)); // 4
    out (2 * 3 / 1);   // 6
```
The following operators are also supported.
These are all lower priority than the arithmetic operators and allow for advanced conditionals.

>=, <=, >, <, ==, !=, &&, ||

**Revised precedence**

1. Brackets
2. Division and multiplication
3. Addition and subtraction
4. ||
5. &&
6. ==, !=
7. <=, <, >=, >

They keywords **true** and **false** can be used in these contexts as well, evaluating to 0 and 1 respectively.

The following statements are also possible, and mimic C.

++, --, +=, -=, *=, /=. They can only be applied to pre-declared variables.

## Comments

Comments follow standard C-style. 

Use // for single line comments<br />
Use /* ... */ for multi-line comments



