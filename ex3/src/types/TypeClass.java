package types;

public class TypeClass extends Type
{
	/*********************************************************************/
	/* If this class does not extend a father class this should be null  */
	/*********************************************************************/
	public TypeClass father;

	/**************************************************/
	/* Gather up all data members in one place        */
	/* Note that data members coming from the AST are */
	/* packed together with the class methods         */
	/**************************************************/
	public TypeClassVarDecList dataMembers;
	
	/****************/
	/* CTROR(S) ... */
	/****************/
	public TypeClass(TypeClass father, String name, TypeClassVarDecList dataMembers)
	{
		this.name = name;
		this.father = father;
		this.dataMembers = dataMembers;
	}

	/***************/
	/* isClass() */
	/***************/
	@Override
	public boolean isClass(){ return true;}	

	public boolean isSubClassOf(TypeClass other) {
		if (other == null) return false;
		TypeClass curr = this;
		while (curr != null) {
			if (curr.name != null && curr.name.equals(other.name)) {
				return true;
			}
			curr = curr.father;
		}
		return false;
	}
}
