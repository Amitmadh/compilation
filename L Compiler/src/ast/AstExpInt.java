package ast;
import java.util.List;

import data.ClassData;
import data.FunctionData;
import ir.IRcommandConstInt;
import ir.Ir;
import temp.Temp;
import temp.TempFactory;
import types.*;
public class AstExpInt extends AstExp
{
	public int value;
	public Type type;

	//annotations
	FunctionData funcData = null;
	ClassData classData = null;

	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstExpInt(int value, int line)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		super(line);
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		// System.out.format("====================== exp -> INT( %d )\n", value);

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.value = value;
	}

	/************************************************/
	/* The printing message for an int exp AST node */
	/************************************************/
	public void printMe()
	{
		/*******************************/
		/* AST NODE TYPE = AST INT EXP */
		/*******************************/
		// System.out.format("AST NODE INT( %d )\n",value);

		/*********************************/
		/* Print to AST GRAPHVIZ DOT file */
		/*********************************/
		AstGraphviz.getInstance().logNode(
				serialNumber,
			String.format("EXP INT(%d)",value));
	}

	public Type semantMe() throws SemanticException
	{
		type = TypeInt.getInstance();
		return type;
	}

	public void setGlobalVarData(List<String> globalVars) {
		//do nothing
	}

	public void setFunctionData(FunctionData data) {
		funcData = data;
	}

	public void setClassData(ClassData data) {
		classData = data;
	}

	public Temp irMe()
	{
		Temp t = TempFactory.getInstance().getFreshTemp();
		Ir.getInstance().AddIrCommand(new IRcommandConstInt(t,value,funcData,classData));
		return t;
	}
}
