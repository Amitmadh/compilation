package ast;

public class AstType extends AstNode {
    public int type;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstType(int type)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== type -> TYPE\n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.type = type;
	}

	/************************************************/
	/* The printing message for an int exp AST node */
	/************************************************/
	public void printMe()
	{
        String stype="";
		
		/*********************************/
		/* CONVERT op to a printable sop */
		/*********************************/
		if (type == 0) {stype = "TYPE_INT";}
		if (type == 1) {stype = "TYPE_STRING";}
        if (type == 2) {stype = "TYPE_VOID";}
        if (type == 3) {stype = "ID";}
		/*******************************/
		/* AST NODE TYPE = AST INT EXP */
		/*******************************/
		System.out.format("AST NODE TYPE\n");

		/*********************************/
		/* Print to AST GRAPHVIZ DOT file */
		/*********************************/
		AstGraphviz.getInstance().logNode(
				serialNumber,
			String.format("TYPE(%s)",stype));
	}
}
