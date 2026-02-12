/***********/
/* PACKAGE */
/***********/
package ir;

import java.io.PrintWriter;
import java.util.HashSet;

import temp.*;

public class IrCommandArraySet extends IrCommand
{
    Temp arrayAddr;
    Temp index;
    Temp src;

    public IrCommandArraySet(Temp arrayAddr, Temp index, Temp src)
    {
        this.arrayAddr = arrayAddr;
        this.index = index;
        this.src = src;
    }

    public HashSet<String> tempsUsed() {
		HashSet<String> used = new HashSet<String>();
		used.add("t" + arrayAddr.getSerialNumber());
		used.add("t" + index.getSerialNumber());
        used.add("t" + src.getSerialNumber());
        return used;
	}

	public String tempDefined() {
		return null;
	}

    public void printMe(PrintWriter fileWriter)
    {
        fileWriter.format("ARRAY_SET t%d, t%d, t%d\n",
                arrayAddr.getSerialNumber(), index.getSerialNumber(), src.getSerialNumber());
    }
}
