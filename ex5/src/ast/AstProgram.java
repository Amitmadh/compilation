package ast;

import temp.Temp;
import data.*;
import mips.MipsGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AstProgram extends AstNode
{
	/****************/
	/* DATA MEMBERS */
	/****************/
	public AstDec head;
	public AstProgram tail;

	public static List<String> globalVars = new ArrayList<>();
	public Map<String,FunctionData> functions;
	public static Map<String,ClassData> classes;

	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstProgram(AstDec head, AstProgram tail, int line)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		super(line);
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
        // if (tail != null) System.out.print("====================== program -> dec program   \n");
		// if (tail == null) System.out.print("====================== program -> dec           \n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.head = head;
        this.tail = tail;

		functions = new HashMap<>();
		classes = new HashMap<>();
	}

	/************************************************/
	/* The printing message for an int exp AST node */
	/************************************************/
	public void printMe()
	{
		/*******************************/
		/* AST NODE TYPE = AST PROGRAM */
		/*******************************/
		// System.out.format("AST NODE PROGRAM\n");

		/**************************************/
		/* RECURSIVELY PRINT left + right ... */
		/**************************************/
		if (head != null) head.printMe();
		if (tail != null) tail.printMe();

		/*********************************/
		/* Print to AST GRAPHVIZ DOT file */
		/*********************************/
		AstGraphviz.getInstance().logNode(
				serialNumber,
			String.format("PROGRAM"));

        /****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (head != null) AstGraphviz.getInstance().logEdge(serialNumber,head.serialNumber);
		if (tail != null) AstGraphviz.getInstance().logEdge(serialNumber,tail.serialNumber);
	}

	public void semantMe() throws SemanticException {
		if (head != null) head.semantMe(null);
		if (tail != null) tail.semantMe();
    }

	public void annotateAst()
	{
		AstDec curr = head;
		AstProgram nextProgram = tail;

		while (curr != null) {
			curr.annotateAst();
			if (curr instanceof AstDecFuncDec) {
				AstDecFuncDec funcDec = (AstDecFuncDec) curr;
				FunctionData funcData = new FunctionData(funcDec.funcName, funcDec.args, funcDec.localVars);
				functions.put(funcDec.funcName, funcData);
				setFunctionData(funcData, funcDec);
			} else if (curr instanceof AstDecVarDec) {
				AstDecVarDec varDec = (AstDecVarDec) curr;
				globalVars.add(varDec.varName + "offset" + varDec.offset);
			} else if (curr instanceof AstDecClassDec) {
				AstDecClassDec classDec = (AstDecClassDec) curr;
				ClassData classData = new ClassData(classDec.className, classDec.extendsName, classDec.vars, classDec.varsNoOffset, classDec.methods, classDec.methodsNoClass, classDec.methodsData, classDec.intVals, classDec.strVals);
				if (classDec.extendsName != null) {
					ClassData extendsClassData = new ClassData(classes.get(classDec.extendsName));
					extendsClassData.addFields(classData);
					extendsClassData.className = classData.className;
					extendsClassData.extendsName = classData.extendsName;
					classData = extendsClassData;
				}
				setClassData(classData, classDec);
				classes.put(classDec.className, classData);
				MipsGenerator.classesToDefaultStrVals.put(classData.className, classData.strVals);
			} else if (curr instanceof AstDecArrayTypeDef) {
				//do nothing
			}

			if (nextProgram != null) {
				curr = nextProgram.head;
				nextProgram = nextProgram.tail;
			} else {
				break;
			}
		}
		setGlobalVarData(globalVars);
	}

	public void setGlobalVarData(List<String> globalVars) {
		AstDec currDec = head;
		AstProgram nextDec = tail;

		while (currDec != null) {
			currDec.setGlobalVarData(globalVars);
			if (nextDec != null) {
				currDec = nextDec.head;
				nextDec = nextDec.tail;
			} else {
				break;
			}
		}
	}

	public void setFunctionData(FunctionData data, AstDecFuncDec function) {
		function.setFunctionData(data);
	}

	public void setClassData(ClassData data, AstDecClassDec cls) {
		cls.setClassData(data);
	}

	public Temp irMe()
	{
		if (head != null) head.irMe();
		if (tail != null) tail.irMe();
		return null;
	}
}
