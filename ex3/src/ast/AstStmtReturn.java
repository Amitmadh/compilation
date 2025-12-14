package ast;

import symboltable.SymbolTable;
import types.*;

public class AstStmtReturn extends AstStmt
{
	public AstExp exp;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstStmtReturn(AstExp exp)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		// System.out.print("====================== stmt -> RETURN [exp] SEMICOLON\n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.exp = exp;
	}
	
	/***********************************************/
	/* The default message for an exp var AST node */
	/***********************************************/
	public void printMe()
	{
		/************************************/
		/* AST NODE TYPE = EXP VAR AST NODE */
		/************************************/
		// System.out.print("AST NODE STMT RETURN\n");

		/*****************************/
		/* RECURSIVELY PRINT var ... */
		/*****************************/
		if (exp != null) exp.printMe();
		
		/*********************************/
		/* Print to AST GRAPHVIZ DOT file */
		/*********************************/
		AstGraphviz.getInstance().logNode(
				serialNumber,
			"STMT\nRETURN");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (exp != null)AstGraphviz.getInstance().logEdge(serialNumber,exp.serialNumber);
			
	}

	public Type semantMe() throws SemanticException
	{
		/* Retrieve expected return type from symbol table */
		Type expectedType = SymbolTable.getInstance().getFunctionReturnType();
        
		if (expectedType == null) {
			return null; 
		}

		Type actualType = null;
		if (exp != null) {
			actualType = exp.semantMe();
		}

		/* If func has return type void, its return stmt must be empty */
		if (expectedType instanceof TypeVoid || "void".equals(expectedType.name)) {
			if (exp != null) {
				throw new SemanticException(String.format(">> ERROR [%d:%d] void function cannot return a value", 2, 2));
			}
			return null; 
		}

		if (exp == null) {
			throw new SemanticException(String.format(">> ERROR [%d:%d] non-void function must return a value", 2, 2));
		}

		/* check return type is compatible with func return type */
        
		/* Handle nil case */
		if (actualType instanceof TypeNil || "nil".equals(actualType.name)) {
			if (!expectedType.isClass() && !expectedType.isArray()) {
				throw new SemanticException(String.format(">> ERROR [%d:%d] cannot return nil from function returning primitive type %s", 2, 2, expectedType.name));
			}
			return null; 
		}

		/* types match exactly */
		if (expectedType.name.equals(actualType.name)) {
			return null; // OK
		}

		/* Handle class inheritance case */
		if (expectedType.isClass() && actualType.isClass()) {
			TypeClass expectedClass = (TypeClass) expectedType;
			TypeClass actualClass = (TypeClass) actualType;
            
			if (!actualClass.isSubClassOf(expectedClass)) {
				throw new SemanticException(String.format(">> ERROR [%d:%d] return type %s is not a subclass of expected type %s", 2, 2, actualType.name, expectedType.name));
			}
			return null; 
		}

		/* If we got here, types are incompatible */
		throw new SemanticException(String.format(">> ERROR [%d:%d] return type mismatch. Expected %s, got %s", 2, 2, expectedType.name, actualType.name));
	}
	
}

