package ast;

public class AstType extends AstNode {
	
    public int type;
	public String name;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstType(int type, String name)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== type -> TYPE INT | TYPE STRING | TYPE VOID | ID\n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.type = type;
		if (type == 0) {
			this.name = "int";
		}
		else if (type == 1) {
			this.name = "string";
		}
		else if (type == 2) {
			this.name = "void";
		}
		else { // type == 3 (ID)
			this.name = name;
		}
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
        if (type == 3) {stype = name;}
		/*******************************/
		/* AST NODE TYPE = AST INT EXP */
		/*******************************/
		System.out.format("AST NODE TYPE(%s)\n", stype);

		/*********************************/
		/* Print to AST GRAPHVIZ DOT file */
		/*********************************/
		AstGraphviz.getInstance().logNode(
				serialNumber,
			String.format("TYPE(%s)",stype));
	}
}
