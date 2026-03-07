/***********/
/* PACKAGE */
/***********/
package ir;

import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;

import ast.AstProgram;
import data.ClassData;
import mips.MipsGenerator;
import temp.*;

public class IrCommandFieldSet extends IrCommand
{
    Temp instanceAddr;
    String fieldName;
    Temp src;
	String className;

	// public List<String> vars;
	// public List<String> funcs;

    public IrCommandFieldSet(Temp instanceAddr, String fieldName, Temp src, String className)
    {
        this.instanceAddr = instanceAddr;
        this.fieldName = fieldName;
        this.src = src;
		this.className = className;
    }
    
	public HashSet<String> tempsUsed() {
		HashSet<String> used = new HashSet<String>();
		used.add("t" + instanceAddr.getSerialNumber());
		used.add("t" + src.getSerialNumber());
		return used;
	}

	public String tempDefined() {
		return null;
	}

    public HashSet<Temp> temps() {
		HashSet<Temp> temps = new HashSet<Temp>();
		temps.add(instanceAddr);
		temps.add(src);
		return temps;
	}

    public void mipsMe()
	{
		ClassData data = AstProgram.classes.get(className);
		if (!data.varsNoOffset.contains(fieldName)) {
			System.out.println("Error: field " + fieldName + " not found in class " + className);
			return;
		}
	 	MipsGenerator.getInstance().fieldSet(instanceAddr, data.varsNoOffset.indexOf(fieldName), src);
	}

    public void printMe(PrintWriter fileWriter)
    {
        fileWriter.format("FIELD_SET t%d, %s, t%d, class %s\n",
                instanceAddr.getSerialNumber(), fieldName, src.getSerialNumber(), className);
	}
}
