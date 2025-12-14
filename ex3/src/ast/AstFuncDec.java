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
	public AstFuncDec(AstType type, String fieldName, AstFuncArgList argList, AstStmtList stmtList)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
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
			throw new SemanticException(String.format(">> ERROR [%d:%d] non existing return type %s",6,6,type.name));
		}
		/*******************/
		/* Check Duplicates & Enter Function (Recursive support) */
		/*******************/
		if (SymbolTable.getInstance().findInCurrentScope(fieldName) != null) {
			throw new SemanticException(String.format(">> ERROR [%d:%d] function %s already exists", 6, 6, fieldName));
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
