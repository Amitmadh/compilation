package ast;
import symboltable.SymbolTable;
import types.*;

public class AstCfield extends AstNode
{
	AstVarDec varDec;
    AstFuncDec funDec;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AstCfield(AstVarDec varDec, AstFuncDec funDec, int line)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		super(line);
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		// System.out.format("====================== cfield -> varDec | funcDec\n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.varDec = varDec;
        this.funDec = funDec;
	}

	/************************************************/
	/* The printing message for an int exp AST node */
	/************************************************/
	public void printMe()
	{
		/*******************************/
		/* AST NODE TYPE = AST INT EXP */
		/*******************************/
		// System.out.format("AST NODE CLASS FIELD\n");

        /**************************************/
		/* RECURSIVELY PRINT left + right ... */
		/**************************************/
		if (varDec != null) varDec.printMe();
		if (funDec != null) funDec.printMe();

		/*********************************/
		/* Print to AST GRAPHVIZ DOT file */
		/*********************************/
		AstGraphviz.getInstance().logNode(
				serialNumber,
			String.format("CFIELD"));

        /****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (varDec  != null) AstGraphviz.getInstance().logEdge(serialNumber,varDec.serialNumber);
		if (funDec != null) AstGraphviz.getInstance().logEdge(serialNumber,funDec.serialNumber);
	}
	public TypeClassVarDec semantMe() throws SemanticException
	{	
		Type t = null;
		TypeClassVarDec varClassDec = null;
		if (varDec != null) {
			varDec.semantMe();
			t = SymbolTable.getInstance().find(varDec.type.name);
			if (t == null) {
				throw new SemanticException(String.format(">> ERROR [%d:%d] Type %s not found", 2, 2, varDec.type.name));
			}
			varClassDec = new TypeClassVarDec(varDec.fieldName, t);
		}	
		else {
			funDec.semantMe();
			t = SymbolTable.getInstance().find(funDec.fieldName);
			if (t == null) {
				throw new SemanticException(String.format(">> ERROR [%d:%d] Type %s not found", 2, 2, funDec.fieldName));
			}
			varClassDec = new TypeClassVarDec(funDec.fieldName, t);
			
		}
		return varClassDec;
	}
}
