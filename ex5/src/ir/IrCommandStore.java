/***********/
/* PACKAGE */
/***********/
package ir;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */
/*******************/
import temp.Temp;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;

import ast.AstProgram;
import data.ClassData;
import data.FunctionData;
import mips.MipsGenerator;

public class IrCommandStore extends IrCommand
{
	public String varName;
	public String name;
	public Temp src;

	// public boolean inFunc = false;
	// public List<String> localVars;
	// public List<String> args;
	public boolean isVarGlobal;
	FunctionData funcdata;
	ClassData classData;
	String funcName;

	public boolean inClass = false;
	public String className;
	public List<String> varFields;
	public List<String> funcFields;
	
	public IrCommandStore(String varName, Temp src, int offset, boolean isVarGlobal, FunctionData funcdata, ClassData classData, String funcName)
	{
		this.src = src;
		this.varName = varName + "offset" + offset;
		this.name = varName;

		this.isVarGlobal = isVarGlobal;
		this.funcdata = funcdata;
		this.classData = classData;
		this.funcName = funcName;
	}

	public HashSet<String> tempsUsed() {
		HashSet<String> used = new HashSet<String>();
		used.add("t" + src.getSerialNumber());
		return used;
	}

	public String tempDefined() {
		return null;
	}

	public HashSet<Temp> temps() {
		HashSet<Temp> temps = new HashSet<Temp>();
		temps.add(src);
		return temps;
	}

	public String getVar() {
		return varName;
	}

	public void mipsMe()
	{
		if (isVarGlobal) {
			MipsGenerator.getInstance().store(varName, src);
		} else if (classData != null) {
			if (funcdata != null && funcdata.localVars.contains(varName)) {
				MipsGenerator.getInstance().storeLocal(src, funcdata.localVars.indexOf(varName));
			} else if (funcdata != null && classData.vars.contains(varName)) {
				MipsGenerator.getInstance().storeField(src, classData.vars.indexOf(varName));
			}
		} else if (funcdata != null) {
			if (funcdata.localVars.contains(varName)) {
				MipsGenerator.getInstance().storeLocal(src, funcdata.localVars.indexOf(varName));
			} else if (funcdata.args.contains(varName)) {
				MipsGenerator.getInstance().storeArg(src, funcdata.args.indexOf(varName));
			}
		} else {
			System.out.println("Error: cant access field " + varName + "!\n" );
		}
	} 

	public void printMe(PrintWriter fileWriter)
	{
		fileWriter.format("%s := t%d\n", varName, src.getSerialNumber());
	}
}
