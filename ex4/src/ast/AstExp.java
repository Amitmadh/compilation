package ast;
import types.*;


public abstract class AstExp extends AstNode
{
    protected AstExp(int line) {
        super(line);
    }
    public abstract Type semantMe() throws SemanticException;
}