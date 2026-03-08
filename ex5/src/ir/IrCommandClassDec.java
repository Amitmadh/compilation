/***********/
/* PACKAGE */
/***********/
package ir;

import java.io.PrintWriter;
import java.util.HashSet;

import data.ClassData;
import mips.MipsGenerator;
import temp.Temp;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */
/*******************/

public class IrCommandClassDec extends IrCommand
{
	public String className;
	public String extendName;

	public ClassData classData;

	// public List<String> varFields;
	// public List<String> funcFields;
	
	public IrCommandClassDec(String className, String extendsName, ClassData classData)
	{
		this.className = className;
		this.extendName = extendsName;

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

	public String getClassName() {
		return className;
	}

	public void mipsMe()
	{
		MipsGenerator.getInstance().classDec(className, classData.methods);
	}

	public void printMe(PrintWriter fileWriter)
	{
		fileWriter.format("CLASS %s :", className);
		if (extendName != null) {
			fileWriter.format("EXTENDS %s :", extendName);
		}
		fileWriter.format("\n");
		fileWriter.format("VAR FIELDS: ");
		for (String var : classData.vars) {
			fileWriter.format("%s, ", var);
		}
		fileWriter.format("\n");
		fileWriter.format("FUNC FIELDS: ");
		for (String meth : classData.methods) {
			fileWriter.format("%s, ", meth);
		}
		fileWriter.format("\n");
	}
}
