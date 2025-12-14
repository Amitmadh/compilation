package ast;

public abstract class AstDec extends AstNode
{
    protected AstDec(int line) {
        super(line);
    }
    public abstract void semantMe() throws SemanticException;
}