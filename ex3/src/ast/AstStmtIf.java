package ast;
import symboltable.SymbolTable;
import types.*;
public class AstStmtIf extends AstStmt
{
	public AstExp cond;
	public AstStmtList body;
	public AstStmtList elseBody;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AstStmtIf(AstExp cond, AstStmtList body, AstStmtList elseBody)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		// System.out.print("====================== stmt -> IF LPAREN exp RPAREN LBRACE stmtList RBRACE [ ELSE LBRACE stmtList RBRACE ]\n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.cond = cond;
		this.body = body;
		this.elseBody = elseBody;
	}

	/*********************************************************/
	/* The printing message for an assign statement AST node */
	/*********************************************************/
	public void printMe()
	{
		/********************************************/
		/* AST NODE TYPE = AST ASSIGNMENT STATEMENT */
		/********************************************/
		// System.out.print("AST NODE IF STMT\n");

		/***********************************/
		/* RECURSIVELY PRINT VAR + EXP ... */
		/***********************************/
		if (cond != null) cond.printMe();
		if (body != null) body.printMe();
		if (elseBody != null) elseBody.printMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AstGraphviz.getInstance().logNode(
				serialNumber,
			"IF\nSTMT\n");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (cond != null) AstGraphviz.getInstance().logEdge(serialNumber,cond.serialNumber);
		if (body != null) AstGraphviz.getInstance().logEdge(serialNumber,body.serialNumber);
		if (elseBody != null) AstGraphviz.getInstance().logEdge(serialNumber,body.serialNumber);
	}
	public Type semantMe() throws SemanticException
	{
		/****************************/
		/* [1] Semant the cond exp */
		/****************************/
		Type condType = cond.semantMe();
		if (!condType.isInt())
		{
			throw new SemanticException(String.format(">> ERROR [%d:%d] IF condition is not boolean",0,0));
		}
		/*	[2] Begin scope */
		SymbolTable.getInstance().beginScope();
		/*******************************/
		/* [3] Semant the body stmtList */
		/*******************************/
		body.semantMe();
		SymbolTable.getInstance().endScope();
		/********************************/
		/* [4] Semant the else body stmtList */
		/********************************/
		if (elseBody != null) {
			SymbolTable.getInstance().beginScope();
			elseBody.semantMe();
			SymbolTable.getInstance().endScope();
		}
		return null;
	}
}