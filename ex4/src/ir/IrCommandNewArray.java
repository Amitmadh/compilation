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

public class IrCommandNewArray extends IrCommand
{
	Temp t;
	Temp size;
	String type;
	
	public IrCommandNewArray(Temp t, Temp size, String type)
	{	this.t = t;
		this.size = size;
		this.type = type;
	}

	public HashSet<String> tempsUsed() {
		HashSet<String> used = new HashSet<String>();
		used.add("t" + size.getSerialNumber());
		return used;
	}

	public String tempDefined() {
		return "t" + t.getSerialNumber();
	}

	public void printMe(PrintWriter fileWriter)
	{
		fileWriter.format("t%d := NEW ARRAY t%d , %s\n", t.getSerialNumber(), size.getSerialNumber(), type);
	}
}
