/***********/
/* PACKAGE */
/***********/
package ir;

import java.io.PrintWriter;
import temp.*;

public class IrCommandJumpIfEqToZero extends IrCommand
{
	Temp t;
	public String labelName;
	
	public IrCommandJumpIfEqToZero(Temp t, String labelName)
	{
		this.t          = t;
		this.labelName = labelName;
	}

	public void printMe(PrintWriter fileWriter)
	{
		fileWriter.format("IF t%d == 0 GOTO %s\n", t.getSerialNumber(), labelName);
	}
}
