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

public class IRcommandConstInt extends IrCommand
{
	Temp t;
	int value;
	
	public IRcommandConstInt(Temp t, int value)
	{
		this.t = t;
		this.value = value;
	}

	public void printMe(PrintWriter fileWriter)
	{
		fileWriter.format("t%d := %d\n", t.getSerialNumber(), value);
	}
}
