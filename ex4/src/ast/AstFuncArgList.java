package ast;
import temp.Temp;
import types.*;
public class AstFuncArgList extends AstNode
{
	/****************/
	/* DATA MEMBERS */
	/****************/
	public AstFuncArg head;
	public AstFuncArgList tail;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstFuncArgList(AstFuncArg head, AstFuncArgList tail, int line)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		super(line);
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		// if (tail != null) System.out.print("====================== funcArgs -> funcArg funcArgs	\n");
		// if (tail == null) System.out.print("====================== funcArgs -> funcArg			\n");

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
		// System.out.print("AST NODE ARG LIST\n");

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
			"FUNC\nARG\nLIST\n");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (head != null) AstGraphviz.getInstance().logEdge(serialNumber,head.serialNumber);
		if (tail != null) AstGraphviz.getInstance().logEdge(serialNumber,tail.serialNumber);
	}
	public TypeList semantMe() throws SemanticException
	{
		TypeList tailTypes = null;
		Type headType = null;

		if (head == null) {
        	return null;
    	}

		/*******************************/
		/* [1] Semant head arg ...  */
		/*******************************/
		headType = head.semantMe();

		/*******************************/
		/* [2] Semant tail argList */
		/*******************************/
		if (tail != null) tailTypes = tail.semantMe();
		

		return new TypeList(headType, tailTypes);
	}
	public Temp irMe()
	{
		if (head != null) head.irMe();
		if (tail != null) tail.irMe();

		return null;
	}
	
}
