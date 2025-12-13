package ast;
import types.*;
public class AstStmtCallExp extends AstStmt
{
	public AstCallExp callExp;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstStmtCallExp(AstCallExp callExp)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== stmt -> callExp SEMICOLON\n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.callExp = callExp;
	}
	
	/***********************************************/
	/* The default message for an exp var AST node */
	/***********************************************/
	public void printMe()
	{
		/************************************/
		/* AST NODE TYPE = EXP VAR AST NODE */
		/************************************/
		System.out.print("AST NODE STMT CALLEXP\n");

		/*****************************/
		/* RECURSIVELY PRINT var ... */
		/*****************************/
		if (callExp != null) callExp.printMe();
		
		/*********************************/
		/* Print to AST GRAPHVIZ DOT file */
		/*********************************/
		AstGraphviz.getInstance().logNode(
				serialNumber,
			"STMT\nCALLEXP");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AstGraphviz.getInstance().logEdge(serialNumber,callExp.serialNumber);
			
	}
	public Type semantMe()
	{
		return callExp.semantMe();
	}
}
