/***********/
/* PACKAGE */
/***********/
package ir;

import java.io.PrintWriter;
import java.util.HashSet;

import temp.Temp;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */
/*******************/

public class IrCommandEndOfClass extends IrCommand
{
	public String className;
	public String extendName;
	
	public IrCommandEndOfClass(String className, String extendsName)
	{
		this.className = className;
		this.extendName = extendsName;
	}

	public HashSet<String> tempsUsed() {
		HashSet<String> used = new HashSet<String>();
		return used;
	}

	public String tempDefined() {
		return null;
	}

	public HashSet<Temp> temps() {
		HashSet<Temp> temps = new HashSet<Temp>();
		return temps;
	}

	public String getClassName() {
		return className;
	}

	public void mipsMe()
	{
		//to do
	}

	public void printMe(PrintWriter fileWriter)
	{
		fileWriter.format("END CLASS %s\n", className);
	}
}
