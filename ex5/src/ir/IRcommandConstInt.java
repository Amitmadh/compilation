/***********/
/* PACKAGE */
/***********/
package ir;

import java.io.PrintWriter;
import java.util.HashSet;
import java.util.function.Function;

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

public class IRcommandConstInt extends IrCommand
{
	Temp t;
	int value;

	FunctionData funcData;
	ClassData classData;
	
	public IRcommandConstInt(Temp t, int value, FunctionData funcData, ClassData classData)
	{
		this.t = t;
		this.value = value;

		this.funcData = funcData;
		this.classData = classData;
	}

	public HashSet<String> tempsUsed() {
		HashSet<String> used = new HashSet<String>();
		return used;
	}

	public String tempDefined() {
		return "t" + t.getSerialNumber();
	}

	public HashSet<Temp> temps() {
		HashSet<Temp> temps = new HashSet<Temp>();
		temps.add(t);
		return temps;
	}

	public void mipsMe()
	{
		if ((classData == null) || (funcData != null)) {
			MipsGenerator.getInstance().li(t,value);
		}
	}

	public void printMe(PrintWriter fileWriter)
	{
		fileWriter.format("t%d := %d\n", t.getSerialNumber(), value);
	}
}
