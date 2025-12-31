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

public class IrCommandArrayAccess extends IrCommand
{
	Temp dst;
	Temp arrayAddr;
	Temp index;
	
	public IrCommandArrayAccess(Temp dst, Temp arrayAddr, Temp index)
	{
		this.dst = dst;
		this.arrayAddr = arrayAddr;
		this.index = index;
	}

	public void printMe(PrintWriter fileWriter)
	{
		fileWriter.print(dst + "=  ARRAY_ACCESS t" + arrayAddr.getSerialNumber() + ", t" + index.getSerialNumber() + "\n");
	}
}
