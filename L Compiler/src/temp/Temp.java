/***********/
/* PACKAGE */
/***********/
package temp;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */
/*******************/

public class Temp
{
	private int serial=0;
	private int register=-1;
	
	public Temp(int serial)
	{
		this.serial = serial;
	}
	
	public int getRegister()
	{
		return register;
	}
	
	public void setRegister(int register)
	{
		this.register = register;
	}
	
	public int getSerialNumber()
	{
		return serial;
	}
}
