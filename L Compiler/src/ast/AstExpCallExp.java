package ast;
import types.*;

import java.util.List;

import data.ClassData;
import data.FunctionData;
import temp.Temp;

public class AstExpCallExp extends AstExp
{
	public AstCallExp callExp;
	public Type type;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstExpCallExp(AstCallExp callExp, int line)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		super(line);
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		// System.out.format("====================== exp -> callExp\n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.callExp = callExp;
	}

	/************************************************/
	/* The printing message for an int exp AST node */
	/************************************************/
	public void printMe()
	{
		/*******************************/
		/* AST NODE TYPE = AST INT EXP */
		/*******************************/
		// System.out.format("AST NODE CALLEXP\n");

        /**************************************/
		/* RECURSIVELY PRINT left + right ... */
		/**************************************/
		if (callExp != null) callExp.printMe();

		/*********************************/
		/* Print to AST GRAPHVIZ DOT file */
		/*********************************/
		AstGraphviz.getInstance().logNode(
				serialNumber,
			String.format("CALLEXP"));

        /****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (callExp  != null) AstGraphviz.getInstance().logEdge(serialNumber,callExp.serialNumber);
	}

	public Type semantMe() throws SemanticException
	{
		type = callExp.semantMe();
		return type;
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
		return callExp.irMe();
	}
}
