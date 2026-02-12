/***********/
/* PACKAGE */
/***********/
package ir;

import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;

import temp.Temp;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */
/*******************/

public class IrCommandCallMethod extends IrCommand
{
	Temp baseClass;
	String methodName;
	List<Temp> args;

	public IrCommandCallMethod(Temp baseClass, String methodName, List<Temp> args)
	{
		this.baseClass = baseClass;
		this.methodName = methodName;
		this.args = args;
	}

	public HashSet<String> tempsUsed() {
		HashSet<String> used = new HashSet<String>();
		used.add("t" + baseClass.getSerialNumber());
		for (Temp t : args) {
			used.add("t" + t.getSerialNumber());
		}
		return used;
	}

	public String tempDefined() {
		return null;
	}

	public void printMe(PrintWriter fileWriter)
	{
		fileWriter.print("VIRTUAL CALL ,t" + baseClass.getSerialNumber() + "," + methodName);
		for (Temp arg : args) {
			fileWriter.print(", t" + arg.getSerialNumber());
		}
		fileWriter.print("\n");
	}
}
