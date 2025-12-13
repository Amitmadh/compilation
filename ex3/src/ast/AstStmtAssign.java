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
	public Type semantMe()
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
					System.out.format(">> ERROR [%d:%d] cannot assign nil to primitive type %s\n", 2, 2, t.name);
					System.exit(0);
				}
				/* If it is class/array, nil is valid, so we can return/continue.*/
			} 
			else {
				/* Check Primitives (int, string) */
				if (t.name.equals("int") || t.name.equals("string")) {
					if (!initType.name.equals(t.name)) {
						System.out.format(">> ERROR [%d:%d] type mismatch in assignment. Expected %s, got %s\n", 2, 2, t.name, initType.name);
						System.exit(0);
					}
				}
				
				/* Check Arrays */
				else if (t.isArray()) {
					if (!initType.isArray()) {
						System.out.format(">> ERROR [%d:%d] type mismatch: cannot assign non-array to array\n", 2, 2);
						System.exit(0);
					}
					/* Check inner element type equality */
					Type tElem = ((TypeArray)t).elemType;
					Type initElem = ((TypeArray)initType).elemType;
					
					if (!tElem.name.equals(initElem.name)) {
						System.out.format(">> ERROR [%d:%d] array element type mismatch\n", 2, 2);
						System.exit(0);
					}
				}
				
				/* Check Classes */
				else if (t.isClass()) {
					if (!initType.isClass()) {
						System.out.format(">> ERROR [%d:%d] type mismatch: cannot assign non-class to class\n", 2, 2);
						System.exit(0);
					}
					/* Check Inheritance (Polymorphism) */
					if (!((TypeClass)initType).isSubClassOf((TypeClass)t)) {
						System.out.format(">> ERROR [%d:%d] type %s is not a subclass of %s\n", 2, 2, initType.name, t.name);
						System.exit(0);
					}
				}
			}
		}
		

		return null;	
	}	
}
