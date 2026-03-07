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

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */
/*******************/
import temp.*;

public class IrCommandFieldAccess extends IrCommand
{
	Temp dst;
	Temp instanceAddr;
	String className;
	String fieldName;

	// public List<String> vars;
	// public List<String> funcs;
	
	public IrCommandFieldAccess(Temp dst, Temp instanceAddr,String className, String fieldName)
	{
		this.dst = dst;
		this.instanceAddr = instanceAddr;
		this.fieldName = fieldName;
		this.className = className;
	}

	public HashSet<String> tempsUsed() {
		HashSet<String> used = new HashSet<String>();
		used.add("t" + instanceAddr.getSerialNumber());
		return used;
	}

	public String tempDefined() {
		return "t" + dst.getSerialNumber();
	}

	public HashSet<Temp> temps() {
		HashSet<Temp> temps = new HashSet<Temp>();
		temps.add(dst);
		temps.add(instanceAddr);
		return temps;
	}

	public void mipsMe()
	{
		ClassData data = AstProgram.classes.get(className);
		if (!data.varsNoOffset.contains(fieldName)) {
			System.out.println("Error: field " + fieldName + " not found in class " + className);
			return;
		}
		MipsGenerator.getInstance().fieldAccess(dst, instanceAddr, data.varsNoOffset.indexOf(fieldName));
	}

	public void printMe(PrintWriter fileWriter)
	{
		fileWriter.format("t%d = FIELD_ACCESS t%d, %s class %s\n", dst.getSerialNumber(), instanceAddr.getSerialNumber(), fieldName, className);
	}
}
