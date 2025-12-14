package ast;
import types.*;


public abstract class AstExp extends AstNode
{
    public abstract Type semantMe() throws SemanticException;
}