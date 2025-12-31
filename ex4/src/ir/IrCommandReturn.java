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

public class IrCommandReturn extends IrCommand
{
	Temp t;
	
	public IrCommandReturn(Temp t)
	{
		this.t = t;
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
