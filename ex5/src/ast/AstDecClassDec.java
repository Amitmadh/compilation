package ast;
import java.util.List;
import java.util.Map;

import data.ClassData;
import data.FunctionData;
import temp.Temp;
import types.TypeClass;

public class AstDecClassDec extends AstDec
{
	public AstClassDec classDec;

	//annotations
	public String className;
	public String extendsName;
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
	public AstDecClassDec(AstClassDec classDec, int line)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		super(line);
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		// System.out.print("====================== dec -> classDec\n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.classDec = classDec;
	}

	/************************************************/
	/* The printing message for an int exp AST node */
	/************************************************/
	public void printMe()
	{
		/*******************************/
		/* AST NODE TYPE = AST INT EXP */
		/*******************************/
		// System.out.print("AST NODE DEC\n");

        /**************************************/
		/* RECURSIVELY PRINT left + right ... */
		/**************************************/
		if (classDec != null) classDec.printMe();

		/*********************************/
		/* Print to AST GRAPHVIZ DOT file */
		/*********************************/
		AstGraphviz.getInstance().logNode(
				serialNumber,
			String.format("DEC"));

        /****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (classDec  != null) AstGraphviz.getInstance().logEdge(serialNumber,classDec.serialNumber);
	}

	public void semantMe(TypeClass classType) throws SemanticException {
		if (classDec != null) classDec.semantMe(classType);
	}

	public void annotateAst()
	{
		classDec.annotateAst();

		this.className = classDec.className;
		this.extendsName = classDec.extendName;
		this.vars = classDec.vars;
		this.varsNoOffset = classDec.varsNoOffset;
		this.methods = classDec.methods;
		this.methodsNoClass = classDec.methodsNoClass;
		this.methodsData = classDec.methodsData;
		this.intVals = classDec.intVals;
		this.strVals = classDec.strVals;
	}

	public void setGlobalVarData(List<String> globalVars) {
		classDec.setGlobalVarData(globalVars);
	}

	public void setClassData(ClassData data) {
		classDec.setClassData(data);
	}

	public Temp irMe() 
	{
		if (classDec != null) return classDec.irMe();
		return null;
	}
}