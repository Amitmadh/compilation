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
	public AstCfield(AstVarDec varDec, AstFuncDec funDec)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		serialNumber = AstNodeSerialNumber.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== cfield -> varDec | funcDec\n");

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
		System.out.format("AST NODE CLASS FIELD\n");

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
	public Type semantMe()
	{	
		Type t = null;
		if (varDec != null) {
			varDec.semantMe();
			t = SymbolTable.getInstance().find(varDec.type.name);
			if (t == null) {
				System.out.format(">> ERROR [%d:%d] Type %s not found\n", 2, 2, varDec.type.name);
				System.exit(0);
			}
		}	
		else {
			funDec.semantMe();
			t = SymbolTable.getInstance().find(funDec.fieldName);
			if (t == null) {
				System.out.format(">> ERROR [%d:%d] Type %s not found\n", 2, 2, funDec.fieldName);
				System.exit(0);
			}
			
		}
		return t;
	}
}
