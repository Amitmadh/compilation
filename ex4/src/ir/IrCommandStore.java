/***********/
/* PACKAGE */
/***********/
package ir;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */
/*******************/
import temp.*;
import java.io.PrintWriter;

public class IrCommandStore extends IrCommand
{
	String varName;
	Temp dst;
	
	public IrCommandStore(String varName, Temp dst)
	{
		this.dst      = dst	;
		this.varName = varName;
	}

	public void printMe(PrintWriter fileWriter)
	{
		fileWriter.format("%s = t%d\n", varName, dst.getSerialNumber());
	}
}
