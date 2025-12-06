package ast;

import symboltable.SymbolTable;
import types.TypeClass;

public class AstClassDec extends AstDec
{
    public String className;
    public String extendName;
    public AstCfieldList cfieldList;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstClassDec(String className, String extendName, AstCfieldList cfieldList)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== classDec -> CLASS ID( %s ) [ EXTENDS ID( %s ) ] LBRACE cField { cField } RBRACE\n", className, extendName);

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.className = className;
        this.extendName = extendName;
        this.cfieldList = cfieldList;
	}

	/************************************************/
	/* The printing message for an int exp AST node */
	/************************************************/
	public void printMe()
	{
		/*******************************/
		/* AST NODE TYPE = AST INT EXP */
		/*******************************/
		System.out.format("AST NODE CLASSDEC( %s )",className);
        if (extendName != null) System.out.format(" EXTENDES( %s )",extendName);
        System.out.format("\n");

        /****************************************/
		/* RECURSIVELY PRINT VAR + SUBSCRIPT ... */
		/****************************************/
		if (cfieldList != null) cfieldList.printMe();
        
		/*********************************/
		/* Print to AST GRAPHVIZ DOT file */
		/*********************************/
		AstGraphviz.getInstance().logNode(
				serialNumber,
			String.format("CLASSDEC( %s )",className));

        /****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (cfieldList != null) AstGraphviz.getInstance().logEdge(serialNumber,cfieldList.serialNumber);
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
