package ast;

import symboltable.SymbolTable;
import types.TypeClass;

public class AstDecArrayTypeDef extends AstDec
{
	public AstArrayTypeDef arrayTypeDef;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstDecArrayTypeDef(AstArrayTypeDef arrayTypeDef)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== dec -> arrayTypeDef\n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.arrayTypeDef = arrayTypeDef;
	}

	/************************************************/
	/* The printing message for an int exp AST node */
	/************************************************/
	public void printMe()
	{
		/*******************************/
		/* AST NODE TYPE = AST INT EXP */
		/*******************************/
		System.out.print("AST NODE DEC\n");

        /**************************************/
		/* RECURSIVELY PRINT left + right ... */
		/**************************************/
		if (arrayTypeDef != null) arrayTypeDef.printMe();

		/*********************************/
		/* Print to AST GRAPHVIZ DOT file */
		/*********************************/
		AstGraphviz.getInstance().logNode(
				serialNumber,
			String.format("DEC"));

        /****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (arrayTypeDef  != null) AstGraphviz.getInstance().logEdge(serialNumber,arrayTypeDef.serialNumber);
	}

	public Type semantMe()
	{	
		/*************************/
		/* [1] Begin Class Scope */
		/*************************/
		SymbolTable.getInstance().beginScope();

		/***************************/
		/* [2] Semant Data Members */
		/***************************/
		TypeClass t = new TypeClass(null,name, dataMembers.semantMe());

		/*****************/
		/* [3] End Scope */
		/*****************/
		SymbolTable.getInstance().endScope();

		/************************************************/
		/* [4] Enter the Class Type to the Symbol Table */
		/************************************************/
		SymbolTable.getInstance().enter(name,t);

		/*********************************************************/
		/* [5] Return value is irrelevant for class declarations */
		/*********************************************************/
		return null;		
	}
}
