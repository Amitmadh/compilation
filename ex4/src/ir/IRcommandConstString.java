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

public class IRcommandConstString extends IrCommand
{
	Temp t;
	String value;
	
	public IRcommandConstString(Temp t, String value)
	{
		this.t = t;
		this.value = value;
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
		fileWriter.format("t%d := %s\n", t.getSerialNumber(), value);
	}
}
