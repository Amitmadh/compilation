package ast;
import types.*;
public abstract class AstVar extends AstNode
{
    protected AstVar(int line) {
        super(line);
    }

    public abstract Type semantMe() throws SemanticException;
    
}
