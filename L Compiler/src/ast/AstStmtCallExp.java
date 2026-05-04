package ast;
import java.util.ArrayList;
import java.util.List;

import data.ClassData;
import data.FunctionData;
import temp.Temp;
import types.*;
public class AstStmtCallExp extends AstStmt
{
	public AstCallExp callExp;


	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstStmtCallExp(AstCallExp callExp, int line)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		super(line);
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		// System.out.print("====================== stmt -> callExp SEMICOLON\n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.callExp = callExp;
	}
	
	/***********************************************/
	/* The default message for an exp var AST node */
	/***********************************************/
	public void printMe()
	{
		/************************************/
		/* AST NODE TYPE = EXP VAR AST NODE */
		/************************************/
		// System.out.print("AST NODE STMT CALLEXP\n");

		/*****************************/
		/* RECURSIVELY PRINT var ... */
		/*****************************/
		if (callExp != null) callExp.printMe();
		
		/*********************************/
		/* Print to AST GRAPHVIZ DOT file */
		/*********************************/
		AstGraphviz.getInstance().logNode(
				serialNumber,
			"STMT\nCALLEXP");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AstGraphviz.getInstance().logEdge(serialNumber,callExp.serialNumber);
			
	}

	public Type semantMe() throws SemanticException
	{
		return callExp.semantMe();
	}

	public void annotateAst()
	{
		varDecs = new ArrayList<>();
	}

	public void setGlobalVarData(List<String> globalVars) {
		callExp.setGlobalVarData(globalVars);
	}

	public void setFunctionData(FunctionData data) {
		callExp.setFunctionData(data);
	}

	public void setClassData(ClassData data) {
		callExp.setClassData(data);
	}

	public Temp irMe()
	{
		if (callExp != null) 
		{
			Temp t = callExp.irMe();
			return t;
		}
		else {
			return null;
		}
	}
}
