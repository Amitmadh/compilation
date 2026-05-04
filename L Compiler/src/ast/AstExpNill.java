package ast;
import ir.Ir;

import java.util.List;

import data.ClassData;
import data.FunctionData;
import ir.IRCommandNil;
import temp.Temp;
import temp.TempFactory;
import types.*;
public class AstExpNill extends AstExp
{
	public Type type;
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstExpNill(int line)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		super(line);
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		// System.out.format("====================== exp -> NILL\n");
	}

	/************************************************/
	/* The printing message for an int exp AST node */
	/************************************************/
	public void printMe()
	{
		/*******************************/
		/* AST NODE TYPE = AST INT EXP */
		/*******************************/
		// System.out.format("AST NODE NILL\n");

		/*********************************/
		/* Print to AST GRAPHVIZ DOT file */
		/*********************************/
		AstGraphviz.getInstance().logNode(
				serialNumber,
			String.format("NILL"));
	}
	
	public Type semantMe() throws SemanticException
	{
		type = TypeNil.getInstance();
		return type;
	}

	public void setGlobalVarData(List<String> globalVars) {
		//do nothing
	}

	public void setFunctionData(FunctionData data) {
		//do nothing
	}

	public void setClassData(ClassData data) {
		//do nothing
	}

	public Temp irMe()
	{
		Temp t = TempFactory.getInstance().getFreshTemp();
		Ir.getInstance().AddIrCommand(new IRCommandNil(t));
		return t;
	}
}
