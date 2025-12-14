package ast;

public abstract class AstDec extends AstNode
{
    public abstract void semantMe() throws SemanticException;
}