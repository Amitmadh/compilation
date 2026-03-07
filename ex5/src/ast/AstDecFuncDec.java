package ast;
import java.util.List;

import data.FunctionData;
import temp.Temp;
import types.TypeClass;

public class AstDecFuncDec extends AstDec
{
	public AstFuncDec funcDec;

	//annotation
	public String funcName;
	public List<String> args;
	public List<String> localVars;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstDecFuncDec(AstFuncDec funcDec, int line)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		super(line);
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		// System.out.print("====================== dec -> funcDec\n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.funcDec = funcDec;
	}

	/************************************************/
	/* The printing message for an int exp AST node */
	/************************************************/
	public void printMe()
	{
		/*******************************/
		/* AST NODE TYPE = AST INT EXP */
		/*******************************/
		// System.out.print("AST NODE DEC\n");

        /**************************************/
		/* RECURSIVELY PRINT left + right ... */
		/**************************************/
		if (funcDec != null) funcDec.printMe();

		/*********************************/
		/* Print to AST GRAPHVIZ DOT file */
		/*********************************/
		AstGraphviz.getInstance().logNode(
				serialNumber,
			String.format("DEC"));

        /****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (funcDec  != null) AstGraphviz.getInstance().logEdge(serialNumber,funcDec.serialNumber);
	}

	public void semantMe(TypeClass classType) throws SemanticException {
		if (funcDec != null) funcDec.semantMe(classType);
	}

	public void annotateAst()
	{
		funcDec.annotateAst();
		
		this.funcName = funcDec.fieldName;
		this.args = funcDec.args;
		this.localVars = funcDec.localVars;
	}

	public void setGlobalVarData(List<String> globalVars) {
		funcDec.setGlobalVarData(globalVars);
	}

	public void setFunctionData(FunctionData data) {
		funcDec.setFunctionData(data);
	}

	public Temp irMe()
	{
		if (funcDec != null) return funcDec.irMe();
		return null;
	}
}
