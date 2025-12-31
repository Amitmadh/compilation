package ast;
import types.*;
import ir.Ir;
import ir.IrCommandStore;
import symboltable.*;
import temp.Temp;

public class AstVarDec extends AstNode
{
	public AstType type;
    public String fieldName;
    public AstExp exp;
    public AstNewExp nexp;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstVarDec(AstType type, String fieldName, AstExp exp, AstNewExp nexp, int line)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		super(line);
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		// System.out.format("====================== varDec -> type ID [ASSIGN (exp | newExp)] SEMICOLON\n");

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
		// System.out.format("AST NODE VARDEC( %s )\n",fieldName);
        
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

	public Type semantMe() throws SemanticException
	{
		Type t;
		/****************************/
		/* [1] Make sure variable is not of type void */
		/****************************/
		if ("void".equals(type.name)){
			System.out.printf("ERROR at line %d, variable %s cannot be of type void\n", line, fieldName);
			throw new SemanticException(String.format("ERROR(%d)",line));
		}
		/****************************/
		/* [2] Check If Type exists */
		/****************************/
		t = SymbolTable.getInstance().find(type.name);
		if (t == null)
		{
			System.out.printf("ERROR at line %d, non existing type %s\n", line, type);
			throw new SemanticException(String.format("ERROR(%d)",line));
		}
		
		/**************************************/
		/* [3] Check That Name does NOT exist in current scope*/
		/**************************************/
		if (SymbolTable.getInstance().findInCurrentScope(fieldName) != null)
		{
			System.out.printf("ERROR at line %d, variable %s already exists in scope\n", line, fieldName);
			throw new SemanticException(String.format("ERROR(%d)",line));
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
					System.out.printf("ERROR at line %d, cannot assign nil to primitive type %s\n", line, fieldName);
					throw new SemanticException(String.format("ERROR(%d)",line));
				}
				// If it is class/array, nil is valid, so we can return/continue.
			} 
			else {
				// Check Primitives (int, string)
				if (t.name.equals("int") || t.name.equals("string")) {
					if (!initType.name.equals(t.name)) {
						System.out.printf("ERROR at line %d, variable %s initialization type mismatch. Expected %s, got %s\n", line, fieldName, t.name, initType.name);
						throw new SemanticException(String.format("ERROR(%d)",line));
					}
				}
				
				// Check Arrays
				else if (t.isArray()) {
					if (!initType.isArray()) {
						System.out.printf("ERROR at line %d, variable %s type mismatch: expected array\n", line, fieldName);
						throw new SemanticException(String.format("ERROR(%d)",line));
					}
					// Check inner element type equality
					Type tElem = ((TypeArray)t).elemType;
					Type initElem = ((TypeArray)initType).elemType;
					
					if (!tElem.name.equals(initElem.name)) {
						System.out.printf("ERROR at line %d, array element type mismatch\n", line);
						throw new SemanticException(String.format("ERROR(%d)",line));
					}
				}
				
				// Check Classes
				else if (t.isClass()) {
					if (!initType.isClass()) {
						System.out.printf("ERROR at line %d, variable %s type mismatch: expected class\n", line, fieldName);
						throw new SemanticException(String.format("ERROR(%d)",line));
					}
					// Check Inheritance (Polymorphism)
					if (!((TypeClass)initType).isSubClassOf((TypeClass)t)) {
						System.out.printf("ERROR at line %d, type %s is not a subclass of %s\n", line, initType.name, t.name);
						throw new SemanticException(String.format("ERROR(%d)",line));
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

	public Temp irMe()
	{	
		AstExp initialValue = null;
		if (exp != null) {
			initialValue = exp;
		}
		else if (nexp != null) {
			initialValue = nexp;
		}

		if (initialValue != null)
		{
			Ir.getInstance().AddIrCommand(new IrCommandStore(fieldName,initialValue.irMe()));
		}
		return null;
	}
}
