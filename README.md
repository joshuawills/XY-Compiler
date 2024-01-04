# XY Compiler

This repo will be where I track any work I complete on the XY
compiler, a C-like syntactic language. Any significant developments will be logged in a LOG.md file, and also 
eventually in a website for the language.

## Simple Structure of the Compiler

### Lexer Creation
  - Turning byte stream into a sequence of tokens that can be interpreted
  - int number = 5; => [variableINIT] [variableNAME] [assignOPER] [variableVAL] [semi]

### Parser
  - Turning tokens into an AST that can be interpreted by the generator

### Generator
  - writes to a vector to store the actual ASM code

## Basic Grammar

Programs that currently work

```
exit;
```

```
exit <i64>;
```

Programs that will one day work :(

```
using io;

ftn main() {
  
  io::out("Hello, world\n");

  return 0;
}
```
