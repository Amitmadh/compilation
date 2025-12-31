package ast;

import types.*;
import ir.Ir;
import ir.IrCommandNewArray;
import ir.IrCommandNewObject;
import symboltable.*;
import temp.Temp;
import temp.TempFactory;

public class AstNewExp extends AstExp
{
	AstType type;
	public AstExp exp;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstNewExp(AstType type, AstExp exp, int line)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		super(line);
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		// System.out.print("====================== newExp -> NEW type [exp]\n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.type = type;
		this.exp = exp;
	}
	
	/*************************************************/
	/* The printing message for a binop exp AST node */
	/*************************************************/
	public void printMe()
	{	
		/*************************************/
		/* AST NODE TYPE = AST BINOP EXP */
		/*************************************/
		// System.out.print("AST NODE NEW EXP\n");

		/**************************************/
		/* RECURSIVELY PRINT left + right ... */
		/**************************************/
		if (type != null) type.printMe();
		if (exp != null) exp.printMe();
		
		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AstGraphviz.getInstance().logNode(
				serialNumber,
			String.format("NEWEXP"));
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (type  != null) AstGraphviz.getInstance().logEdge(serialNumber,type.serialNumber);
		if (exp  != null) AstGraphviz.getInstance().logEdge(serialNumber,exp.serialNumber);
	}

	public Type semantMe() throws SemanticException
	{
		Type t;
		/****************************/
		/* [1] Make sure variable is not of type void */
		/****************************/
		if ("void".equals(type.name)){
			System.out.printf("ERROR at line %d, variable %s cannot be of type void\n", line, type.name);
			throw new SemanticException(String.format("ERROR(%d)",line));
		}
		/****************************/
		/* [2] Check If Type exists */
		/****************************/
		t = SymbolTable.getInstance().find(type.name);
		if (t == null)
		{
			System.out.printf("ERROR at line %d, non existing type %s\n", line, type.name);
			throw new SemanticException(String.format("ERROR(%d)",line));
		}
		
		if (exp != null){
			// that means we expect an array type
			TypeArray array_type = new TypeArray (null, t);
			// if (exp == null){
			// 	System.out.printf("ERROR at line %d, must provide array size expression\n", line);
			// 	throw new SemanticException(String.format("ERROR(%d)",line));
			// }
			/****************************/
			/* [3] Semant the exp */
			/****************************/
			Type expType = exp.semantMe();
			if (!expType.isInt()){
				System.out.printf("ERROR at line %d, array size expression must be of type int\n", line);
				throw new SemanticException(String.format("ERROR(%d)",line));
			}
			if (exp instanceof AstExpInt) {
				int v = ((AstExpInt)exp).value;
				if (v <= 0) {
					System.out.printf("ERROR at line %d, array size must be positive constant\n", line);
					throw new SemanticException(String.format("ERROR(%d)",line));
				}
			}
			return array_type;
		} 
		else  if (t.isClass()){
			if (exp != null){
				System.out.printf("ERROR at line %d, only array types can have size expression\n", line);
				throw new SemanticException(String.format("ERROR(%d)",line));
			}
			return t;
		}
		else{
			System.out.printf("ERROR at line %d, only class or array types can be instantiated\n", line);
			throw new SemanticException(String.format("ERROR(%d)",line));
		}
		
	}

	public Temp irMe()
	{
		Temp varName = TempFactory.getInstance().getFreshTemp();
		if (exp != null){
			// array allocation
			Temp exp_temp = exp.irMe();
			Ir.getInstance().AddIrCommand(new IrCommandNewArray(varName, exp_temp, type.name));
		} 
		else {
			// class allocation
			Ir.getInstance().AddIrCommand(new IrCommandNewObject(varName, type.name));
		}
		return varName;
	}
	
}
