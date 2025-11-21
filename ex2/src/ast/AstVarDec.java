package ast;

public class AstVarDec extends AstDec
{
	public AstType type;
    public String fieldName;
    public AstExp exp;
    public AstNewExp nexp;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstVarDec(AstType type, String fieldName, AstExp exp, AstNewExp nexp)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== varDec -> type ID [ASSIGN (exp | newExp)] SEMICOLON\n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.type = type;
        this.fieldName = fieldName;
        this.exp = exp;
        this.nexp = nexp;
	}

	/************************************************/
	/* The printing message for an int exp AST node */
	/************************************************/
	public void printMe()
	{
		/*******************************/
		/* AST NODE TYPE = AST INT EXP */
		/*******************************/
		System.out.format("AST NODE VARDEC( %s )\n",fieldName);
        
        /****************************************/
		/* RECURSIVELY PRINT VAR + SUBSCRIPT ... */
		/****************************************/
		if (type != null) type.printMe();
		if (exp != null) exp.printMe();
        if (nexp != null) nexp.printMe();
        
		/*********************************/
		/* Print to AST GRAPHVIZ DOT file */
		/*********************************/
		AstGraphviz.getInstance().logNode(
				serialNumber,
			String.format("VAEDEC( %s )",fieldName));

        /****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (type       != null) AstGraphviz.getInstance().logEdge(serialNumber,type.serialNumber);
		if (exp != null) AstGraphviz.getInstance().logEdge(serialNumber,exp.serialNumber);
        if (nexp != null) AstGraphviz.getInstance().logEdge(serialNumber,nexp.serialNumber);
	}
}
