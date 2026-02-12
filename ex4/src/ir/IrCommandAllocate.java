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

public class IrCommandAllocate extends IrCommand
{
	String varName;
	
	public IrCommandAllocate(String varName, int offset)
	{
		this.varName = varName + "offset" + offset;
	}

	public HashSet<String> tempsUsed() {
		HashSet<String> used = new HashSet<String>();
		return used;
	}

	public String tempDefined() {
		return null;
	}

	public String getVar() {
		return varName;
	}

	public void printMe(PrintWriter fileWriter)
	{
		fileWriter.print("ALLOCATE " + varName + "\n");
	}
}
