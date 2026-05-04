/***********/
/* PACKAGE */
/***********/
package ir;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */
/*******************/

public class IrCommandList
{
	public IrCommand head;
	public IrCommandList tail;

	IrCommandList(IrCommand head, IrCommandList tail)
	{
		this.head = head;
		this.tail = tail;
	}

	public void mipsMe()
	{
		allocGlobs();
		mipsNotGlobals();
	}

	public void mipsNotGlobals()
	{
		if (head != null && !head.isGlobal) head.mipsMe();
		if (tail != null) tail.mipsNotGlobals();
	}

	public void allocGlobs()
	{
		if (head != null)
		{
			if (head instanceof IrCommandAllocate && head.isGlobal) {
				((IrCommandAllocate)head).mipsMe();
			}
		}
		if (tail != null) tail.allocGlobs();
	}

	public void initGlobs()
	{
		if (head != null)
		{
			if (!(head instanceof IrCommandAllocate) && head.isGlobal) {
				head.mipsMe();
			}
		}
		if (tail != null) tail.initGlobs();
	}
}
