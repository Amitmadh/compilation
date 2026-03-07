package ast;
import java.util.List;

import types.TypeClass;

public abstract class AstDec extends AstNode
{
    protected AstDec(int line) {
        super(line);
    }

    public abstract void annotateAst();
    public abstract void setGlobalVarData(List<String> globalVars);

    public abstract void semantMe(TypeClass classType) throws SemanticException;
}