/***********/
/* PACKAGE */
/***********/
package ir;

import temp.Temp;
import java.io.PrintWriter;
import java.util.List;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */
/*******************/

public class IrCommandCallFunc extends IrCommand
{
	String funcName;
	List<Temp> args;

	public IrCommandCallFunc(String funcName, List<Temp> args)
	{
		this.funcName = funcName;
		this.args = args;
	}

	public void printMe(PrintWriter fileWriter)
	{
		fileWriter.print("CALL " + funcName);
		for (Temp arg : args) {
			fileWriter.print(", t" + arg.getSerialNumber());
		}
		fileWriter.print("\n");
	}
}
