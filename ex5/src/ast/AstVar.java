package ast;
import java.util.List;

import data.ClassData;
import data.FunctionData;
import types.*;
public abstract class AstVar extends AstNode
{
    Type type;

    //annotations
    String funcName;
    
    protected AstVar(int line) {
        super(line);
    }

    public abstract Type semantMe() throws SemanticException;

    public abstract void setGlobalVarData(List<String> globalVars);
    public abstract void setFunctionData(FunctionData data);
    public abstract void setClassData(ClassData data);
}
