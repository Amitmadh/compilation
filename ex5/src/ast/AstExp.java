package ast;
import java.util.List;

import data.ClassData;
import data.FunctionData;
import types.*;


public abstract class AstExp extends AstNode
{
    public Type type;

    //annotations
    String funcName;
    
    protected AstExp(int line) {
        super(line);
    }

    public abstract void setGlobalVarData(List<String> globalVars);
    public abstract void setFunctionData(FunctionData data);
    public abstract void setClassData(ClassData data);

    public abstract Type semantMe() throws SemanticException;
}