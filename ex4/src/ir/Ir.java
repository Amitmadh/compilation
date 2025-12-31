/***********/
/* PACKAGE */
/***********/
package ir;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */
/*******************/

public class Ir
{
	private IrCommand head=null;
	private IrCommandList tail=null;

	/******************/
	/* Add Ir command */
	/******************/
	public void AddIrCommand(IrCommand cmd)
	{
		if ((head == null) && (tail == null))
		{
			this.head = cmd;
		}
		else if ((head != null) && (tail == null))
		{
			this.tail = new IrCommandList(cmd,null);
		}
		else
		{
			IrCommandList it = tail;
			while ((it != null) && (it.tail != null))
			{
				it = it.tail;
			}
			it.tail = new IrCommandList(cmd,null);
		}
	}

	/**************************************/
	/* USUAL SINGLETON IMPLEMENTATION ... */
	/**************************************/
	private static Ir instance = null;

	/*****************************/
	/* PREVENT INSTANTIATION ... */
	/*****************************/
	protected Ir() {}

	/******************************/
	/* GET SINGLETON INSTANCE ... */
	/******************************/
	public static Ir getInstance()
	{
		if (instance == null)
		{
			/*******************************/
			/* [0] The instance itself ... */
			/*******************************/
			instance = new Ir();
		}
		return instance;
	}
	public IrCommand getHead() {
		return head;
	}
	public IrCommandList getTail() {
		return tail;
	}
	public void printIrCommands(String fileLocation) throws FileNotFoundException
	{
		PrintWriter fileWriter = new PrintWriter(fileLocation);
		if (head != null) head.printMe(fileWriter);
		IrCommandList itList = tail;
		while (itList != null)
		{
			if (itList.head != null) itList.head.printMe(fileWriter);
			itList = itList.tail;
		}
		fileWriter.close();
	}
}
