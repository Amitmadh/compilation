package ast;
import types.*;

import java.util.List;

import data.ClassData;
import data.FunctionData;
import temp.Temp;

public class AstExpVar extends AstExp
{
	public AstVar var;
	public Type type;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstExpVar(AstVar var, int line)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		super(line);
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		// System.out.print("====================== exp -> var\n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.var = var;
	}
	
	/***********************************************/
	/* The default message for an exp var AST node */
	/***********************************************/
	public void printMe()
	{
		/************************************/
		/* AST NODE TYPE = EXP VAR AST NODE */
		/************************************/
		// System.out.print("AST NODE EXP VAR\n");

		/*****************************/
		/* RECURSIVELY PRINT var ... */
		/*****************************/
		if (var != null) var.printMe();
		
		/*********************************/
		/* Print to AST GRAPHVIZ DOT file */
		/*********************************/
		AstGraphviz.getInstance().logNode(
				serialNumber,
			"EXP\nVAR");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AstGraphviz.getInstance().logEdge(serialNumber,var.serialNumber);
			
	}

	public Type semantMe() throws SemanticException
	{
		type = var.semantMe();
		return type;
	}

	public void setGlobalVarData(List<String> globalVars) {
		var.setGlobalVarData(globalVars);
	}

	public void setFunctionData(FunctionData data) {
		var.setFunctionData(data);
	}

	public void setClassData(ClassData data) {
		var.setClassData(data);
	}
	
	public Temp irMe()
	{
		return var.irMe();
	}
}
