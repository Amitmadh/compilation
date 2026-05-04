package ast;
import java.util.List;

import data.ClassData;
import data.FunctionData;
import ir.IRcommandConstString;
import ir.Ir;
import temp.Temp;
import temp.TempFactory;
import types.*;
public class AstExpString extends AstExp
{
	public String value;
	public Type type;

	//annotations
	FunctionData funcData = null;
	ClassData classData = null;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstExpString(String value, int line)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		super(line);
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		// System.out.format("====================== exp -> STRING( %s )\n", value);

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
		// System.out.format("AST NODE STRING( %s )\n",value);

		/*********************************/
		/* Print to AST GRAPHVIZ DOT file */
		/*********************************/
		AstGraphviz.getInstance().logNode(
				serialNumber,
			String.format("STRING(%s)",value));
	}

	public Type semantMe() throws SemanticException
	{
		type = TypeString.getInstance();
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
		Ir.getInstance().AddIrCommand(new IRcommandConstString(t,value, funcData, classData));
		return t;
	}
}
