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

	public HashSet<String> tempsUsed() {
		HashSet<String> used = new HashSet<String>();
		used.add("t" + arrayAddr.getSerialNumber());
		used.add("t" + index.getSerialNumber());
		return used;
	}

	public String tempDefined() {
		return "t" + dst.getSerialNumber();
	}

	public void printMe(PrintWriter fileWriter)
	{
		fileWriter.print(dst + "=  ARRAY_ACCESS t" + arrayAddr.getSerialNumber() + ", t" + index.getSerialNumber() + "\n");
	}
}
