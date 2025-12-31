package ast;
import types.*;

import java.util.ArrayList;
import java.util.List;

import ir.Ir;
import ir.IrCommandCallMethod;
import ir.IrCommandCallFunc;
import symboltable.*;
import temp.Temp;
import temp.TempFactory;

public class AstCallExp extends AstNode
{
	AstVar var;
    String fieldName;
    AstExpList expList;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstCallExp(AstVar var, String fieldName, AstExpList expList, int line)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		super(line);
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		// System.out.format("====================== callExp -> [ var DOT ] ID( %s ) LPAREN [ exp { COMMA exp } ] RPAREN\n", fieldName);

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.var = var;
        this.fieldName = fieldName;
        this.expList = expList;
	}

	/************************************************/
	/* The printing message for an int exp AST node */
	/************************************************/
	public void printMe()
	{
		/*******************************/
		/* AST NODE TYPE = AST INT EXP */
		/*******************************/
		// System.out.format("AST NODE CALL EXPRESSION(%s)\n",fieldName);

        /****************************************/
		/* RECURSIVELY PRINT VAR + SUBSCRIPT ... */
		/****************************************/
		if (var != null) var.printMe();
		if (expList != null) expList.printMe();

		/*********************************/
		/* Print to AST GRAPHVIZ DOT file */
		/*********************************/
		AstGraphviz.getInstance().logNode(
				serialNumber,
			String.format("ID(%s)",fieldName));

        /****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (var != null) AstGraphviz.getInstance().logEdge(serialNumber,var.serialNumber);
		if (expList != null) AstGraphviz.getInstance().logEdge(serialNumber,expList.serialNumber);
	}
	public Type semantMe() throws SemanticException
	{
		Type varType = null;
		Type funcType = null;
		/******************************/
		/* retrieve function type */
		/******************************/
		if (var != null)
		{
			varType = var.semantMe();

			if (varType == null)
			{
				System.out.printf("ERROR at line %d, calling method %s on a non-existing variable\n", line, fieldName);
				throw new SemanticException(String.format("ERROR(%d)",line));
			}
			/* Check that the var is instance of class */
			if (!(varType.isClass()))
			{
				System.out.printf("ERROR at line %d, var must have class type\n", line);
				throw new SemanticException(String.format("ERROR(%d)",line));
			}
			
			/* Find func fieldName in class data members (including ancestors)*/
			TypeClass classType = (TypeClass) varType;	
			TypeClass currentClass = classType;
			while (currentClass != null && funcType == null) 
			{
				TypeClassVarDecList dataMembers = currentClass.dataMembers;
				while (dataMembers != null) 
				{
					if (dataMembers.head.name.equals(fieldName)) {
						funcType =  dataMembers.head.type;
						break;
					}
					dataMembers = dataMembers.tail;
				}
				currentClass = currentClass.father;
			}
		
		}
		else{	
			/* Lookup function name in symbol table */
			funcType = SymbolTable.getInstance().find(fieldName);
		}
		/* Check that funcType is indeed a function */
		if (funcType == null || !funcType.isFunction())
			{
				System.out.printf("ERROR at line %d, call to undefined function %s\n", line, fieldName);
				throw new SemanticException(String.format("ERROR(%d)",line));
			}
		TypeFunction funcTypeFunction = (TypeFunction)funcType;

		/***************************************/
		/* Semant the expList object */
		/***************************************/
		TypeList expTypes = null;
		if (expList != null)
		{
			expTypes = expList.semantMe();
		}


		/***********************************************/
		/* Check that the types of the arguments match the function's parameters */
		/***********************************************/
		TypeList paramTypes = funcTypeFunction.params;
		TypeList currExpType = expTypes;
		Type paramType = null;
        Type expType = null;
		boolean match = false;
		while (paramTypes != null && currExpType != null)
		{
			paramType = paramTypes.head;
        	expType = currExpType.head;
			if (paramType.name.equals(expType.name))
			{
				match = true;
			}
			/*Handle Nil */
			else if (expType instanceof TypeNil || "nil".equals(expType.name))
			{
				if (paramType.isClass() || paramType.isArray())
				{
					match = true;
				}
			}
			else if (paramType.isClass() && expType.isClass())
			{
				TypeClass paramClass = (TypeClass) paramType;
				TypeClass expClass = (TypeClass) expType;
				if (expClass.isSubClassOf(paramClass))
				{
					match = true;
				}
			}
			if (!match)
			{
				System.out.printf("ERROR at line %d, function %s argument type mismatch. Expected %s, got %s\n", line, fieldName, paramType.name, expType.name);
				throw new SemanticException(String.format("ERROR(%d)",line));
			}
			paramTypes = paramTypes.tail;
			currExpType = currExpType.tail;
			match = false;
		}
		if (paramTypes != null || currExpType != null)
		{
			System.out.printf("ERROR at line %d, function %s argument count mismatch\n", line, fieldName);
			throw new SemanticException(String.format("ERROR(%d)",line));
		}
		return funcTypeFunction.returnType;
	}

	public Temp irMe()
	{
		Temp returnValue = TempFactory.getInstance().getFreshTemp();

		List<Temp> temps = new ArrayList<>();
		AstExpList curr_arg = expList;
		while (curr_arg != null)
		{
			Temp argTemp = curr_arg.head.irMe();
			temps.add(argTemp);
			curr_arg = curr_arg.tail;
		}

		if (var != null) {
			Temp varBase = var.irMe();
			Ir.getInstance().AddIrCommand(new IrCommandCallMethod(varBase, fieldName, temps));
		} else {
			Ir.getInstance().AddIrCommand(new IrCommandCallFunc(fieldName, temps));
		}

		return returnValue;
	}
}
