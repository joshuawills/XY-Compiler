## 16.01.24

- Added in config errors (xy.config)
    - Identifying unused variables and functions
    - Variable styling (camel case or snake case)
    - Identifying comprehensive scope and brackets declarations
- Introduced concept of non-breaking errors
- Can read in integers using "in" method
- first [blog-post](https://joshuawills.github.io/blogs/01.html)

## 17.01.24

- Fix break/continue bug
- Improve *toString()* approach for logging out parse tree
- Introduced UnaryExpression type - need to add tests for

## 18.01.24

- Add in void return type, basic error checking

## 19.01.24

- Refactor variable declarations to accommodate strings
- String support for 'in' method

## 21.01.24

- General porting of stuff in the generator to the verifier (also a type-checker at this point)
    - Whole new type-checking system
    - Checks for unused variables etc.
    - Required quite a significant refactoring
        - Will need to clean it up next
- Add strings in function returns

## 22.01.24

- Added in boolean type
- Added in char type

## 27.01.24

- Added in array types


## 28.01.24

- Support specifying variables as mut in function calls 
- `__lc__` is a reversed word - need to actually implement in code
- loops with num specifiers (like `for i in range(10)` or whatever in python) 

## 29.01.24

- Implementation of for loops

## TO-DO

- Make sure something is actually returned for strings and integers (i.e. non void return methods)
- Add in tests for UnaryExpression type
- Add a keyword for the iterator value `it`