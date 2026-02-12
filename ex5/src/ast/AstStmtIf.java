package ast;
import ir.Ir;
import ir.IrCommand;
import ir.IrCommandJumpIfEqToZero;
import ir.IrCommandLabel;
import ir.IrCommandJumpLabel;
import symboltable.SymbolTable;
import temp.Temp;
import temp.TempFactory;
import types.*;
public class AstStmtIf extends AstStmt
{
	public AstExp cond;
	public AstStmtList body;
	public AstStmtList elseBody;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AstStmtIf(AstExp cond, AstStmtList body, AstStmtList elseBody, int line)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		super(line);
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		// System.out.print("====================== stmt -> IF LPAREN exp RPAREN LBRACE stmtList RBRACE [ ELSE LBRACE stmtList RBRACE ]\n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.cond = cond;
		this.body = body;
		this.elseBody = elseBody;
	}

	/*********************************************************/
	/* The printing message for an assign statement AST node */
	/*********************************************************/
	public void printMe()
	{
		/********************************************/
		/* AST NODE TYPE = AST ASSIGNMENT STATEMENT */
		/********************************************/
		// System.out.print("AST NODE IF STMT\n");

		/***********************************/
		/* RECURSIVELY PRINT VAR + EXP ... */
		/***********************************/
		if (cond != null) cond.printMe();
		if (body != null) body.printMe();
		if (elseBody != null) elseBody.printMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AstGraphviz.getInstance().logNode(
				serialNumber,
			"IF\nSTMT\n");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (cond != null) AstGraphviz.getInstance().logEdge(serialNumber,cond.serialNumber);
		if (body != null) AstGraphviz.getInstance().logEdge(serialNumber,body.serialNumber);
		if (elseBody != null) AstGraphviz.getInstance().logEdge(serialNumber,body.serialNumber);
	}
	public Type semantMe() throws SemanticException
	{
		/****************************/
		/* [1] Semant the cond exp */
		/****************************/
		Type condType = cond.semantMe();
		if (!condType.isInt())
		{
			System.out.printf("ERROR at line %d, IF condition is not boolean\n", line);
			throw new SemanticException(String.format("ERROR(%d)",line));
		}
		/*	[2] Begin scope */
		SymbolTable.getInstance().beginScope();
		/*******************************/
		/* [3] Semant the body stmtList */
		/*******************************/
		body.semantMe();
		SymbolTable.getInstance().endScope();
		/********************************/
		/* [4] Semant the else body stmtList */
		/********************************/
		if (elseBody != null) {
			SymbolTable.getInstance().beginScope();
			elseBody.semantMe();
			SymbolTable.getInstance().endScope();
		}
		return null;
	}

	public Temp irMe()
	{
		if (elseBody != null) {
			/*******************************/
			/* [1] Allocate 3 fresh labels */
			/*******************************/
			String labelTrue = IrCommand.getFreshLabel("true");
			String labelFalse = IrCommand.getFreshLabel("false");
			String labelEnd = IrCommand.getFreshLabel("end");
			
			/********************/
			/* [2] cond.IRme(); */
			/********************/
			Temp condTemp = cond.irMe();

			/**********************************/
			/* [3] Jump conditionally to false label */
			/**********************************/
			Ir.
				getInstance().
				AddIrCommand(new IrCommandJumpIfEqToZero(condTemp, labelFalse));
			
			/**********************************/
			/* [4] if true label */
			/**********************************/
			Ir.
				getInstance().
				AddIrCommand(new IrCommandLabel(labelTrue));
			
			/*******************/
			/* [5] if body.IRme() */
			/*******************/
			body.irMe();

			/*******************/
			/* [5] jump to end label */
			/*******************/
			Ir.
				getInstance().
				AddIrCommand(new IrCommandJumpLabel(labelEnd));

			/*******************/
			/* [5] if false label */
			/*******************/
			Ir.
				getInstance().
				AddIrCommand(new IrCommandLabel(labelFalse));
			
			/*******************/
			/* [5] else body.IRme() */
			/*******************/
			elseBody.irMe();

			/**********************************/
			/* [4] if end label */
			/**********************************/
			Ir.
				getInstance().
				AddIrCommand(new ir.IrCommandLabel(labelEnd));
		
		} else {
			/*******************************/
			/* [1] Allocate fresh label */
			/*******************************/
			String labelEnd = IrCommand.getFreshLabel("end");
						
			/********************/
			/* [2] cond.IRme(); */
			/********************/
			Temp condTemp = cond.irMe();

			/**********************************/
			/* [3] Jump conditionally to end label */
			/**********************************/
			Ir.
				getInstance().
				AddIrCommand(new IrCommandJumpIfEqToZero(condTemp, labelEnd));

			/*******************/
			/* [4] if body.IRme() */
			/*******************/
			body.irMe();

			/**********************************/
			/* [4] if end label */
			/**********************************/
			Ir.
				getInstance().
				AddIrCommand(new ir.IrCommandLabel(labelEnd));
		
		}
		return null;
	}
}