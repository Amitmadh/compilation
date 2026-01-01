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
import java.io.PrintWriter;
import temp.*;

public class IrCommandStore extends IrCommand
{
	String varName;
	Temp dst;
	public int offset;
	
	public IrCommandStore(String varName, Temp dst, int offset)
	{
		this.dst      = dst	;
		this.varName = varName;
		this.offset = offset;
	}

	public void printMe(PrintWriter fileWriter)
	{
		fileWriter.format("%s = t%d\n", varName, dst.getSerialNumber());
	}
}
