package ast;
import types.*;

public class AstStmtAssign extends AstStmt
{
	/***************/
	/*  var := exp */
	/***************/
	public AstVar var;
	public AstExp exp;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AstStmtAssign(AstVar var, AstExp exp)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		// System.out.print("====================== stmt -> var ASSIGN exp SEMICOLON\n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.var = var;
		this.exp = exp;
	}

	/*********************************************************/
	/* The printing message for an assign statement AST node */
	/*********************************************************/
	public void printMe()
	{
		/********************************************/
		/* AST NODE TYPE = AST ASSIGNMENT STATEMENT */
		/********************************************/
		// System.out.print("AST NODE ASSIGN STMT\n");

		/***********************************/
		/* RECURSIVELY PRINT VAR + EXP ... */
		/***********************************/
		if (var != null) var.printMe();
		if (exp != null) exp.printMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AstGraphviz.getInstance().logNode(
				serialNumber,
			"ASSIGN\nleft := right\n");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AstGraphviz.getInstance().logEdge(serialNumber,var.serialNumber);
		AstGraphviz.getInstance().logEdge(serialNumber,exp.serialNumber);
	}
	public Type semantMe() throws SemanticException
	{
		Type t = var.semantMe();
		
		/**************************************/
		/* Check that Initialization expression type matches variable type */
		/**************************************/
		Type initType = null;
    
		if (exp != null) {
			initType = exp.semantMe();
		}

		/* Only perform checks if there is an initialization*/ 
		if (initType != null) {

			if (initType instanceof TypeNil) { 
				if (!t.isClass() && !t.isArray()) {
					throw new SemanticException(String.format(">> ERROR [%d:%d] cannot assign nil to primitive type %s", 2, 2, t.name));
				}
				/* If it is class/array, nil is valid, so we can return/continue.*/
			} 
			else {
				/* Check Primitives (int, string) */
				if (t.name.equals("int") || t.name.equals("string")) {
					if (!initType.name.equals(t.name)) {
						throw new SemanticException(String.format(">> ERROR [%d:%d] type mismatch in assignment. Expected %s, got %s", 2, 2, t.name, initType.name));
					}
				}
				
				/* Check Arrays */
				else if (t.isArray()) {
					if (!initType.isArray()) {
						throw new SemanticException(String.format(">> ERROR [%d:%d] type mismatch: cannot assign non-array to array", 2, 2));
					}
					/* Check inner element type equality */
					Type tElem = ((TypeArray)t).elemType;
					Type initElem = ((TypeArray)initType).elemType;
					
					if (!tElem.name.equals(initElem.name)) {
						throw new SemanticException(String.format(">> ERROR [%d:%d] array element type mismatch", 2, 2));
					}
				}
				
				/* Check Classes */
				else if (t.isClass()) {
					if (!initType.isClass()) {
						throw new SemanticException(String.format(">> ERROR [%d:%d] type mismatch: cannot assign non-class to class", 2, 2));
					}
					/* Check Inheritance (Polymorphism) */
					if (!((TypeClass)initType).isSubClassOf((TypeClass)t)) {
						throw new SemanticException(String.format(">> ERROR [%d:%d] type %s is not a subclass of %s", 2, 2, initType.name, t.name));
					}
				}
			}
		}
		

		return null;	
	}	
}
