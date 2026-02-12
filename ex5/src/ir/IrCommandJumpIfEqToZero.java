/***********/
/* PACKAGE */
/***********/
package ir;

import java.io.PrintWriter;
import java.util.HashSet;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */
/*******************/
import temp.*;

public class IrCommandJumpIfEqToZero extends IrCommand
{
	Temp t;
	public String labelName;
	
	public IrCommandJumpIfEqToZero(Temp t, String labelName)
	{
		this.t          = t;
		this.labelName = labelName;
	}

	public HashSet<String> tempsUsed() {
		HashSet<String> used = new HashSet<String>();
		used.add("t" + t.getSerialNumber());
		return used;
	}

	public String tempDefined() {
		return null;
	}

	public void printMe(PrintWriter fileWriter)
	{
		fileWriter.format("IF t%d == 0 GOTO %s\n", t.getSerialNumber(), labelName);
	}
}
