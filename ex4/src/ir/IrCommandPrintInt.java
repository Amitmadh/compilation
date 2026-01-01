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

public class IrCommandPrintInt extends IrCommand
{
	Temp t;
	
	public IrCommandPrintInt(Temp t)
	{
		this.t = t;
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
		fileWriter.format("PRINT t%d\n", t.getSerialNumber());
	}
}
