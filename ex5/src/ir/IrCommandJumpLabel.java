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

public class IrCommandJumpLabel extends IrCommand
{
	public String labelName;
	
	public IrCommandJumpLabel(String labelName)
	{
		this.labelName = labelName;
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
		MipsGenerator.getInstance().jump(labelName);
	}

	public void printMe(PrintWriter fileWriter)
	{
		fileWriter.format("BR %s :\n", labelName);
	}
}
