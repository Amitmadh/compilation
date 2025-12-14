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
			throw new SemanticException(String.format(">> ERROR [%d:%d] variable %s cannot be of type void",2,2,type.name));
		}
		/****************************/
		/* [2] Check If Type exists */
		/****************************/
		t = SymbolTable.getInstance().find(type.name);
		if (t == null)
		{
			throw new SemanticException(String.format(">> ERROR [%d:%d] non existing type %s",2,2,type));
		}
		
		if (t.isArray()){
			TypeArray array_type = (TypeArray) t;
			if (exp == null){
				throw new SemanticException(String.format(">> ERROR [%d:%d] must provide array size expression",2,2));
			}
			/****************************/
			/* [3] Semant the exp */
			/****************************/
			Type expType = exp.semantMe();
			if (!expType.isInt()){
				throw new SemanticException(String.format(">> ERROR [%d:%d] array size expression must be of type int",2,2));
			}
			if (exp instanceof AstExpInt) {
				int v = ((AstExpInt)exp).value;
				if (v <= 0) {
					throw new SemanticException(String.format(">> ERROR: array size must be positive constant"));
				}
			}
			return array_type;
		} 
		else  if (t.isClass()){
			if (exp != null){
				throw new SemanticException(String.format(">> ERROR [%d:%d] only array types can have size expression",2,2));
			}
			return t;
		}
		else{
			throw new SemanticException(String.format(">> ERROR [%d:%d] only class or array types can be instantiated",2,2));
			// System.out.format(">> ERROR [%d:%d] only class or array types can be instantiated\n",2,2);
			// System.exit(0);
			// return null;
		}
		
	}
	
}
