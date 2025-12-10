package ast;
import types.*;
public class AstDecVarDec extends AstDec
{
	public AstVarDec varDeck;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstDecVarDec(AstVarDec varDeck)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== dec -> varDec\n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.varDeck = varDeck;
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
		if (varDeck != null) varDeck.printMe();

		/*********************************/
		/* Print to AST GRAPHVIZ DOT file */
		/*********************************/
		AstGraphviz.getInstance().logNode(
				serialNumber,
			String.format("DEC"));

        /****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (varDeck  != null) AstGraphviz.getInstance().logEdge(serialNumber,varDeck.serialNumber);
	}
	public Type semantMe()
	{
		return varDeck.semantMe();
	}
	
}
