/***********/
/* PACKAGE */
/***********/
package ir;

import java.io.PrintWriter;
import java.util.HashSet;

import mips.MipsGenerator;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */
/*******************/
import temp.Temp;

public class IrCommandLoadArg extends IrCommand
{
	public String varName;
	int index;
	
	public IrCommandLoadArg(String varName, int offset, int index)
	{
		this.varName = varName + "offset" + offset;
		this.index = index;
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
		return;
	}

	public void printMe(PrintWriter fileWriter)
	{
		fileWriter.format("load arg%d\n", index);
	}
}
