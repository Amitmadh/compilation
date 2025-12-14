package ast;
import symboltable.SymbolTable;
import types.*;

public class AstFuncArg extends AstNode
{
	AstType type;
    String fieldName;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstFuncArg(AstType type, String fieldName, int line)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		super(line);
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		// System.out.format("====================== funcArg -> type ID(%s)\n", fieldName);

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.type = type;
        this.fieldName = fieldName;
	}

	/************************************************/
	/* The printing message for an int exp AST node */
	/************************************************/
	public void printMe()
	{
		/*******************************/
		/* AST NODE TYPE = AST INT EXP */
		/*******************************/
		// System.out.format("AST NODE FUNC ARGUMENT(%s)\n", fieldName);

		/****************************************/
		/* RECURSIVELY PRINT VAR + SUBSCRIPT ... */
		/****************************************/
		if (type != null) type.printMe();

		/*********************************/
		/* Print to AST GRAPHVIZ DOT file */
		/*********************************/
		AstGraphviz.getInstance().logNode(
				serialNumber,
			String.format("ARG ID(%s)", fieldName));

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (type != null) AstGraphviz.getInstance().logEdge(serialNumber,type.serialNumber);
	}

	public Type semantMe() throws SemanticException
	{
		Type t;
		/****************************/
		/* [1] Make sure variable is not of type void */
		/****************************/
		if ("void".equals(type.name)){
			throw new SemanticException(String.format(">> ERROR [%d:%d] argument %s cannot be of type void",2,2,fieldName));
		}
		/****************************/
		/* [2] Check If Type exists */
		/****************************/
		t = SymbolTable.getInstance().find(type.name);
		if (t == null)
		{
			throw new SemanticException(String.format(">> ERROR [%d:%d] non existing type %s",2,2,type.name));
		}
		/**************************************/
		/* [3] Check That Name does NOT exist in current scope*/
		/**************************************/
		if (SymbolTable.getInstance().findInCurrentScope(fieldName) != null)
		{
			throw new SemanticException(String.format(">> ERROR [%d:%d] variable %s already exists in scope",2,2,fieldName));
		}
		/************************************************/
		/* [4] Enter the Identifier to the Symbol Table */
		/************************************************/
		SymbolTable.getInstance().enter(fieldName, t);

		return t;
	}
}
