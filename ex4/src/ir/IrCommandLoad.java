/***********/
/* PACKAGE */
/***********/
package ir;

import java.io.PrintWriter;
import temp.*;

public class IrCommandLoad extends IrCommand
{
	Temp dst;
	String varName;
	public int offset;
	
	public IrCommandLoad(Temp dst, String varName, int offset)
	{
		this.dst      = dst;
		this.varName = varName;
		this.offset = offset;
	}

	public void printMe(PrintWriter fileWriter)
	{
		fileWriter.format("t%d = %s\n", dst.getSerialNumber(), varName);
	}
}
