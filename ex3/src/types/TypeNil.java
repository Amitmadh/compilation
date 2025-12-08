package types;

public class TypeNil extends Type
{
    public Type elemType;
    
    /****************/
    /* CTROR(S) ... */
    /****************/
    public TypeNil(String name)
    {
        this.name = name;
    }

    /*************/
    /* isArray() */
    /*************/
    @Override
    public boolean isArray(){ return true;}
    
}
