package types;

public class TypeArray extends Type
{
    public Type elemType;
    
    /****************/
    /* CTROR(S) ... */
    /****************/
    public TypeArray(String name, Type elemType)
    {
        this.name = name;
        this.elemType = elemType;
    }
    
}
