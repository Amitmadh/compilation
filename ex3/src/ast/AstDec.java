package ast;

import types.TypeClass;

public abstract class AstDec extends AstNode
{
    protected AstDec(int line) {
        super(line);
    }
    public abstract void semantMe(TypeClass classType) throws SemanticException;
}