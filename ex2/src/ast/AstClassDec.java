package ast;

public class AstClassDec extends AstDec
{
    public String className;
    public String extendName;
    public AstCfieldList cfieldList;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstClassDec(String className, String extendName, AstCfieldList cfieldList)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== classDec -> CLASS ID [ EXTENDS ID ] LBRACE cField { cField } RBRACE\n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.className = className;
        this.extendName = extendName;
        this.cfieldList = cfieldList;
	}

	/************************************************/
	/* The printing message for an int exp AST node */
	/************************************************/
	public void printMe()
	{
		/*******************************/
		/* AST NODE TYPE = AST INT EXP */
		/*******************************/
		System.out.format("AST NODE CLASSDEC( %s )",className);
        if (extendName != null) System.out.format(" EXTENDES( %s )",extendName);;
        System.out.format("\n");

        /****************************************/
		/* RECURSIVELY PRINT VAR + SUBSCRIPT ... */
		/****************************************/
		if (cfieldList != null) cfieldList.printMe();
        
		/*********************************/
		/* Print to AST GRAPHVIZ DOT file */
		/*********************************/
		AstGraphviz.getInstance().logNode(
				serialNumber,
			String.format("CLASSDEC( %s )",className));

        /****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (cfieldList != null) AstGraphviz.getInstance().logEdge(serialNumber,cfieldList.serialNumber);
	}
}
