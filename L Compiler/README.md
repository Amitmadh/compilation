# L Compiler

A complete compiler for the **L programming language**, implementing lexical analysis, syntax parsing, semantic analysis, and code generation to MIPS assembly.



- Lexical analysis (JFlex), syntax parsing (Java CUP), AST generation
- Semantic analysis: type checking, symbol tables, scope validation
- IR generation and MIPS code generation with register allocation
- Control flow graph analysis
- **Supported**: Functions, local/global variables, classes, arrays, if/else, while loops, strings, integers, recursion

## Project Structure

```
├── src/                  # Source code (AST, CFG, IR, MIPS, symboltable, types, etc.)
├── jflex/LEX_FILE.lex    # Lexer specification
├── cup/CUP_FILE.cup      # Parser grammar specification
├── input/                # 26 test files
├── output/               # Compiled output
├── examples/             # Example programs
├── bin/                  # Compiled classes
├── external_jars/        # Java CUP runtime
└── Makefile              # Build automation
```

## Building

**Prerequisites**: JDK 8+, JFlex, Java CUP, GNU Make

```bash
make              # Build entire project
make clean        # Clean compiled files
```

## Usage

```bash
java -jar COMPILER input.txt output.asm
```

Example:
```c
int f(int x, int y) {
    int z := x + y;
    return z;
}
void main() {
    int z := f(1, 2);
    PrintInt(z);
}
```

## L Language Syntax

**Variables**: `int x := 5;` | **Arrays**: `int arr[10];` | **Functions**: `int add(int a, int b) { ... }`  
**Classes**: `class Point { int x; int y; }` | **Control Flow**: `if`, `while`  
**Operators**: Arithmetic (`+`, `-`, `*`, `/`), Comparison (`<`, `>`, `=`), Assignment (`:=`)

## Compilation Stages

1. **Lexical Analysis**: Tokenization with line/column tracking
2. **Syntax Analysis**: LALR parser builds AST
3. **Semantic Analysis**: Type checking, symbol tables, scope validation
4. **IR Generation**: Language-independent intermediate code
5. **Code Generation**: MIPS assembly with register allocation

## Tests

26 test cases cover: primes, sorting, lists, matrices, classes, strings, arrays, access violations, recursion, operators, and more.

## Generated Files

- `src/Lexer.java` (from `jflex/LEX_FILE.lex`)
- `src/Parser.java` (from `cup/CUP_FILE.cup`)
- `src/TokenNames.java` (token definitions)

## Dependencies

- **Java CUP 11b**: LALR parser generator (in `external_jars/java-cup-11b.jar`)

## Error Handling

Detailed error reporting for: lexical errors, parse errors (with line numbers), semantic errors (type mismatches, undefined variables, scope), and runtime warnings (overflow, access violations)

## Architecture

- **Symbol Table**: Scope-aware variable, function, and class management
- **Type System**: Primitive and complex types with compatibility checking
- **Register Allocation**: Efficient MIPS register assignment
- **Control Flow Graph**: Program flow analysis for optimization

## Notes

- Assignment operator is `:=` (not `=`)
- Variables must be explicitly typed
- Functions support recursion; arrays indexed from 0
- Static, strict type checking


## Troubleshooting

- **JFlex not found**: Install JFlex and add to PATH
- **CUP parser errors**: Verify Java CUP JARs in `external_jars/`
- **Compilation fails**: Check JDK 8+ is installed and in PATH

