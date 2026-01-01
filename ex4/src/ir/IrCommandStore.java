/***********/
/* PACKAGE */
/***********/
package ir;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */
/*******************/
import temp.*;
import java.io.PrintWriter;
import java.util.HashSet;

public class IrCommandStore extends IrCommand
{
	String varName;
	Temp src;
	
	public IrCommandStore(String varName, Temp src)
	{
		this.src      = src	;
		this.varName = varName;
	}

	public HashSet<String> tempsUsed() {
		HashSet<String> used = new HashSet<String>();
		used.add("t" + src.getSerialNumber());
		return used;
	}

	public String tempDefined() {
		return null;
	}

	public String getVar() {
		return varName;
	}

	public void printMe(PrintWriter fileWriter)
	{
		fileWriter.format("%s = t%d\n", varName, src.getSerialNumber());
	}
}
