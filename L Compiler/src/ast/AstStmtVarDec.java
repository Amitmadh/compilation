package ast;
import java.util.ArrayList;
import java.util.List;

import data.ClassData;
import data.FunctionData;
import temp.Temp;
import types.*;
public class AstStmtVarDec extends AstStmt
{
	public AstVarDec varDec;

	//annotations
	String varName;
	int offset;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstStmtVarDec(AstVarDec varDec, int line)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		super(line);
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		// System.out.print("====================== stmt -> varDec\n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.varDec = varDec;
	}
	
	/***********************************************/
	/* The default message for an exp var AST node */
	/***********************************************/
	public void printMe()
	{
		/************************************/
		/* AST NODE TYPE = EXP VAR AST NODE */
		/************************************/
		// System.out.print("AST NODE STMT VARDEC\n");

		/*****************************/
		/* RECURSIVELY PRINT var ... */
		/*****************************/
		if (varDec != null) varDec.printMe();
		
		/*********************************/
		/* Print to AST GRAPHVIZ DOT file */
		/*********************************/
		AstGraphviz.getInstance().logNode(
				serialNumber,
			"STMT\nVARDEC");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AstGraphviz.getInstance().logEdge(serialNumber,varDec.serialNumber);
			
	}

	public Type semantMe() throws SemanticException
	{
		return varDec.semantMe();
	}

	public void annotateAst()
	{
		varName = varDec.fieldName;
		offset = varDec.offset;

		varDecs = new ArrayList<>();
		varDecs.add(varName + "offset" + offset);
	}

	public void setGlobalVarData(List<String> globalVars) {
		varDec.setGlobalVarData(globalVars);
	}

	public void setFunctionData(FunctionData data) {
		varDec.setFunctionData(data);
	}

	public void setClassData(ClassData data) {
		varDec.setClassData(data);
	}

	public Temp irMe() 
	{
		return varDec.irMe();
	}
}
