package ast;
import types.*;
import data.ClassData;
import data.FunctionData;
import ir.Ir;
import temp.Temp;
public class AstFuncArgList extends AstNode
{
	/****************/
	/* DATA MEMBERS */
	/****************/
	public AstFuncArg head;
	public AstFuncArgList tail;

	//annotations
	public String funcName;

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

	public void annotateAst()
	{
		if (tail != null && tail.head != null) {
			tail.head.index = head.index + 1;
			tail.funcName = funcName;
			head.funcName = funcName;
			tail.annotateAst();
		}
	}

	public void setFunctionData(FunctionData data) {
		head.setFunctionData(data);
		if (tail != null) {
			tail.setFunctionData(data);
		}
	}

	public void setClassData(ClassData data) {
		head.setClassData(data);
		if (tail != null) {
			tail.setClassData(data);
		}
	}

	public Temp irMe()
	{
		head.irMe();
		if (tail != null) tail.irMe();
		return null;
	}
}
