package ast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import data.ClassData;
import data.FunctionData;
import temp.Temp;
import types.*;

public class AstCfieldList extends AstNode
{
	/****************/
	/* DATA MEMBERS */
	/****************/
	public AstCfield head;
	public AstCfieldList tail;
	public String className;

	//annotations
	public List<String> vars;
	public List<String> varsNoOffset;
    public List<String> methods;
	public List<String> methodsNoClass;
    public Map<String, FunctionData> methodsData;

	public Map<String,Integer> intVals;
	public Map<String,String> strVals;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstCfieldList(AstCfield head, AstCfieldList tail, int line)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		super(line);
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		// if (tail != null) System.out.print("====================== cFields -> cField cFields\n");
		// if (tail == null) System.out.print("====================== cFields -> cField        \n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.head = head;
		this.tail = tail;
	}

	/******************************************************/
	/* The printing message for a statement list AST node */
	/******************************************************/
	public void printMe()
	{
		/**************************************/
		/* AST NODE TYPE = AST STATEMENT LIST */
		/**************************************/
		// System.out.print("AST NODE CFIELD LIST\n");

		/*************************************/
		/* RECURSIVELY PRINT HEAD + TAIL ... */
		/*************************************/
		if (head != null) head.printMe();
		if (tail != null) tail.printMe();

		/**********************************/
		/* PRINT to AST GRAPHVIZ DOT file */
		/**********************************/
		AstGraphviz.getInstance().logNode(
				serialNumber,
			"CFIELD\nLIST\n");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (head != null) AstGraphviz.getInstance().logEdge(serialNumber,head.serialNumber);
		if (tail != null) AstGraphviz.getInstance().logEdge(serialNumber,tail.serialNumber);
	}

	public TypeClassVarDecList semantMe(TypeClass classType) throws SemanticException
	{
		if (head == null) {
        	return null;
    	}

		/*******************************/
		/* [1] Semant head cfield ...  */
		/*******************************/
		if (head != null)  head.semantMe(classType);

		/*******************************/
		/* [2] Semant tail cfieldList */
		/*******************************/
		if (tail != null) tail.semantMe(classType);

		/*******************************/
		/* [3] Return the type list ... */
		/*******************************/
		return classType.dataMembers;
	}

	public void annotateAst() {
		AstCfield curr = head;
		AstCfieldList nextField = tail;

		vars = new ArrayList<>();
		varsNoOffset = new ArrayList<>();
		methods = new ArrayList<>();
		methodsNoClass = new ArrayList<>();
		methodsData = new HashMap<>();

		intVals = new HashMap<>();
		strVals = new HashMap<>();

		while (curr != null) {
			curr.annotateAst();
			if (curr.varDec != null) {
				AstVarDec var = curr.varDec;
				vars.add(var.fieldName + "offset" + var.offset);
				varsNoOffset.add(var.fieldName);
				if (var.exp instanceof AstExpInt) {
					int val = ((AstExpInt) var.exp).value;
					intVals.put(var.fieldName, val);
				} else if (var.exp instanceof AstExpString) {
					String val = ((AstExpString) var.exp).value;
					strVals.put(var.fieldName, val);
				}
			}
			if (curr.funDec != null) {
				AstFuncDec func = curr.funDec;
				String method = func.fieldName + "class" + className;
				methods.add(method);
				methodsNoClass.add(func.fieldName);

				List<String> args = func.args;
		 		List<String> localVars = func.localVars;
				FunctionData funcData = new FunctionData(method, args, localVars);
				methodsData.put(method, funcData);
				func.setFunctionData(funcData);
			}
			
			if (nextField != null) {
				curr = nextField.head;
				nextField = nextField.tail;
			} else {
				break;
			}
		}
	}

	public void setGlobalVarData(List<String> globalVars) {
		head.setGlobalVarData(globalVars);
		if (tail != null) {
			tail.setGlobalVarData(globalVars);
		}
	}

	public void setClassData(ClassData data) {
		head.setClassData(data);
		if (tail != null) {
			tail.setClassData(data);
		}
	}
	
	public Temp irMe()
	{
		head.irMe();
		if (tail != null) tail.irMe();
		return null;
	}
}
