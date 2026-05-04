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

public class IrCommandBinopEqStrings extends IrCommand
{
	public Temp t1;
	public Temp t2;
	public Temp dst;

	public IrCommandBinopEqStrings(Temp dst, Temp t1, Temp t2)
	{
		this.dst = dst;
		this.t1 = t1;
		this.t2 = t2;
	}

	public HashSet<String> tempsUsed() {
		HashSet<String> used = new HashSet<String>();
		used.add("t" + t1.getSerialNumber());
		used.add("t" + t2.getSerialNumber());
		return used;
	}

	public String tempDefined() {
		return "t" + dst.getSerialNumber();
	}

	public HashSet<Temp> temps() {
		HashSet<Temp> temps = new HashSet<Temp>();
		temps.add(dst);
		temps.add(t1);
		temps.add(t2);
		return temps;
	}

	public void mipsMe()
	{
		MipsGenerator.getInstance().eqStrings(dst, t1, t2);
	}

	public void printMe(PrintWriter fileWriter)
	{
		fileWriter.print("t" + dst.getSerialNumber() + " = eq t" + t1.getSerialNumber() + ", t" + t2.getSerialNumber() + "\n");
	}
}
