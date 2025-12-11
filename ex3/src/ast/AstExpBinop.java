package ast;
import symboltable.SymbolTable;
import types.*;

public class AstExpBinop extends AstExp
{
	int op;
	public AstExp left;
	public AstExp right;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstExpBinop(AstExp left, AstExp right, int op)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== exp -> exp BINOP exp\n");

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
		System.out.print("AST NODE BINOP EXP\n");

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

	public Type semantMe()
	{
		Type type_left = left.semantMe();
		Type type_right = right.semantMe();
		/****************************/
		/* [1] Check special case: operator + on two strings */
		/****************************/
		if(op == 0 && type_left.isString() && type_right.isString()) {
			return TypeString.getInstance();
		}

		/****************************/
		/* [2] General case: ensure both sides are int */
		/****************************/
		if (!type_left.isInt() || !type_right.isInt()) {
			System.out.format(">> ERROR [%d:%d] binary operator %s requires both operands to be of type int\n",2,2,op);
			System.exit(0);
		}

		/****************************/
		/* [3] Check if op is /, if so check that right side is not 0 */
		/****************************/
		if (op == 3) {
			if (right instanceof AstExpInt) {
				AstExpInt right_int = (AstExpInt)right;
				if (right_int.value == 0) {
					System.out.format(">> ERROR [%d:%d] division by zero\n",2,2);
					System.exit(0);
				}
			}
		}

		/****************************/
		/* [4] Return type int */
		/****************************/
		return TypeInt.getInstance();
	}
}
