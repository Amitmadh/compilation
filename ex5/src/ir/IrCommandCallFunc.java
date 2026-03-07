/***********/
/* PACKAGE */
/***********/
package ir;

import temp.Temp;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;

import mips.MipsGenerator;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */
/*******************/

public class IrCommandCallFunc extends IrCommand
{
	String funcName;
	List<Temp> args;
	Temp returnValue;

	public IrCommandCallFunc(Temp returnValue, String funcName, List<Temp> args)
	{
		this.returnValue = returnValue;
		this.funcName = funcName;
		this.args = args;
	}

	public HashSet<String> tempsUsed() {
		HashSet<String> used = new HashSet<String>();
		for (Temp t : args) {
			used.add("t" + t.getSerialNumber());
		}
		return used;
	}

	public String tempDefined() {
		if (returnValue != null) {
			return "t" + returnValue.getSerialNumber();
		}
		return null;
	}

	public HashSet<Temp> temps() {
		HashSet<Temp> temps = new HashSet<Temp>();
		for (Temp t : args) {
			temps.add(t);
		}
		if (returnValue != null) {
			temps.add(returnValue);
		}
		return temps;
	}

	public void mipsMe()
	{
		if (funcName.equals("PrintInt")) {
			MipsGenerator.getInstance().printInt(args.get(0));
		} else if (funcName.equals("PrintString")) {
			MipsGenerator.getInstance().printString(args.get(0));
		} else {
			MipsGenerator.getInstance().callFunc(funcName, args, returnValue, returnValue != null);
		}
	}

	public void printMe(PrintWriter fileWriter)
	{
		if (returnValue != null) {
			fileWriter.format("t%d = ",returnValue.getSerialNumber());
		}
		fileWriter.print("CALL " + funcName);
		for (Temp arg : args) {
			fileWriter.print(", t" + arg.getSerialNumber());
		}
		fileWriter.print("\n");
	}
}
