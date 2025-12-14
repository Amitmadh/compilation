package ast;
import types.*;
public abstract class AstStmt extends AstNode
{
    public abstract Type semantMe() throws SemanticException;
}
