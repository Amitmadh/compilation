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

public class IrCommandReturn extends IrCommand
{
	Temp t;
	
	public IrCommandReturn(Temp t)
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
		if (t == null) {
			fileWriter.format("RETURN\n");
		} else {
			fileWriter.format("RETURN t%d\n", t.getSerialNumber());
		}
	}
}
