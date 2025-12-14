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
	public AstStmtAssign(AstVar var, AstExp exp, int line)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		super(line);
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
					System.out.printf("ERROR at line %d, cannot assign nil to primitive type %s\n", line, t.name);
					throw new SemanticException(String.format("ERROR(%d)",line));
				}
				/* If it is class/array, nil is valid, so we can return/continue.*/
			} 
			else {
				/* Check Primitives (int, string) */
				if (t.name.equals("int") || t.name.equals("string")) {
					if (!initType.name.equals(t.name)) {
						System.out.printf("ERROR at line %d, type mismatch in assignment. Expected %s, got %s\n", line, t.name, initType.name);
						throw new SemanticException(String.format("ERROR(%d)",line));
					}
				}
				
				/* Check Arrays */
				else if (t.isArray()) {
					if (!initType.isArray()) {
						System.out.printf("ERROR at line %d, type mismatch: cannot assign non-array to array\n", line);
						throw new SemanticException(String.format("ERROR(%d)",line));
					}
					/* Check inner element type equality */
					Type tElem = ((TypeArray)t).elemType;
					Type initElem = ((TypeArray)initType).elemType;
					
					if (!tElem.name.equals(initElem.name)) {
						System.out.printf("ERROR at line %d, array element type mismatch\n", line);
						throw new SemanticException(String.format("ERROR(%d)",line));
					}
				}
				
				/* Check Classes */
				else if (t.isClass()) {
					if (!initType.isClass()) {
						System.out.printf("ERROR at line %d, type mismatch: cannot assign non-class to class\n", line);
						throw new SemanticException(String.format("ERROR(%d)",line));
					}
					/* Check Inheritance (Polymorphism) */
					if (!((TypeClass)initType).isSubClassOf((TypeClass)t)) {
						System.out.printf("ERROR at line %d, type %s is not a subclass of %s\n", line, initType.name, t.name);
						throw new SemanticException(String.format("ERROR(%d)",line));
					}
				}
			}
		}
		

		return null;	
	}	
}
