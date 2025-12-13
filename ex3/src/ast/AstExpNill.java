package ast;
import types.*;
public class AstExpNill extends AstExp
{
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstExpNill()
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		// System.out.format("====================== exp -> NILL\n");
	}

	/************************************************/
	/* The printing message for an int exp AST node */
	/************************************************/
	public void printMe()
	{
		/*******************************/
		/* AST NODE TYPE = AST INT EXP */
		/*******************************/
		// System.out.format("AST NODE NILL\n");

		/*********************************/
		/* Print to AST GRAPHVIZ DOT file */
		/*********************************/
		AstGraphviz.getInstance().logNode(
				serialNumber,
			String.format("NILL"));
	}
	public Type semantMe()
	{
		return TypeNil.getInstance();
	}
}
