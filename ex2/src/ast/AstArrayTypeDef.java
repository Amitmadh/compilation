package ast;

public class AstArrayTypeDef extends AstDec
{
	public String fieldname;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstArrayTypeDef(String fieldname)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== arrayTypeDef -> ARRAY ID EQ type LBRACK RBRACK SEMICOLON\n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.fieldname = fieldname;
	}
	
	/***********************************************/
	/* The default message for an exp var AST node */
	/***********************************************/
	public void printMe()
	{
		/************************************/
		/* AST NODE TYPE = EXP VAR AST NODE */
		/************************************/
		System.out.print("AST NODE ARRAY TEPE DEFINITION\n");
		
		/*********************************/
		/* Print to AST GRAPHVIZ DOT file */
		/*********************************/
		AstGraphviz.getInstance().logNode(
				serialNumber,
			"ARRAYTYPEDEF");
	}
}
