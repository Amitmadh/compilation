package ast;
import types.*;
import symboltable.*;
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
				throw new SemanticException(String.format(">> ERROR [%d:%d] calling method %s on a non-existing variable", 2, 2, fieldName));
			}
			/* Check that the var is instance of class */
			if (!(varType.isClass()))
			{
				throw new SemanticException(String.format(">> ERROR [%d:%d] var must have class type",2,2));
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
				throw new SemanticException(String.format(">> ERROR [%d:%d] call to undefined function %s", 2, 2, fieldName));
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
				throw new SemanticException(String.format(">> ERROR [%d:%d] function %s argument type mismatch. Expected %s, got %s", 2, 2, fieldName, paramType.name, expType.name));
			}
			paramTypes = paramTypes.tail;
			currExpType = currExpType.tail;
			match = false;
		}
		if (paramTypes != null || currExpType != null)
		{
			throw new SemanticException(String.format(">> ERROR [%d:%d] function %s argument count mismatch", 2, 2, fieldName));
		}
		return funcTypeFunction.returnType;
	}
}
