package ast;

import types.*;
import symboltable.*;

public class AstNewExp extends AstExp
{
	AstType type;
	public AstExp exp;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstNewExp(AstType type, AstExp exp)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== newExp -> NEW type [exp]\n");

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
		System.out.print("AST NODE NEW EXP\n");

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

	public Type semantMe()
	{
		Type t;
		/****************************/
		/* [1] Make sure variable is not of type void */
		/****************************/
		if ("void".equals(type.name)){
			System.out.format(">> ERROR [%d:%d] variable %s cannot be of type void\n",2,2,fieldName);
			System.exit(0);
		}
		/****************************/
		/* [2] Check If Type exists */
		/****************************/
		t = SymbolTable.getInstance().find(type.name);
		if (t == null)
		{
			System.out.format(">> ERROR [%d:%d] non existing type %s\n",2,2,type);
			System.exit(0);
		}
		
		if (t.isArray()){
			TypeArray array_type = (TypeArray) t;
			if (exp == null){
				System.out.format(">> ERROR [%d:%d] must provide array size expression\n",2,2);
				System.exit(0);
			}
			/****************************/
			/* [3] Semant the exp */
			/****************************/
			Type expType = exp.semantMe();
			if (!expType.isInt()){
				System.out.format(">> ERROR [%d:%d] array size expression must be of type int\n",2,2);
				System.exit(0);
			}
			return array_type;

		} 
		else {
			if (exp != null){
				System.out.format(">> ERROR [%d:%d] only array types can have size expression\n",2,2);
				System.exit(0);
			}
			return t;
		}
		
	}
}
