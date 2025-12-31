/***********/
/* PACKAGE */
/***********/
package ir;

import java.io.PrintWriter;
import temp.*;

public class IrCommandFieldSet extends IrCommand
{
    Temp instanceAddr;
    String fieldName;
    Temp src;

    public IrCommandFieldSet(Temp instanceAddr, String fieldName, Temp src)
    {
        this.instanceAddr = instanceAddr;
        this.fieldName = fieldName;
        this.src = src;
    }

    public void printMe(PrintWriter fileWriter)
    {
        fileWriter.format("FIELD_SET t%d, %s, t%d\n",
                instanceAddr.getSerialNumber(), fieldName, src.getSerialNumber());
    }
}
