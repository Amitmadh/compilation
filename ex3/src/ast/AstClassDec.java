package ast;

import symboltable.*;
import types.*;

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
		// System.out.format("====================== classDec -> CLASS ID( %s ) [ EXTENDS ID( %s ) ] LBRACE cField { cField } RBRACE\n", className, extendName);

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
		// System.out.format("AST NODE CLASSDEC( %s )",className);
        // if (extendName != null) System.out.format(" EXTENDES( %s )",extendName);
        // System.out.format("\n");

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

	public void semantMe() throws SemanticException
	{	
		/***************************/
		/* [1] Class in global scope */
		if (!SymbolTable.getInstance().isGlobalScope()) {
			throw new SemanticException(String.format(">> ERROR [%d:%d] class declarations are allowed only in the global scope",2,2));
		}
		/* [2] Check That Name does NOT exist in current scope*/
		/**************************************/
		if (SymbolTable.getInstance().find(className) != null)
		{
			throw new SemanticException(String.format(">> ERROR [%d:%d] class %s already exists in scope",2,2,className));
		}
		/***************************/
		/* [3] Handle Extends */
		Type fatherType = null;
		if (extendName != null)
		{
			fatherType = SymbolTable.getInstance().find(extendName);
			if (fatherType == null || !(fatherType.isClass()))
			{
				throw new SemanticException(String.format(">> ERROR [%d:%d] non existing father class %s",2,2,extendName));
			}
			
		}

		/***************************/
		/* [4] Enter the Class Type to the Symbol Table */
		/***************************/
		TypeClass t = new TypeClass((TypeClass)fatherType, className, null);
		SymbolTable.getInstance().enter(className,t);
		/*************************/
		/* [5] Begin Class Scope */
		/*************************/
		SymbolTable.getInstance().beginScope();

		/*******************************/
		/* [6] Semant Class Fields */		
		if (cfieldList != null) {
			t.dataMembers = cfieldList.semantMe();
		}
		/*****************/
		/* [7] End Scope */
		/*****************/
		SymbolTable.getInstance().endScope();

		/*********************************************************/
		/* [8] Return value is irrelevant for class declarations */
		/*********************************************************/
		
	}
}
