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

public class IrCommandNewObject extends IrCommand
{
	Temp varName;
	String type;
	
	public IrCommandNewObject(Temp varName, String type)
	{	this.varName = varName;
		this.type = type;
	}

	public void printMe(PrintWriter fileWriter)
	{
		fileWriter.format("t%d := NEW OBJECT %s\n", varName.getSerialNumber(), type);
	}
}
