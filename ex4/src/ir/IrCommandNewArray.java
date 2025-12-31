/***********/
/* PACKAGE */
/***********/
package ir;

import temp.Temp;
import java.io.PrintWriter;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */
/*******************/

public class IrCommandNewArray extends IrCommand
{
	Temp varName;
	Temp size;
	String type;
	
	public IrCommandNewArray(Temp varName, Temp size, String type)
	{	this.varName = varName;
		this.size = size;
		this.type = type;
	}

	public void printMe(PrintWriter fileWriter)
	{
		fileWriter.format("t%d := NEW ARRAY t%d , %s\n", varName.getSerialNumber(), size.getSerialNumber(), type);
	}
}
