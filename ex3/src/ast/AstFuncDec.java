package ast;

public class AstFuncDec extends AstDec
{
	public AstType type;
    public String fieldName;
    public AstFuncArgList argList;
    public AstStmtList stmtList;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstFuncDec(AstType type, String fieldName, AstFuncArgList argList, AstStmtList stmtList)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== funcDec -> type ID LPAREN [ type ID { COMMA type ID } ] RPAREN LBRACE stmtList RBRACE\n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.type = type;
        this.fieldName = fieldName;
        this.argList = argList;
        this.stmtList = stmtList;
	}

	/************************************************/
	/* The printing message for an int exp AST node */
	/************************************************/
	public void printMe()
	{
		/*******************************/
		/* AST NODE TYPE = AST INT EXP */
		/*******************************/
		System.out.format("AST NODE FUNCDEC( %s )\n",fieldName);
        
        /****************************************/
		/* RECURSIVELY PRINT VAR + SUBSCRIPT ... */
		/****************************************/
		if (type != null) type.printMe();
		if (argList != null) argList.printMe();
        if (stmtList != null) stmtList.printMe();
        
		/*********************************/
		/* Print to AST GRAPHVIZ DOT file */
		/*********************************/
		AstGraphviz.getInstance().logNode(
				serialNumber,
			String.format("FUNCDEC( %s )",fieldName));

        /****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (type       != null) AstGraphviz.getInstance().logEdge(serialNumber,type.serialNumber);
		if (argList != null) AstGraphviz.getInstance().logEdge(serialNumber,argList.serialNumber);
        if (stmtList != null) AstGraphviz.getInstance().logEdge(serialNumber,stmtList.serialNumber);
	}
}
