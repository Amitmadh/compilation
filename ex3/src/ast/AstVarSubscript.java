package ast;
import types.*;
public class AstVarSubscript extends AstVar
{
	public AstVar var;
	public AstExp subscript;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstVarSubscript(AstVar var, AstExp subscript, int line)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		super(line);
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		// System.out.print("====================== var -> var [ exp ]\n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.var = var;
		this.subscript = subscript;
	}

	/*****************************************************/
	/* The printing message for a subscript var AST node */
	/*****************************************************/
	public void printMe()
	{
		/*************************************/
		/* AST NODE TYPE = AST SUBSCRIPT VAR */
		/*************************************/
		// System.out.print("AST NODE SUBSCRIPT VAR\n");

		/****************************************/
		/* RECURSIVELY PRINT VAR + SUBSCRIPT ... */
		/****************************************/
		if (var != null) var.printMe();
		if (subscript != null) subscript.printMe();
		
		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AstGraphviz.getInstance().logNode(
				serialNumber,
			"SUBSCRIPT\nVAR");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (var != null) AstGraphviz.getInstance().logEdge(serialNumber,var.serialNumber);
		if (subscript != null) AstGraphviz.getInstance().logEdge(serialNumber,subscript.serialNumber);
	}

	public Type semantMe() throws SemanticException
	{
		/* 1. Check that the var is an array */
		Type varType = var.semantMe();
		if (!(varType.isArray()))
		{
			throw new SemanticException(String.format(">> ERROR [%d:%d] subscripted variable is not an array",2,2));
		}
		
		/* 2. Check that the subscript is an int */
		Type subscriptType = subscript.semantMe();
		if (!(subscriptType.isInt()))
		{
			throw new SemanticException(String.format(">> ERROR [%d:%d] array subscript is not an integer",2,2));
		}
		if (subscript instanceof AstExpInt) {
				int v = ((AstExpInt)subscript).value;
				if (v < 0) {
					throw new SemanticException(String.format(">> ERROR: constant subscript must be greater than or equal to zero"));
				}
			}
		
		/* 3. Return the array element type */
		return ((TypeArray)varType).elemType;
	}
}
