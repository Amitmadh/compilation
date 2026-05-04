/***********/
/* PACKAGE */
/***********/
package ir;

import java.io.PrintWriter;
import java.util.HashSet;

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

public class IRcommandConstString extends IrCommand
{
	Temp t;
	String value;
	int index;

	FunctionData funcData;
	ClassData classData;
	
	public IRcommandConstString(Temp t, String value, FunctionData funcData, ClassData classData)
	{
		this.t = t;
		this.value = value;

		this.funcData = funcData;
		this.classData = classData;

		index = MipsGenerator.strings.size();
		MipsGenerator.strings.add(value);
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
			MipsGenerator.getInstance().la(t, index);
		}
	}

	public void printMe(PrintWriter fileWriter)
	{
		fileWriter.format("t%d := %s\n", t.getSerialNumber(), value);
	}
}
