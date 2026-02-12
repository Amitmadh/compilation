package ast;
import temp.Temp;
import types.*;
public class AstStmtVarDec extends AstStmt
{
	public AstVarDec varDec;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstStmtVarDec(AstVarDec varDec, int line)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		super(line);
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		// System.out.print("====================== stmt -> varDec\n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.varDec = varDec;
	}
	
	/***********************************************/
	/* The default message for an exp var AST node */
	/***********************************************/
	public void printMe()
	{
		/************************************/
		/* AST NODE TYPE = EXP VAR AST NODE */
		/************************************/
		// System.out.print("AST NODE STMT VARDEC\n");

		/*****************************/
		/* RECURSIVELY PRINT var ... */
		/*****************************/
		if (varDec != null) varDec.printMe();
		
		/*********************************/
		/* Print to AST GRAPHVIZ DOT file */
		/*********************************/
		AstGraphviz.getInstance().logNode(
				serialNumber,
			"STMT\nVARDEC");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AstGraphviz.getInstance().logEdge(serialNumber,varDec.serialNumber);
			
	}
	public Type semantMe() throws SemanticException
	{
		return varDec.semantMe();
	}

	public Temp irMe() { return varDec.irMe(); }
}
