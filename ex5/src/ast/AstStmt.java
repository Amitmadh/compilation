package ast;
import types.*;
public abstract class AstStmt extends AstNode
{
    protected AstStmt(int line) {
        super(line);
    }

    public abstract Type semantMe() throws SemanticException;
}
