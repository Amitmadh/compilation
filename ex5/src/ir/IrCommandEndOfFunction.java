package ir;

import java.util.HashSet;
import java.util.List;

import data.ClassData;
import data.FunctionData;
import mips.MipsGenerator;
import temp.Temp;

public class IrCommandEndOfFunction extends IrCommand
{
    String funcName;

    FunctionData funcData;
    ClassData classData;

    public IrCommandEndOfFunction(String funcName, FunctionData funcData, ClassData classData)
    {
        this.funcName = funcName;

        this.funcData = funcData;
        this.classData = classData;
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

    public String getFunctionName() {
        return funcName;
    }

    public void mipsMe()
    {
        if (classData != null) {
			int index = classData.methodsNoClass.indexOf(funcName);
			String method = classData.methods.get(index);
			MipsGenerator.getInstance().funcEpilogue(method);
		} else if (funcData != null) {
			MipsGenerator.getInstance().funcEpilogue(funcName);
		}
    }

    public void printMe(java.io.PrintWriter fileWriter)
    {
        fileWriter.print("END OF FUNCTION " + funcName + "\n");
    }
}
