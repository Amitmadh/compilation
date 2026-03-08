/***********/
/* PACKAGE */
/***********/
package ir;

import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;

import ast.AstProgram;
import data.ClassData;
import mips.MipsGenerator;
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
	Temp returnValue;
	String className;

	// public List<String> vars;
	// public List<String> funcs;

	public IrCommandCallMethod(Temp returnValue, String className, Temp baseClass, String methodName, List<Temp> args)
	{
		this.returnValue = returnValue;
		this.className = className;
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
		if (returnValue != null) {
			return "t" + returnValue.getSerialNumber();
		}
		return null;
	}

	public HashSet<Temp> temps() {
		HashSet<Temp> temps = new HashSet<Temp>();
		temps.add(baseClass);
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
		ClassData data = AstProgram.classes.get(className);
		MipsGenerator.getInstance().callMethod(baseClass, data.methodsNoClass.indexOf(methodName), args, returnValue, returnValue != null);
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
