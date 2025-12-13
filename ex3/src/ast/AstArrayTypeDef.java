package ast;

import symboltable.*;
import types.*;

public class AstArrayTypeDef extends AstDec
{
	public String fieldname;
	public AstType elemType;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstArrayTypeDef(String fieldname, AstType elemType)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== arrayTypeDef -> ARRAY ID( %s ) EQ type LBRACK RBRACK SEMICOLON\n", fieldname);

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.fieldname = fieldname;
		this.elemType = elemType;

	}
	
	/***********************************************/
	/* The default message for an exp var AST node */
	/***********************************************/
	public void printMe()
	{
		/************************************/
		/* AST NODE TYPE = EXP VAR AST NODE */
		/************************************/
		System.out.format("AST NODE ARRAY TYPE DEFINITION ( %s )\n", fieldname);
		
		/*********************************/
		/* Print to AST GRAPHVIZ DOT file */
		/*********************************/
		AstGraphviz.getInstance().logNode(
				serialNumber,
			String.format("ARRAYTYPEDEF( %s )", fieldname));
	}

	public void semantMe()
	{
		Type t;
		if (!SymbolTable.getInstance().isGlobalScope()) {
			System.out.format(">> ERROR [%d:%d] array type definitions are allowed only in the global scope\n",2,2);
			System.exit(0);
		}
		/****************************/
		/* [1] Make sure the type of elements is not void */
		/****************************/
		if ("void".equals(elemType.name)){
			System.out.format(">> ERROR [%d:%d] elements %s cannot be of type void\n",2,2,fieldname);
			System.exit(0);
		}
		/****************************/
		/* [2] Check If Type of elements exists */
		/****************************/
		t = SymbolTable.getInstance().find(elemType.name);
		if (t == null)
		{
			System.out.format(">> ERROR [%d:%d] non existing type %s\n",2,2,elemType.name);
			System.exit(0);
		}
		
		/**************************************/
		/* [3] Check That Name does NOT exist in current scope*/
		/**************************************/
		if (SymbolTable.getInstance().find(fieldname) != null)
		{
			System.out.format(">> ERROR [%d:%d] array %s already exists in scope\n",2,2,fieldname);	
			System.exit(0);		
		}
		
		/************************************************/
		/* [4] Enter the Identifier to the Symbol Table */
		/************************************************/
		TypeArray typeArray = new TypeArray(fieldname, t);
		SymbolTable.getInstance().enter(fieldname,typeArray);

		/************************************************************/
		/* [5] Return value is irrelevant for variable declarations */
		/************************************************************/	
	}
}
