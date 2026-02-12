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

public class IrCommandLabel extends IrCommand
{
	public String labelName;
	
	public IrCommandLabel(String labelName)
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

	public void printMe(PrintWriter fileWriter)
	{
		fileWriter.format("LABEL %s :\n", labelName);
	}
}
