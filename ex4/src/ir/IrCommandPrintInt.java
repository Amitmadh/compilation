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

public class IrCommandPrintInt extends IrCommand
{
	Temp t;
	
	public IrCommandPrintInt(Temp t)
	{
		this.t = t;
	}

	public void printMe(PrintWriter fileWriter)
	{
		fileWriter.format("PRINT t%d\n", t.getSerialNumber());
	}
}
