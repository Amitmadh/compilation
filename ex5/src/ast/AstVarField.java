package ast;
import ir.Ir;
import ir.IrCommandFieldAccess;
import temp.Temp;
import temp.TempFactory;
import types.*;

public class AstVarField extends AstVar
{
	public AstVar var;
	public String fieldName;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstVarField(AstVar var, String fieldName, int line)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		super(line);
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		// System.out.format("====================== var -> var DOT ID( %s )\n",fieldName);

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.var = var;
		this.fieldName = fieldName;
	}

	/*************************************************/
	/* The printing message for a field var AST node */
	/*************************************************/
	public void printMe()
	{
		/*********************************/
		/* AST NODE TYPE = AST FIELD VAR */
		/*********************************/
		// System.out.format("AST NODE FIELD VAR( %s )\n", fieldName);

		/**********************************************/
		/* RECURSIVELY PRINT VAR, then FIELD NAME ... */
		/**********************************************/
		if (var != null) var.printMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AstGraphviz.getInstance().logNode(
				serialNumber,
			String.format("VAR\nFIELD(%s)",fieldName));
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (var != null) AstGraphviz.getInstance().logEdge(serialNumber,var.serialNumber);
	}

	public Type semantMe() throws SemanticException
	{
		/* 1. Check that the var is instance of class */
		Type varType = var.semantMe();
		if (!(varType.isClass()))
		{
			System.out.printf("ERROR at line %d, var must have class type\n", line);
			throw new SemanticException(String.format("ERROR(%d)",line));
		}
		
		/* 2. Find field in class data members (including ancestors)*/
		TypeClass classType = (TypeClass) varType;	
		TypeClass currentClass = classType;
		while (currentClass != null) 
    	{
			TypeClassVarDecList dataMembers = currentClass.dataMembers;
			while (dataMembers != null) 
			{
				if (dataMembers.head.name.equals(fieldName)) {
					return dataMembers.head.type;
				}
				dataMembers = dataMembers.tail;
			}
			currentClass = currentClass.father;
		}	
    
		System.out.printf("ERROR at line %d, field %s does not exist in class %s\n", line, fieldName, classType.name);
		throw new SemanticException(String.format("ERROR(%d)",line));
    
    	//return null;
	}

	public Temp irMe()
	{
		Temp t = TempFactory.getInstance().getFreshTemp();
		Temp instanceAddr = var.irMe();

		Ir.getInstance().AddIrCommand(new IrCommandFieldAccess(t,instanceAddr, fieldName));
		
		return t;
	}
		
		
}
