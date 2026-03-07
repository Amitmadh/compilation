/***********/
/* PACKAGE */
/***********/
package ir;

import java.io.PrintWriter;
import java.util.HashSet;

import data.ClassData;
import data.FunctionData;
import mips.MipsGenerator;
import temp.Temp;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */
/*******************/

public class IrCommandAllocate extends IrCommand
{
	//with offset
	String varName;
	//without offset
	public String name;

	public boolean isVarGlobal;
	public FunctionData funcData;
	public ClassData classData;
	
	public IrCommandAllocate(String varName, int offset, boolean isVarGlobal, FunctionData funcData, ClassData classData)
	{
		this.varName = varName + "offset" + offset;
		this.name = varName;

		this.isVarGlobal = isVarGlobal;
		this.funcData = funcData;
		this.classData = classData;
	}

	public HashSet<String> tempsUsed() {
		HashSet<String> used = new HashSet<String>();
		return used;
	}

	public String tempDefined() {
		return null;
	}

	public HashSet<Temp> temps() {
		HashSet<Temp> temps = new HashSet<Temp>();
		return temps;
	}

	public String getVar() {
		return varName;
	}

	public void mipsMe()
	{
		if (isVarGlobal) {
			MipsGenerator.getInstance().allocate(varName);
		}
	}

	public void printMe(PrintWriter fileWriter)
	{
		fileWriter.print("ALLOCATE " + varName + "\n");
	}
}
