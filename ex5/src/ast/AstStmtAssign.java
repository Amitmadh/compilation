package ast;
import ir.Ir;
import ir.IrCommandArraySet;
import ir.IrCommandFieldSet;
import ir.IrCommandStore;
import temp.Temp;
import types.*;

public class AstStmtAssign extends AstStmt
{
	/***************/
	/*  var := exp */
	/***************/
	public AstVar var;
	public AstExp exp;
	public int offset;

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
		
		if (var instanceof AstVarSimple) {
			this.offset = ((AstVarSimple)var).offset;
		}
		
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
					if(initType.name != null) {
						// this array has a type, check it against expected
						if( !t.name.equals(initType.name)) {
							System.out.printf("ERROR at line %d, array type mismatch in assignment. Expected %s, got %s\n", line, t.name, initType.name);
							throw new SemanticException(String.format("ERROR(%d)",line));
						}
					}
					// else array has no type e.g created via new T[e]
					/* Check inner element type equality */
					Type tElem = ((TypeArray)t).elemType;
					Type initElem = ((TypeArray)initType).elemType;
					System.out.println("ARRAYS TYPES: " + t.name + " , " + initType.name);
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

	public Temp irMe()
	{
		Temp expVal = exp.irMe();

		// handle simple variable assignment by name

		if (var instanceof AstVarSimple) {
			String name = ((AstVarSimple)var).name;
			Ir.getInstance().AddIrCommand(new IrCommandStore(name, expVal, this.offset));
		}

		// field assignment: instance.field := exp
		if (var instanceof AstVarField) {
			AstVarField varField = (AstVarField)var;
			Temp instanceAddr = varField.var.irMe();
			Ir.getInstance().AddIrCommand(new IrCommandFieldSet(instanceAddr, varField.fieldName, expVal));
		}

		// array assignment: arr[index] := exp
		if (var instanceof AstVarSubscript) {
			AstVarSubscript varSubscript = (AstVarSubscript)var;
			Temp arrayAddr = varSubscript.var.irMe();
			Temp index = varSubscript.subscript.irMe();
			Ir.getInstance().AddIrCommand(new IrCommandArraySet(arrayAddr, index, expVal));
		}

		return null;
	}
}
