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

public class IrCommandFieldAccess extends IrCommand
{
	Temp dst;
	Temp instanceAddr;
	String fieldName;
	
	public IrCommandFieldAccess(Temp dst, Temp instanceAddr, String fieldName)
	{
		this.dst = dst;
		this.instanceAddr = instanceAddr;
		this.fieldName = fieldName;
	}

	public void printMe(PrintWriter fileWriter)
	{
		fileWriter.format("t%d = FIELD_ACCESS t%d, %s\n", dst.getSerialNumber(), instanceAddr.getSerialNumber(), fieldName);
	}
}
