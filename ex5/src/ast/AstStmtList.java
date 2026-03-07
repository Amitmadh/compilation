package ast;
import java.util.ArrayList;
import java.util.List;

import data.ClassData;
import data.FunctionData;
import temp.Temp;
import types.*;
public class AstStmtList extends AstNode
{
	/****************/
	/* DATA MEMBERS */
	/****************/
	public AstStmt head;
	public AstStmtList tail;

	//annotations
	List<String> varDecs;
	String funcName;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstStmtList(AstStmt head, AstStmtList tail, int line)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		super(line);
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		// if (tail != null) System.out.print("====================== stmts -> stmt stmts\n");
		// if (tail == null) System.out.print("====================== stmts -> stmt      \n");

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
		// System.out.print("AST NODE STMT LIST\n");

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
			"STMT\nLIST\n");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (head != null) AstGraphviz.getInstance().logEdge(serialNumber,head.serialNumber);
		if (tail != null) AstGraphviz.getInstance().logEdge(serialNumber,tail.serialNumber);
	}
	public TypeList semantMe() throws SemanticException
	{
		if (head == null) {
        	return null;
    	}

		/*******************************/
		/* [1] Semant head stmt ...  */
		/*******************************/
		head.semantMe();

		/*******************************/
		/* [2] Semant tail stmtList */
		/*******************************/
		if (tail != null) tail.semantMe();

		return null;
	}

	public void annotateAst()
	{
		AstStmt curr = head;
		AstStmtList nextStmt = tail;

		varDecs = new ArrayList<>();

		while (curr != null) {
			curr.annotateAst();
			varDecs.addAll(curr.varDecs);
			
			if (nextStmt != null) {
				curr = nextStmt.head;
				nextStmt = nextStmt.tail;
			} else {
				break;
			}
		}
	}

	public void setGlobalVarData(List<String> globalVars) {
		head.setGlobalVarData(globalVars);
		if (tail != null) {
			tail.setGlobalVarData(globalVars);
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
		if (head != null) head.irMe();
		if (tail != null) tail.irMe();

		return null;
	}
}
