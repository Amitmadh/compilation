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
		} else if (classData != null && funcdata != null) {
			// In a method
			if (funcdata.localVars.contains(varName)) {
				MipsGenerator.getInstance().loadLocal(dst, funcdata.localVars.indexOf(varName));
			} else if (funcdata.args.contains(varName)) {
				// Method parameter - use offset 12 + index*4
				MipsGenerator.getInstance().loadArg(dst, funcdata.args.indexOf(varName), true);
			} else {
				// Try to find in class fields using varsNoOffset
				String fieldNameNoOffset = varName.substring(0, varName.lastIndexOf("offset"));
				if (classData.varsNoOffset.contains(fieldNameNoOffset)) {
					// Class field - use index from varsNoOffset
					int fieldIndex = classData.varsNoOffset.indexOf(fieldNameNoOffset);
					MipsGenerator.getInstance().loadField(dst, fieldIndex);
				}
			}
		} else if (funcdata != null) {
			// In a function (not a method)
			if (funcdata.localVars.contains(varName)) {
				MipsGenerator.getInstance().loadLocal(dst, funcdata.localVars.indexOf(varName));
			} else if (funcdata.args.contains(varName)) {
				// Function parameter - use offset 8 + index*4
				MipsGenerator.getInstance().loadArg(dst, funcdata.args.indexOf(varName), false);
			}
		} else if (classData != null) {
			// In a class but not directly in a function (shouldn't normally happen)
			String fieldNameNoOffset = varName.substring(0, varName.lastIndexOf("offset"));
			if (classData.varsNoOffset.contains(fieldNameNoOffset)) {
				int fieldIndex = classData.varsNoOffset.indexOf(fieldNameNoOffset);
				MipsGenerator.getInstance().loadField(dst, fieldIndex);
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
