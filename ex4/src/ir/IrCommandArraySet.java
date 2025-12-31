/***********/
/* PACKAGE */
/***********/
package ir;

import java.io.PrintWriter;
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

    public void printMe(PrintWriter fileWriter)
    {
        fileWriter.format("ARRAY_SET t%d, t%d, t%d\n",
                arrayAddr.getSerialNumber(), index.getSerialNumber(), src.getSerialNumber());
    }
}
