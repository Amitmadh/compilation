/***********/
/* PACKAGE */
/***********/
package ir;

import java.io.PrintWriter;
import java.util.HashSet;

import mips.MipsGenerator;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */
/*******************/
import temp.*;

public class IRCommandNil extends IrCommand
{
	Temp t;

	public boolean inClass = false;
	
	public IRCommandNil(Temp t)
	{
		this.t = t;
	}

	public HashSet<String> tempsUsed() {
		HashSet<String> used = new HashSet<String>();
		return used;
	}

	public String tempDefined() {
		return "t" + t.getSerialNumber();
	}

	public HashSet<Temp> temps() {
		HashSet<Temp> temps = new HashSet<Temp>();
		temps.add(t);
		return temps;
	}

	public void mipsMe()
	{
		if (!inClass) {
			MipsGenerator.getInstance().loadNil(t);
		}
	}

	public void printMe(PrintWriter fileWriter)
	{
		fileWriter.format("t%d := NIL\n", t.getSerialNumber());
	}
}
