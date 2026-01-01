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

	public HashSet<String> tempsUsed() {
		HashSet<String> used = new HashSet<String>();
		used.add("t" + instanceAddr.getSerialNumber());
		return used;
	}

	public String tempDefined() {
		return "t" + dst.getSerialNumber();
	}

	public void printMe(PrintWriter fileWriter)
	{
		fileWriter.format("t%d = FIELD_ACCESS t%d, %s\n", dst.getSerialNumber(), instanceAddr.getSerialNumber(), fieldName);
	}
}
