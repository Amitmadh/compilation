package ir;

import java.io.PrintWriter;
import java.util.HashSet;

import mips.MipsGenerator;
import temp.Temp;

public class IrCommandLoadThis extends IrCommand
{
	Temp dst;

	public IrCommandLoadThis(Temp dst)
	{
		this.dst = dst;
	}

	public HashSet<String> tempsUsed() {
		return new HashSet<String>();
	}

	public String tempDefined() {
		return "t" + dst.getSerialNumber();
	}

	public HashSet<Temp> temps() {
		HashSet<Temp> temps = new HashSet<Temp>();
		temps.add(dst);
		return temps;
	}

	public void mipsMe()
	{
		MipsGenerator.getInstance().loadThis(dst);
	}

	public void printMe(PrintWriter fileWriter)
	{
		fileWriter.format("t%d = this\n", dst.getSerialNumber());
	}
}
