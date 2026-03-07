package ast;

import types.*;

import java.util.ArrayList;
import java.util.List;

import data.ClassData;
import data.FunctionData;
import ir.IRcommandConstInt;
import ir.Ir;
import temp.Temp;
import temp.TempFactory;

public class AstExpList extends AstNode
{
	/****************/
	/* DATA MEMBERS */
	/****************/
	public AstExp head;
	public AstExpList tail;

	//annotations
	public String funcName;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstExpList(AstExp head, AstExpList tail, int line)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		super(line);
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

	public TypeList semantMe() throws SemanticException
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
		head.irMe();
		tail.irMe();
		return null;
	}
}
