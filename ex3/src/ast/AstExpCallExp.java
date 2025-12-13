package ast;
import types.*;
public class AstExpCallExp extends AstExp
{
	public AstCallExp callExp;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstExpCallExp(AstCallExp callExp)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		// System.out.format("====================== exp -> callExp\n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.callExp = callExp;
	}

	/************************************************/
	/* The printing message for an int exp AST node */
	/************************************************/
	public void printMe()
	{
		/*******************************/
		/* AST NODE TYPE = AST INT EXP */
		/*******************************/
		// System.out.format("AST NODE CALLEXP\n");

        /**************************************/
		/* RECURSIVELY PRINT left + right ... */
		/**************************************/
		if (callExp != null) callExp.printMe();

		/*********************************/
		/* Print to AST GRAPHVIZ DOT file */
		/*********************************/
		AstGraphviz.getInstance().logNode(
				serialNumber,
			String.format("CALLEXP"));

        /****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (callExp  != null) AstGraphviz.getInstance().logEdge(serialNumber,callExp.serialNumber);
	}
	public Type semantMe()
	{
		return callExp.semantMe();
	}
}
