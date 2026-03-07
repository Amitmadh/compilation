/***********/
/* PACKAGE */
/***********/
package ir;

import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;

import data.ClassData;
import data.FunctionData;
import mips.MipsGenerator;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */
/*******************/
import temp.Temp;

public class IrCommandLoad extends IrCommand
{
	Temp dst;
	String varName;

	// public boolean inFunc = false;
	// public List<String> localVars;
	// public List<String> args;
	public boolean isVarGlobal;
	FunctionData funcdata;
	ClassData classData;

	// public boolean inClass = false;
	// public String className;
	// public List<String> varFields;
	// public List<String> funcFields;
	
	public IrCommandLoad(Temp dst, String varName, int offset, boolean isVarGlobal, FunctionData funcData, ClassData classData)
	{
		this.dst      = dst;
		this.varName = varName + "offset" + offset;

		this.isVarGlobal = isVarGlobal;
		this.funcdata = funcData;
		this.classData = classData;
	}

	public HashSet<String> tempsUsed() {
		HashSet<String> used = new HashSet<String>();
		return used;
	}

	public String tempDefined() {
		return "t" + dst.getSerialNumber();
	}

	public HashSet<Temp> temps() {
		HashSet<Temp> temps = new HashSet<Temp>();
		temps.add(dst);
		return temps;
	}

	public String getVar() {
		return varName;
	}

	public void mipsMe()
	{
		if (isVarGlobal) {
			MipsGenerator.getInstance().load(dst, varName);
		} else if (classData != null) {
			if (funcdata != null && funcdata.localVars.contains(varName)) {
				MipsGenerator.getInstance().loadLocal(dst, funcdata.localVars.indexOf(varName));
			} else if (funcdata != null && classData.vars.contains(varName)) {
				MipsGenerator.getInstance().loadField(dst, classData.vars.indexOf(varName));
			}
		} else if (funcdata != null) {
			if (funcdata.localVars.contains(varName)) {
				MipsGenerator.getInstance().loadLocal(dst, funcdata.localVars.indexOf(varName));
			} else if (funcdata.args.contains(varName)) {
				MipsGenerator.getInstance().loadArg(dst, funcdata.args.indexOf(varName));
			}
		} else {
			System.out.println("Error: cant access field " + varName + "!\n" );
		}
	}

	public void printMe(PrintWriter fileWriter)
	{
		fileWriter.format("t%d = %s\n", dst.getSerialNumber(), varName);
	}
}
