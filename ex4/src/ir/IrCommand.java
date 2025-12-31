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

public abstract class IrCommand
{
	/*****************/
	/* Label Factory */
	/*****************/
	protected static int labelCounter = 0;
	public    static String getFreshLabel(String msg)
	{
		return String.format("Label_%d_%s", labelCounter++,msg);
	}
	public void printMe(PrintWriter fileWriter) {}
}
