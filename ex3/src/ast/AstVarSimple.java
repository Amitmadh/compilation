package ast;
import symboltable.*;
import types.*;

public class AstVarSimple extends AstVar
{
	/************************/
	/* simple variable name */
	/************************/
	public String name;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstVarSimple(String name, int line)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		super(line);
		serialNumber = AstNodeSerialNumber.getFresh();
	
		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		// System.out.format("====================== var -> ID( %s )\n",name);

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.name = name;
	}

	/**************************************************/
	/* The printing message for a simple var AST node */
	/**************************************************/
	public void printMe()
	{
		/**********************************/
		/* AST NODE TYPE = AST SIMPLE VAR */
		/**********************************/
		// System.out.format("AST NODE SIMPLE VAR( %s )\n",name);

		/*********************************/
		/* Print to AST GRAPHVIZ DOT file */
		/*********************************/
		AstGraphviz.getInstance().logNode(
				serialNumber,
			String.format("SIMPLE\nVAR(%s)",name));
	}

	public Type semantMe() throws SemanticException
	{
		/****************************/
		/* [1] Look up in symbol table */
		/****************************/
		Type t = SymbolTable.getInstance().find(name);
		
		/******************************/
		/* [2] Check if variable exists */
		/******************************/
		if (t == null)
		{
			throw new SemanticException(String.format(">> ERROR [%d:%d] variable %s does not exist in scope",2,2,name));
		}
		
		/*************************/
		/* [3] Return variable type */
		/*************************/
		return t;
	}
}
