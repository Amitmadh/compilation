package types;

public abstract class Type
{
	/******************************/
	/*  Every type has a name ... */
	/******************************/
	public String name;

	/*************/
	/* isClass() */
	/*************/
	public boolean isClass(){ return false;}

	/*************/
	/* isArray() */
	/*************/
	public boolean isArray(){ return false;}
	
	/*************/
	/* isFunction() */
	/*************/
	public boolean isFunction(){ return false;}
	
	/*************/
	/* isInt() */
	/*************/
	public boolean isInt(){ return false;}

	/*************/
	/* isString() */
	/*************/
	public boolean isString(){ return false;}

}
