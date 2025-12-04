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
		System.out.format("====================== arrayTypeDef -> ARRAY ID( %s ) EQ type LBRACK RBRACK SEMICOLON\n", fieldname);

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
		System.out.format("AST NODE ARRAY TYPE DEFINITION ( %s )\n", fieldname);
		
		/*********************************/
		/* Print to AST GRAPHVIZ DOT file */
		/*********************************/
		AstGraphviz.getInstance().logNode(
				serialNumber,
			String.format("ARRAYTYPEDEF( %s )", fieldname));
	}
}
