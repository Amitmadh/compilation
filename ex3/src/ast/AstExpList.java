package ast;

import types.*;

public class AstExpList extends AstNode
{
	/****************/
	/* DATA MEMBERS */
	/****************/
	public AstExp head;
	public AstExpList tail;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstExpList(AstExp head, AstExpList tail)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		// if (tail != null) System.out.print("====================== exps -> exp exps \n");
		// if (tail == null) System.out.print("====================== exps -> exp      \n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.head = head;
		this.tail = tail;
	}

	/******************************************************/
	/* The printing message for a statement list AST node */
	/******************************************************/
	public void printMe()
	{
		/**************************************/
		/* AST NODE TYPE = AST STATEMENT LIST */
		/**************************************/
		// System.out.print("AST NODE EXP LIST\n");

		/*************************************/
		/* RECURSIVELY PRINT HEAD + TAIL ... */
		/*************************************/
		if (head != null) head.printMe();
		if (tail != null) tail.printMe();

		/**********************************/
		/* PRINT to AST GRAPHVIZ DOT file */
		/**********************************/
		AstGraphviz.getInstance().logNode(
				serialNumber,
			"EXP\nLIST\n");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (head != null) AstGraphviz.getInstance().logEdge(serialNumber,head.serialNumber);
		if (tail != null) AstGraphviz.getInstance().logEdge(serialNumber,tail.serialNumber);
	}
	public TypeList semantMe()
	{
		TypeList tailTypes = null;
		Type headType = null;

		if (head == null) {
        	return null;
    	}

		/*******************************/
		/* [1] Semant head exp ...  */
		/*******************************/
		headType = head.semantMe();

		/*******************************/
		/* [2] Semant tail expList */
		/*******************************/
		if (tail != null) tailTypes = tail.semantMe();
		

		return new TypeList(headType, tailTypes);
	}
}
