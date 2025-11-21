package ast;

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
		System.out.format("====================== dec -> arrayTypeDef\n");

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
		System.out.format("AST NODE ARRAY TYPE DEF\n");

        /**************************************/
		/* RECURSIVELY PRINT left + right ... */
		/**************************************/
		if (arrayTypeDef != null) arrayTypeDef.printMe();

		/*********************************/
		/* Print to AST GRAPHVIZ DOT file */
		/*********************************/
		AstGraphviz.getInstance().logNode(
				serialNumber,
			String.format("ARRAYTYPEDEF"));

        /****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (arrayTypeDef  != null) AstGraphviz.getInstance().logEdge(serialNumber,arrayTypeDef.serialNumber);
	}
}
