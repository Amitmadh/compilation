/***********/
/* PACKAGE */
/***********/
package ir;

import temp.Temp;
import java.io.PrintWriter;
import java.util.HashSet;

import ast.AstProgram;
import data.ClassData;
import mips.MipsGenerator;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */
/*******************/

public class IrCommandNewObject extends IrCommand
{
	Temp t;
	String type;

	// public List<String> vars;
	// public List<String> funcs;

	// public Map<String,Integer> intVals;
	// public Map<String,String> strVals;
	
	public IrCommandNewObject(Temp t, String type)
	{	this.t = t;
		this.type = type;
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
		ClassData data = AstProgram.classes.get(type);
		MipsGenerator.getInstance().newClass(t, data.className, data.vars, data.varsNoOffset, data.intVals, data.strVals);
	}

	public void printMe(PrintWriter fileWriter)
	{
		fileWriter.format("t%d := NEW OBJECT %s\n", t.getSerialNumber(), type);
		ClassData data = AstProgram.classes.get(type);
		fileWriter.format("int vals: %s\n", data.intVals);
		fileWriter.format("str vals: %s\n", data.strVals);
	}
}
