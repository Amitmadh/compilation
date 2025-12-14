package ast;
import types.*;

public class AstCfieldList extends AstNode
{
	/****************/
	/* DATA MEMBERS */
	/****************/
	public AstCfield head;
	public AstCfieldList tail;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstCfieldList(AstCfield head, AstCfieldList tail)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		// if (tail != null) System.out.print("====================== cFields -> cField cFields\n");
		// if (tail == null) System.out.print("====================== cFields -> cField        \n");

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
		// System.out.print("AST NODE CFIELD LIST\n");

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
			"CFIELD\nLIST\n");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (head != null) AstGraphviz.getInstance().logEdge(serialNumber,head.serialNumber);
		if (tail != null) AstGraphviz.getInstance().logEdge(serialNumber,tail.serialNumber);
	}

	public TypeClassVarDecList semantMe() throws SemanticException
	{
		TypeClassVarDec headType = null;
		TypeClassVarDecList tailTypes = null;
		if (head == null) {
        	return null;
    	}

		/*******************************/
		/* [1] Semant head cfield ...  */
		/*******************************/
		if (head != null) headType = head.semantMe();

		/*******************************/
		/* [2] Semant tail cfieldList */
		/*******************************/
		if (tail != null) tailTypes = tail.semantMe();

		/*******************************/
		/* [3] Return the type list ... */
		/*******************************/
		return new TypeClassVarDecList(headType, tailTypes);
	}
	
}
