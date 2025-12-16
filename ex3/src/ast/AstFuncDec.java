package ast;

import symboltable.SymbolTable;
import types.*;

public class AstFuncDec extends AstDec
{
	public AstType type;
    public String fieldName;
    public AstFuncArgList argList;
    public AstStmtList stmtList;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstFuncDec(AstType type, String fieldName, AstFuncArgList argList, AstStmtList stmtList, int line)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		super(line);
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		// System.out.format("====================== funcDec -> type ID LPAREN [ type ID { COMMA type ID } ] RPAREN LBRACE stmtList RBRACE\n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.type = type;
        this.fieldName = fieldName;
        this.argList = argList;
        this.stmtList = stmtList;
	}

	/************************************************/
	/* The printing message for an int exp AST node */
	/************************************************/
	public void printMe()
	{
		/*******************************/
		/* AST NODE TYPE = AST INT EXP */
		/*******************************/
		// System.out.format("AST NODE FUNCDEC( %s )\n",fieldName);
        
        /****************************************/
		/* RECURSIVELY PRINT VAR + SUBSCRIPT ... */
		/****************************************/
		if (type != null) type.printMe();
		if (argList != null) argList.printMe();
        if (stmtList != null) stmtList.printMe();
        
		/*********************************/
		/* Print to AST GRAPHVIZ DOT file */
		/*********************************/
		AstGraphviz.getInstance().logNode(
				serialNumber,
			String.format("FUNCDEC( %s )",fieldName));

        /****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (type       != null) AstGraphviz.getInstance().logEdge(serialNumber,type.serialNumber);
		if (argList != null) AstGraphviz.getInstance().logEdge(serialNumber,argList.serialNumber);
        if (stmtList != null) AstGraphviz.getInstance().logEdge(serialNumber,stmtList.serialNumber);
	}
	public void semantMe() throws SemanticException
	{
		Type returnType;
		TypeList argListTypes = null;

		/*******************/
		/* [0] return type */
		/*******************/
		returnType = SymbolTable.getInstance().find(type.name);
		if (returnType == null)
		{
			System.out.printf("ERROR at line %d, non existing return type %s\n", line, type.name);
			throw new SemanticException(String.format("ERROR(%d)",line));
		}
		/*******************/
		/* Check Duplicates & Enter Function (Recursive support) */
		/*******************/
		if (SymbolTable.getInstance().findInCurrentScope(fieldName) != null) {
			// special case: do not allow redefining in global scope
			if (SymbolTable.getInstance().isGlobalScope()) {
				System.out.printf("ERROR at line %d, name already exists in global scope\n", line, fieldName);
				throw new SemanticException(String.format("ERROR(%d)",line));
			}
			// allow redefining only if the functions have the same signature
			Type existing = SymbolTable.getInstance().find(fieldName);
			if (existing != null && existing.isFunction()) {
				TypeFunction existingFunc = (TypeFunction) existing;
				if (existingFunc.returnType.name.equals(returnType.name)) {
					// Check if the function signatures match
					TypeList a = existingFunc.params;
					AstFuncArgList astArgs = argList;
					TypeList aa = a;
					AstFuncArgList bb = astArgs;
					while (aa != null && bb != null) { aa = aa.tail; bb = bb.tail; }
					if (!((aa == null) && (bb == null))) {
						System.out.printf("ERROR at line %d, function %s already exists with different signature\n", line, fieldName);
						throw new SemanticException(String.format("ERROR(%d)", line));
					}
				}else{
					// function signatures do not match
					System.out.printf("ERROR at line %d, function %s already exists with different signature\n", line, fieldName);
					throw new SemanticException(String.format("ERROR(%d)",line));
				}
			}
			else{
				// existing symbol is not a function -> shadowing illegal
				System.out.printf("ERROR at line %d, field %s already exists\n", line, fieldName);
				throw new SemanticException(String.format("ERROR(%d)",line));
			}
		}
		/* Check for shadowing/overriding: if a symbol with the same name exists in an
		 * ancestor scope, disallow shadowing. If it's a function, allow overriding
		 * only when the signature (return type and parameter list) matches exactly.
		 */
		Type existing = SymbolTable.getInstance().find(fieldName);
		if (existing != null && SymbolTable.getInstance().findInCurrentScope(fieldName) != null) {
			if (existing.isFunction()) {
				TypeFunction existingFunc = (TypeFunction) existing;
				/* compare return type names */
				if (!java.util.Objects.equals(existingFunc.returnType.name, returnType.name)) {
					System.out.printf("ERROR at line %d, function %s already exists with different signature\n", line, fieldName);
					throw new SemanticException(String.format("ERROR(%d)", line));
				}
				/* compare parameter lists exactly by inspecting the AST argList without calling semantMe() */
				TypeList a = existingFunc.params;
				AstFuncArgList astArgs = argList;
				/* count and compare types */
				TypeList aa = a;
				AstFuncArgList bb = astArgs;
				// count arguments
				while (aa != null && bb != null) { aa = aa.tail; bb = bb.tail; }
				if (!((aa == null) && (bb == null))) {
					System.out.printf("ERROR at line %d, function %s already exists with different signature\n", line, fieldName);
					throw new SemanticException(String.format("ERROR(%d)", line));
				}
				// exact type equality check
				aa = a; bb = astArgs;
				while (aa != null && bb != null) {
					if (!java.util.Objects.equals(aa.head.name, bb.head.type.name)) {
						System.out.printf("ERROR at line %d, function %s already exists with different signature\n", line, fieldName);
						throw new SemanticException(String.format("ERROR(%d)", line));
					}
					aa = aa.tail; bb = bb.tail;
				}
				/* Signatures match exactly -> overriding is allowed */
			} else {
				/* existing symbol is not a function -> shadowing illegal */
				System.out.printf("ERROR at line %d, function %s already exists\n", line, fieldName);
				throw new SemanticException(String.format("ERROR(%d)", line));
			}
		}
		TypeFunction funcType = new TypeFunction(returnType, fieldName, null);
		SymbolTable.getInstance().enter(fieldName, funcType);
		/****************************/
		/* Begin Function Scope */
		/****************************/
		SymbolTable.getInstance().beginScope();

		/***************************/
		/* Set Current Function Return Type for return stmts */	
		/***************************/
		SymbolTable.getInstance().setFunctionReturnType(returnType);

		/***************************/
		/* Semant Input Params */
		/***************************/
		if (argList != null) {
			argListTypes = argList.semantMe();
		}
		/***************************/
		/* Update FUNCTION TYPE params*/
		/***************************/
		funcType.params = argListTypes;

		/*******************/
		/* Semant Body */
		/*******************/
		stmtList.semantMe();

		/*****************/
		/* End Scope */
		/*****************/
		SymbolTable.getInstance().endScope();

		/************************************************************/
		/* Return value is irrelevant for function declarations */
		/************************************************************/
			
	}
}
