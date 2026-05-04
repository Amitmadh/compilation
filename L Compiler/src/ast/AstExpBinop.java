package ast;
import java.util.List;

import data.ClassData;
import data.FunctionData;
import ir.IRcommandConstInt;
import ir.Ir;
import ir.IrCommandBinopAddIntegers;
import ir.IrCommandBinopEqIntegers;
import ir.IrCommandBinopEqStrings;
import ir.IrCommandBinopLtIntegers;
import ir.IrCommandBinopMulIntegers;
import ir.IrCommandBinopDivIntegers;
import ir.IrCommandBinopSubIntegers;
import ir.IrCommandBinopGtIntegers;
import ir.IrCommandBinopAddStrings;
import ir.IrCommandBinopEqClasses;
import ir.IrCommandBinopEqArrays;
import temp.Temp;
import temp.TempFactory;
import types.*;

public class AstExpBinop extends AstExp
{
	int op;
	public AstExp left;
	public AstExp right;
	public Type type;

	Type type_left;
	Type type_right;

	//annotations
	FunctionData funcData = null;
	ClassData classData = null;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstExpBinop(AstExp left, AstExp right, int op, int line)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		super(line);
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		// System.out.print("====================== exp -> exp BINOP exp\n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.left = left;
		this.right = right;
		this.op = op;
	}
	
	/*************************************************/
	/* The printing message for a binop exp AST node */
	/*************************************************/
	public void printMe()
	{
		String sop="";
		
		/*********************************/
		/* CONVERT op to a printable sop */
		/*********************************/
		if (op == 0) {sop = "+";}
		if (op == 1) {sop = "-";}
		if (op == 2) {sop = "*";}
		if (op == 3) {sop = "/";}
		if (op == 4) {sop = "<";}
		if (op == 5) {sop = ">";}
		if (op == 6) {sop = "=";}
		
		/*************************************/
		/* AST NODE TYPE = AST BINOP EXP */
		/*************************************/
		// System.out.print("AST NODE BINOP EXP\n");

		/**************************************/
		/* RECURSIVELY PRINT left + right ... */
		/**************************************/
		if (left != null) left.printMe();
		if (right != null) right.printMe();
		
		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AstGraphviz.getInstance().logNode(
				serialNumber,
			String.format("EXP BINOP(%s)",sop));
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (left  != null) AstGraphviz.getInstance().logEdge(serialNumber,left.serialNumber);
		if (right != null) AstGraphviz.getInstance().logEdge(serialNumber,right.serialNumber);
	}

	public Type semantMe() throws SemanticException
	{
		type_left = left.semantMe();
		type_right = right.semantMe();
	
		if (op != 6){
			/****************************/
			/* [1] Check special case: operator + on two strings */
			/****************************/
			if(op == 0 && type_left.isString() && type_right.isString()) {
				type = TypeString.getInstance();
				return type;
			}

			/****************************/
			/* [2] General case: ensure both sides are int */
			/****************************/
			if (!type_left.isInt() || !type_right.isInt()) {
				System.out.printf("ERROR at line %d, binary operator %s requires both operands to be of type int\n", line, op);
				throw new SemanticException(String.format("ERROR(%d)",line));
			}

			/****************************/
			/* [3] Check if op is /, if so check that right side is not 0 */
			/****************************/
			if (op == 3) {
				if (right instanceof AstExpInt) {
					AstExpInt right_int = (AstExpInt)right;
					if (right_int.value == 0) {
						System.out.printf("ERROR at line %d, division by zero\n", line);
						throw new SemanticException(String.format("ERROR(%d)",line));
					}
				}
			}
			type = TypeInt.getInstance();
			return type;
		}
		else{ /*equality check */
		/****************************/
		    /* both operands are of the same type */
			if (type_left.name.equals(type_right.name)) {
				type = TypeInt.getInstance();
				return type;
			}
			
			/*	comparing with nil */
			boolean leftIsNil = (type_left instanceof TypeNil) || "nil".equals(type_left.name);
			boolean rightIsNil = (type_right instanceof TypeNil) || "nil".equals(type_right.name);

			if (leftIsNil || rightIsNil) {
				Type nonNilType = leftIsNil ? type_right : type_left;
				
				/* non Nil Type can't be primitive */
				if (nonNilType.isClass() || nonNilType.isArray()) {
					type = TypeInt.getInstance();
					return type;
				} 
				else {
					System.out.printf("ERROR at line %d, equality check with nil requires class or array type\n", line);
					throw new SemanticException(String.format("ERROR(%d)",line));
				}
			}

			/* both operands are classes and one is subclass of the other */
			if (type_left.isClass() && type_right.isClass()) {
				TypeClass left_class = (TypeClass)type_left;
				TypeClass right_class = (TypeClass)type_right;
				
				if (left_class.isSubClassOf(right_class) || right_class.isSubClassOf(left_class)) {
					type = TypeInt.getInstance();
					return type;
				}
			}

			/* if got here, them type mismatch */
			System.out.printf("ERROR at line %d, type mismatch for equality operator\n", line);
			throw new SemanticException(String.format("ERROR(%d)",line));
		}
	}
	
	public void setGlobalVarData(List<String> globalVars) {
		left.setGlobalVarData(globalVars);
		right.setGlobalVarData(globalVars);
	}

	public void setFunctionData(FunctionData data) {
		left.setFunctionData(data);
		right.setFunctionData(data);
	}

	public void setClassData(ClassData data) {
		left.setClassData(data);
		right.setClassData(data);
	}

	public Temp irMe()
	{
		Temp t1 = null;
		Temp t2 = null;
		Temp dst = TempFactory.getInstance().getFreshTemp();

		if (left  != null) t1 = left.irMe();
		if (right != null) t2 = right.irMe();

		/* If either operand failed to generate IR, return early */
		if (t1 == null || t2 == null) {
			return dst;
		}

		/*	comparing with nil */
		boolean leftIsNil = (type_left instanceof TypeNil) || "nil".equals(type_left.name);
		boolean rightIsNil = (type_right instanceof TypeNil) || "nil".equals(type_right.name);

		if (type_left.isInt()) {
			if (op == 0) Ir.getInstance().AddIrCommand(new IrCommandBinopAddIntegers(dst,t1,t2));
			if (op == 1) Ir.getInstance().AddIrCommand(new IrCommandBinopSubIntegers(dst,t1,t2));
			if (op == 2) Ir.getInstance().AddIrCommand(new IrCommandBinopMulIntegers(dst,t1,t2));
			if (op == 3) Ir.getInstance().AddIrCommand(new IrCommandBinopDivIntegers(dst,t1,t2));
			if (op == 4) Ir.getInstance().AddIrCommand(new IrCommandBinopLtIntegers(dst,t1,t2));
			if (op == 5) Ir.getInstance().AddIrCommand(new IrCommandBinopGtIntegers(dst,t1,t2));
			if (op == 6) Ir.getInstance().AddIrCommand(new IrCommandBinopEqIntegers(dst,t1,t2));
		}
		else if (type_left.isString() && op == 0) {
			Ir.getInstance().AddIrCommand(new IrCommandBinopAddStrings(dst,t1,t2));
		} 
		else if (type_left.isString() && op == 6) {
			Ir.getInstance().AddIrCommand(new IrCommandBinopEqStrings(dst,t1,t2));
		}
		else if (leftIsNil && rightIsNil && op == 6) {
			Ir.getInstance().AddIrCommand(new IRcommandConstInt(dst, 1, funcData, classData)); // nil = nil is always true
		}
		else if ((type_left.isClass() || leftIsNil) && (type_right.isClass() || rightIsNil) && op == 6) {
			Ir.getInstance().AddIrCommand(new IrCommandBinopEqClasses(dst,t1,t2));
		}
		else if ((type_left.isArray() || leftIsNil) && (type_right.isArray() || rightIsNil) && op == 6) {
			Ir.getInstance().AddIrCommand(new IrCommandBinopEqArrays(dst,t1,t2));
		}
		return dst;
	}
	
}
