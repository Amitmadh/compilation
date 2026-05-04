# L Compiler

A complete compiler for the **L programming language**, implementing lexical analysis, syntax parsing, semantic analysis, and code generation to MIPS assembly.

## Overview

This project is a full-featured compiler written in Java that compiles programs written in the L language (a C-like procedural language) to MIPS assembly code. The compiler performs multiple stages of compilation including lexical analysis, parsing, semantic analysis, intermediate representation generation, and machine code generation.

## Features

- **Lexical Analysis**: Tokenization using JFlex
- **Syntax Parsing**: Grammar-based parsing using Java CUP
- **Abstract Syntax Tree (AST)**: Full representation of program structure
- **Semantic Analysis**: Type checking, symbol table management
- **Intermediate Representation (IR)**: Language-independent code generation
- **MIPS Code Generation**: Final assembly output for MIPS architecture
- **Control Flow Graph (CFG)**: Program flow analysis
- **Register Allocation**: Efficient register usage in generated code
- **Supported Language Features**:
  - Functions with parameters and return values
  - Local and global variables
  - Classes and objects
  - Arrays and subscripting
  - Control flow: if/else, while loops
  - String and integer operations
  - Function calls and recursion

## Project Structure

```
├── src/                          # Source code
│   ├── Main.java                # Compiler entry point
│   ├── Lexer.java               # Lexer (generated from LEX_FILE.lex)
│   ├── Parser.java              # Parser (generated from CUP_FILE.cup)
│   ├── TokenNames.java          # Token definitions (generated)
│   ├── RegisterAllocation.java   # Register allocation strategy
│   ├── ast/                      # Abstract Syntax Tree node definitions
│   ├── cfg/                      # Control Flow Graph implementation
│   ├── data/                     # Data structures (ClassData, FunctionData)
│   ├── ir/                       # Intermediate Representation commands
│   ├── mips/                     # MIPS code generation
│   ├── symboltable/              # Symbol table management
│   ├── temp/                     # Temporary variables handling
│   └── types/                    # Type system
├── jflex/
│   └── LEX_FILE.lex              # JFlex lexer specification
├── cup/
│   └── CUP_FILE.cup              # Java CUP grammar specification
├── input/                        # Test input files
│   ├── TEST_01_Print_Primes.txt
│   ├── TEST_02_Bubble_Sort.txt
│   ├── TEST_03_Merge_Lists.txt
│   ├── TEST_04_Matrices.txt
│   ├── TEST_05_Classes.txt
│   ├── TEST_06_Strings.txt
│   ├── TEST_07_Arrays.txt
│   └── ... (26 test files total)
├── output/                       # Compiled output files
├── examples/                     # Example programs
├── bin/                          # Compiled Java classes
├── external_jars/                # External dependencies (Java CUP runtime)
├── manifest/                     # JAR manifest
├── Makefile                      # Build automation
└── README.md                     # This file
```

## Building the Compiler

### Prerequisites

- Java Development Kit (JDK) 8 or higher
- JFlex (lexer generator)
- Java CUP (parser generator)
- GNU Make

### Build Instructions

```bash
# Build the entire project
make

# Clean compiled files
make clean

# Generate lexer only
make jflex

# Generate parser only
make cup

# Compile Java sources only
make compile

# Run all tests
make run

# Build JAR file
make jar
```

## Usage

### Basic Compilation

```bash
java -jar Compiler.jar input_file.l output_file.s
```

### Example

**Input file** (`example.l`):
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

**Compilation**:
```bash
java -jar Compiler.jar example.l example.s
```

**Output**: `example.s` (MIPS assembly code)

## L Language Syntax

### Variables and Types
```c
int x := 5;           // Integer variable
int arr[10];          // Array declaration
```

### Functions
```c
int add(int a, int b) {
    int result := a + b;
    return result;
}
```

### Classes
```c
class Point {
    int x;
    int y;
}
```

### Control Flow
```c
if (x > 0) {
    PrintInt(x);
}

while (i < 10) {
    i := i + 1;
}
```

### Operators
- Arithmetic: `+`, `-`, `*`, `/`
- Comparison: `<`, `>`, `=` (equality)
- Assignment: `:=`

## Compilation Stages

### 1. Lexical Analysis (Lexer)
- Reads source code character by character
- Produces tokens (keywords, identifiers, literals, operators)
- Handles line and column tracking

### 2. Syntax Analysis (Parser)
- Uses LALR parser generated from grammar
- Builds Abstract Syntax Tree (AST)
- Reports syntax errors

### 3. Semantic Analysis
- Symbol table construction
- Type checking
- Scope validation
- Method resolution

### 4. Intermediate Representation (IR)
- Language-independent intermediate code
- Optimization opportunities
- Machine-independent representation

### 5. Code Generation
- MIPS assembly generation
- Register allocation
- Control flow optimization

## Test Suite

The project includes 26 comprehensive test cases covering:
- Print operations (Primes)
- Sorting algorithms (Bubble Sort)
- Data structure operations (Merge Lists)
- Matrix operations
- Object-oriented features (Classes)
- String handling
- Array operations
- Error handling (Access Violations)
- Advanced arithmetic (Fibonacci, Overflow)
- Global and local variables
- Operator precedence

Run tests with:
```bash
make run
```

## Generated Files

The compiler generates the following files from specifications:

- `src/Lexer.java` - Generated from `jflex/LEX_FILE.lex`
- `src/Parser.java` - Generated from `cup/CUP_FILE.cup`
- `src/TokenNames.java` - Token symbol definitions (generated)

## Dependencies

- **Java CUP 11b**: LALR parser generator for Java
  - Located in `external_jars/java-cup-11b.jar`
  - Location: [www.cups.cs.umn.edu](http://www2.cs.umn.edu/~raid/cup/)

## Error Handling

The compiler provides detailed error reporting:
- Lexical errors: Invalid tokens or syntax
- Parse errors: Grammar violations with line numbers
- Semantic errors: Type mismatches, undefined variables, scope issues
- Runtime warnings: Overflow, access violations

## Architecture Highlights

### Symbol Table
Manages variable declarations, function definitions, and class definitions with scope tracking.

### Type System
Supports primitive types (int), complex types (arrays, classes), and type compatibility checking.

### Register Allocation
Implements efficient register assignment to minimize memory access in generated MIPS code.

### Control Flow Graph
Analyzes program control flow for optimization and debugging.

## Notes

- The L language uses `:=` for variable assignment (not `=`)
- All variables must be explicitly typed
- Functions support recursion
- Arrays are indexed from 0
- Type checking is static and strict

## Building a JAR

To create an executable JAR file:

```bash
make jar
```

The JAR can then be used as:
```bash
java -jar Compiler.jar input.l output.s
```

## Troubleshooting

**Problem**: JFlex not found
- **Solution**: Install JFlex and ensure it's in your PATH

**Problem**: CUP parser errors
- **Solution**: Check that Java CUP JAR files are in `external_jars/`

**Problem**: Compilation fails
- **Solution**: Ensure JDK 8+ is installed and in your PATH

## Future Enhancements

- Optimization passes
- Extended language features
- Debugging symbol generation
- Performance improvements
- Code generation for additional architectures

## License

This project was developed as part of a compiler design course.

## Author

Compiler Development Project - Exercise 5
