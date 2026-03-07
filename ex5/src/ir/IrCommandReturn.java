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
import temp.*;

public class IrCommandReturn extends IrCommand
{
	Temp t;
	// public String funcName;
	public FunctionData funcData;
	public ClassData classData;
	public String funcName;
	
	public IrCommandReturn(Temp t, FunctionData funcData, ClassData classData, String funcName)
	{
		this.t = t;
		this.funcData = funcData;
		this.classData = classData;
		this.funcName = funcName;
	}

	public HashSet<String> tempsUsed() {
		HashSet<String> used = new HashSet<String>();
		if (t != null) {
			used.add("t" + t.getSerialNumber());
		}
		return used;
	}

	public String tempDefined() {
		return null;
	}

	public HashSet<Temp> temps() {
		HashSet<Temp> temps = new HashSet<Temp>();
		if (t != null) {
			temps.add(t);
		}
		return temps;
	}

	public void mipsMe()
	{
		if (funcData != null) {
			MipsGenerator.getInstance().returnValue(t, funcData.funcName);
		}
	}

	public void printMe(PrintWriter fileWriter)
	{
		if (t == null) {
			fileWriter.format("RETURN\n");
		} else {
			fileWriter.format("RETURN t%d\n", t.getSerialNumber());
		}
	}
}
