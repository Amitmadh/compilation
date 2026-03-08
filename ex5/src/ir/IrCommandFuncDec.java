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

public class IrCommandFuncDec extends IrCommand
{
	public String funcName;
	// public List<String> localVars;
	public FunctionData funcData;
	public ClassData classData;
	
	public IrCommandFuncDec(String funcName, FunctionData funcData, ClassData classData)
	{
		this.funcName = funcName;
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

	public void mipsMe()
	{
		if (classData != null) {
			int index = classData.methodsNoClass.indexOf(funcName);
			String method = classData.methods.get(index);
			FunctionData methodData = classData.methodsData.get(method);
			MipsGenerator.getInstance().funcPrologue(method, methodData.localVars.size());
		} else if (funcData != null) {
			MipsGenerator.getInstance().funcPrologue(funcName, funcData.localVars.size());
		}
	}

	public void printMe(PrintWriter fileWriter)
	{
		if (funcData != null) {
			fileWriter.format("FUNCTION %s :\n", funcName);
			fileWriter.format("LOCAL VARIABLES: ");
			if (funcData.localVars != null) {
			fileWriter.format("%s\n", String.join(", ", funcData.localVars));
			} else {
				fileWriter.format("None\n");
			}
		} else if (classData != null) {
			FunctionData methodData = classData.methodsData.get(funcName);
			fileWriter.format("METHOD %s :\n", funcName);
			fileWriter.format("LOCAL VARIABLES: ");
			if (methodData.localVars != null) {
			fileWriter.format("%s\n", String.join(", ", methodData.localVars));
			} else {
				fileWriter.format("None\n");
			}
		}
	}
}
