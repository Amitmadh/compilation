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

public class IrCommandBinopDivIntegers extends IrCommand
{
	public Temp t1;
	public Temp t2;
	public Temp dst;
	
	public IrCommandBinopDivIntegers(Temp dst, Temp t1, Temp t2)
	{
		this.dst = dst;
		this.t1 = t1;
		this.t2 = t2;
	}

	public void printMe(PrintWriter fileWriter)
	{
		fileWriter.print("t" + dst.getSerialNumber() + " = div t" + t1.getSerialNumber() + ", t" + t2.getSerialNumber() + "\n");
	}
}
