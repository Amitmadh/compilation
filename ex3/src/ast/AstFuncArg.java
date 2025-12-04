package ast;

public class AstFuncArg extends AstNode
{
	AstType type;
    String fieldName;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstFuncArg(AstType type, String fieldName)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== funcArg -> type ID(%s)\n", fieldName);

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.type = type;
        this.fieldName = fieldName;
	}

	/************************************************/
	/* The printing message for an int exp AST node */
	/************************************************/
	public void printMe()
	{
		/*******************************/
		/* AST NODE TYPE = AST INT EXP */
		/*******************************/
		System.out.format("AST NODE FUNC ARGUMENT(%s)\n", fieldName);

		/****************************************/
		/* RECURSIVELY PRINT VAR + SUBSCRIPT ... */
		/****************************************/
		if (type != null) type.printMe();

		/*********************************/
		/* Print to AST GRAPHVIZ DOT file */
		/*********************************/
		AstGraphviz.getInstance().logNode(
				serialNumber,
			String.format("ARG ID(%s)", fieldName));

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (type != null) AstGraphviz.getInstance().logEdge(serialNumber,type.serialNumber);
	}
}
