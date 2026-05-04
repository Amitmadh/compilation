/***********/
/* PACKAGE */
/***********/
package ir;

import java.io.PrintWriter;
import java.util.HashSet;

import mips.MipsGenerator;
import temp.Temp;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */
/*******************/

public class IrCommandLabel extends IrCommand
{
	public String labelName;

	boolean isFunction = false;
	boolean isClass = false;
	
	public IrCommandLabel(String labelName, boolean isFunction, boolean isClass)
	{
		this.labelName = labelName;
		this.isFunction = isFunction;
		this.isClass = isClass;
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

	public void mipsMe()
	{
		if (isFunction) {
			MipsGenerator.getInstance().funcLabel(labelName);
		} else if (isClass) {
			MipsGenerator.getInstance().classLabel(labelName);
		} else {
			MipsGenerator.getInstance().label(labelName);
		}
	}

	public void printMe(PrintWriter fileWriter)
	{
		fileWriter.format("LABEL %s :\n", labelName);
	}
}
