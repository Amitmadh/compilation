/***********/
/* PACKAGE */
/***********/
package ir;

import temp.Temp;
import java.io.PrintWriter;
import java.util.HashSet;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */
/*******************/

public class IrCommandNewObject extends IrCommand
{
	Temp t;
	String type;
	
	public IrCommandNewObject(Temp t, String type)
	{	this.t = t;
		this.type = type;
	}

	public HashSet<String> tempsUsed() {
		HashSet<String> used = new HashSet<String>();
		return used;
	}

	public String tempDefined() {
		return "t" + t.getSerialNumber();
	}

	public void printMe(PrintWriter fileWriter)
	{
		fileWriter.format("t%d := NEW OBJECT %s\n", t.getSerialNumber(), type);
	}
}
