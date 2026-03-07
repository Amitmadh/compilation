package ast;
import java.util.List;

import data.ClassData;
import symboltable.SymbolTable;
import temp.Temp;
import types.*;

public class AstCfield extends AstNode
{
	AstVarDec varDec;
    AstFuncDec funDec;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstCfield(AstVarDec varDec, AstFuncDec funDec, int line)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		super(line);
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		// System.out.format("====================== cfield -> varDec | funcDec\n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.varDec = varDec;
        this.funDec = funDec;
	}

	/************************************************/
	/* The printing message for an int exp AST node */
	/************************************************/
	public void printMe()
	{
		/*******************************/
		/* AST NODE TYPE = AST INT EXP */
		/*******************************/
		// System.out.format("AST NODE CLASS FIELD\n");

        /**************************************/
		/* RECURSIVELY PRINT left + right ... */
		/**************************************/
		if (varDec != null) varDec.printMe();
		if (funDec != null) funDec.printMe();

		/*********************************/
		/* Print to AST GRAPHVIZ DOT file */
		/*********************************/
		AstGraphviz.getInstance().logNode(
				serialNumber,
			String.format("CFIELD"));

        /****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (varDec  != null) AstGraphviz.getInstance().logEdge(serialNumber,varDec.serialNumber);
		if (funDec != null) AstGraphviz.getInstance().logEdge(serialNumber,funDec.serialNumber);
	}
	public TypeClassVarDec semantMe(TypeClass classType) throws SemanticException{				
		Type t = null;
		TypeClassVarDec varClassDec = null;
		if (varDec != null) {
			varDec.semantMe();
			t = SymbolTable.getInstance().find(varDec.type.name);
			if (t == null) {
				System.out.printf("ERROR at line %d, Type %s not found\n", line, varDec.type.name);
				throw new SemanticException(String.format("ERROR(%d)",line));
			}
			varClassDec = new TypeClassVarDec(varDec.fieldName, t);
			/*
			becasue of the recursive nature of class declarations, we will want to add
			the class variable to the class members before semanting the tail as functions or other members
			declarations may refer to it.
			*/
		
			if (classType == null) {
				System.out.printf("ERROR at line %d, class not found for class field\n", line);
				throw new SemanticException(String.format("ERROR(%d)",	line));
			}

			if (classType.dataMembers == null) {
				classType.dataMembers = new TypeClassVarDecList(varClassDec, null);
			} else {	
				classType.dataMembers = new TypeClassVarDecList(varClassDec, classType.dataMembers);
			}
		}	
		else {
			funDec.semantMe(classType);
			t = SymbolTable.getInstance().find(funDec.fieldName);
			if (t == null) {
				System.out.printf("ERROR at line %d, Type %s not found\n", line, funDec.fieldName);
				throw new SemanticException(String.format("ERROR(%d)",line));
			}
			varClassDec = new TypeClassVarDec(funDec.fieldName, t);
			
		}
		return varClassDec;
	}

	public void annotateAst() {
		if (funDec != null) {
			funDec.annotateAst();
		}
	}

	public void setGlobalVarData(List<String> globalVars) {
		if (varDec != null) {
			varDec.setGlobalVarData(globalVars);
		}
		if (funDec != null) {
			funDec.setGlobalVarData(globalVars);
		}
	}

	public void setClassData(ClassData data) {
		if (varDec != null) {
			varDec.setClassData(data);
		}
		if (funDec != null) {
			funDec.setClassData(data);
		}
	}
	
	public Temp irMe()
	{
		if (varDec != null) varDec.irMe();
		if (funDec != null) funDec.irMe();
		return null;
	}
}
