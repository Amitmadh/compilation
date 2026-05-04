package ast;
import java.util.List;

import data.ClassData;
import data.FunctionData;
import types.*;
public abstract class AstStmt extends AstNode
{
    protected AstStmt(int line) {
        super(line);
    }

    List<String> varDecs;
    String funcName;

    public abstract void annotateAst();
    public abstract void setGlobalVarData(List<String> globalVars);
    public abstract void setFunctionData(FunctionData data);
    public abstract void setClassData(ClassData data);

    public abstract Type semantMe() throws SemanticException;
}
