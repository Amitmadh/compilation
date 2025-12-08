package ast;
import types.*;
import symboltable.*;

public class AstVarDec extends AstNode
{
	public AstType type;
    public String fieldName;
    public AstExp exp;
    public AstNewExp nexp;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstVarDec(AstType type, String fieldName, AstExp exp, AstNewExp nexp)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== varDec -> type ID [ASSIGN (exp | newExp)] SEMICOLON\n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.type = type;
        this.fieldName = fieldName;
        this.exp = exp;
        this.nexp = nexp;
	}

	/************************************************/
	/* The printing message for an int exp AST node */
	/************************************************/
	public void printMe()
	{
		/*******************************/
		/* AST NODE TYPE = AST INT EXP */
		/*******************************/
		System.out.format("AST NODE VARDEC( %s )\n",fieldName);
        
        /****************************************/
		/* RECURSIVELY PRINT VAR + SUBSCRIPT ... */
		/****************************************/
		if (type != null) type.printMe();
		if (exp != null) exp.printMe();
        if (nexp != null) nexp.printMe();
        
		/*********************************/
		/* Print to AST GRAPHVIZ DOT file */
		/*********************************/
		AstGraphviz.getInstance().logNode(
				serialNumber,
			String.format("VARDEC( %s )",fieldName));

        /****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (type != null) AstGraphviz.getInstance().logEdge(serialNumber,type.serialNumber);
		if (exp != null) AstGraphviz.getInstance().logEdge(serialNumber,exp.serialNumber);
        if (nexp != null) AstGraphviz.getInstance().logEdge(serialNumber,nexp.serialNumber);
	}

	public Type semantMe()
	{
		Type t;
		/****************************/
		/* [1] Make sure variable is not of type void */
		/****************************/
		if ("void".equals(type.name)){
			System.out.format(">> ERROR [%d:%d] variable %s cannot be of type void\n",2,2,fieldName);
			System.exit(0);
		}
		/****************************/
		/* [2] Check If Type exists */
		/****************************/
		t = SymbolTable.getInstance().find(type.name);
		if (t == null)
		{
			System.out.format(">> ERROR [%d:%d] non existing type %s\n",2,2,type);
			System.exit(0);
		}
		
		/**************************************/
		/* [3] Check That Name does NOT exist in current scope*/
		/**************************************/
		if (SymbolTable.getInstance().findInCurrentScope(fieldName) != null)
		{
			System.out.format(">> ERROR [%d:%d] variable %s already exists in scope\n",2,2,fieldName);	
			System.exit(0);			
		}
		/**************************************/
		/* [4] Check that Initialization expression type matches variable type */
		/**************************************/
		Type initType = null;

    
		if (exp != null) {
			initType = exp.semantMe();
		}
		else if (nexp != null) {
			initType = nexp.semantMe();
		}

		// Only perform checks if there is an initialization
		if (initType != null) {

			if (initType instanceof TypeNil) { 
				if (!t.isClass() && !t.isArray()) {
					System.out.format(">> ERROR [%d:%d] cannot assign nil to primitive type %s\n", 2, 2, fieldName);
					System.exit(0);
				}
				// If it is class/array, nil is valid, so we can return/continue.
			} 
			else {
				// Check Primitives (int, string)
				if (t.name.equals("int") || t.name.equals("string")) {
					if (!initType.name.equals(t.name)) {
						System.out.format(">> ERROR [%d:%d] variable %s initialization type mismatch. Expected %s, got %s\n", 2, 2, fieldName, t.name, initType.name);
						System.exit(0);
					}
				}
				
				// Check Arrays
				else if (t.isArray()) {
					if (!initType.isArray()) {
						System.out.format(">> ERROR [%d:%d] variable %s type mismatch: expected array\n", 2, 2, fieldName);
						System.exit(0);
					}
					// Check inner element type equality
					Type tElem = ((TypeArray)t).elemType;
					Type initElem = ((TypeArray)initType).elemType;
					
					if (!tElem.name.equals(initElem.name)) {
						System.out.format(">> ERROR [%d:%d] array element type mismatch\n", 2, 2);
						System.exit(0);
					}
				}
				
				// Check Classes
				else if (t.isClass()) {
					if (!initType.isClass()) {
						System.out.format(">> ERROR [%d:%d] variable %s type mismatch: expected class\n", 2, 2, fieldName);
						System.exit(0);
					}
					// Check Inheritance (Polymorphism)
					if (!((TypeClass)initType).isSubClassOf((TypeClass)t)) {
						System.out.format(">> ERROR [%d:%d] type %s is not a subclass of %s\n", 2, 2, initType.name, t.name);
						System.exit(0);
					}
				}
			}
		}
		/************************************************/
		/* [5] Enter the Identifier to the Symbol Table */
		/************************************************/
		SymbolTable.getInstance().enter(fieldName, t);

		return null;	
	}	
}
