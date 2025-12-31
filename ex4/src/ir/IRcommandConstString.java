/***********/
/* PACKAGE */
/***********/
package ir;

import java.io.PrintWriter;

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

	public void printMe(PrintWriter fileWriter)
	{
		fileWriter.format("t%d := %s\n", t.getSerialNumber(), value);
	}
}
