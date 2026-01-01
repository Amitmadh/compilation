/***********/
/* PACKAGE */
/***********/
package symboltable;

/*******************/
/* GENERAL IMPORTS */
/*******************/
import java.io.PrintWriter;
import types.*;

/****************/
/* SYMBOL TABLE */
/****************/
public class SymbolTable
{
	private int hashArraySize = 13;
	private int scopeDepth = 0;
	
	/**********************************************/
	/* The actual symbol table data structure ... */
	/**********************************************/
	private SymbolTableEntry[] table = new SymbolTableEntry[hashArraySize];
	private SymbolTableEntry top;
	private int topIndex = 0;
	private Type currentFunctionReturnType = null;

	private int globalOffsetCounter = 0;
    private int localOffsetCounter = 0;
    private int scopeLevel = 0;
	
	/**************************************************************/
	/* A very primitive hash function for exposition purposes ... */
	/**************************************************************/
	private int hash(String s)
	{
		if (s.charAt(0) == 'l') {return 1;}
		if (s.charAt(0) == 'm') {return 1;}
		if (s.charAt(0) == 'r') {return 3;}
		if (s.charAt(0) == 'i') {return 6;}
		if (s.charAt(0) == 'd') {return 6;}
		if (s.charAt(0) == 'k') {return 6;}
		if (s.charAt(0) == 'f') {return 6;}
		if (s.charAt(0) == 'S') {return 6;}
		return 12;
	}

	/****************************************************************************/
	/* Enter a variable, function, class type or array type to the symbol table */
	/****************************************************************************/
	public void enter(String name, Type t)
	{
		/*************************************************/
		/* [1] Compute the hash value for this new entry */
		/*************************************************/
		int hashValue = hash(name);

		/******************************************************************************/
		/* [2] Extract what will eventually be the next entry in the hashed position  */
		/*     NOTE: this entry can very well be null, but the behaviour is identical */
		/******************************************************************************/
		SymbolTableEntry next = table[hashValue];

			/*************************************************/
		/* [2.5] NEW: Compute Offset based on scope level */
		/*************************************************/
		int currentOffset = 0;
		
		/* Compute offset only for real variables (not types or scope boundaries) */
		if (!(t instanceof TypeForScopeBoundaries) && !name.equals("int") && !name.equals("string")) {
			if (scopeLevel == 0) {
				/* global scope	 */
				currentOffset = globalOffsetCounter;
				globalOffsetCounter += 1;
			} 
			else {
				localOffsetCounter -= 1;
				currentOffset = localOffsetCounter;
				
			}
		}
	
		/**************************************************************************/
		/* [3] Prepare a new symbol table entry with name, type, next, prevtop and offset */
		/**************************************************************************/
		SymbolTableEntry e = new SymbolTableEntry(name, t, hashValue, next, top, topIndex++, currentOffset);

		/**********************************************/
		/* [4] Update the top of the symbol table ... */
		/**********************************************/
		top = e;
		
		/****************************************/
		/* [5] Enter the new entry to the table */
		/****************************************/
		table[hashValue] = e;
		
		/**************************/
		/* [6] Print Symbol Table */
		/**************************/
		printMe();
	}

	/**
	 * Begin a new scope and pre-populate it with the members of the given
	 * parent class. This is used to implement inheritance: entries of the
	 * parent class (fields and methods) are entered into the new scope so
	 * they are visible to the child class unless shadowed by child members.
	 */
	public void beginScopeFrom(types.TypeClass parent)
	{
		/* Start a new scope boundary */
		beginScope();

		if (parent == null) return;

		/*
		 * Walk the ancestor chain from the root-most ancestor down to the
		 * immediate parent, inserting their members in that order. This
		 * guarantees that nearer ancestors (the immediate parent) will shadow
		 * members from more distant ancestors when names clash.
		 */
		java.util.List<types.TypeClass> chain = new java.util.ArrayList<>();
		types.TypeClass cur = parent;
		while (cur != null) {
			chain.add(0, cur); /* insert at front to reverse order */
			cur = cur.father;
		}

		for (types.TypeClass anc : chain) {
			types.TypeClassVarDecList members = anc.dataMembers;
			while (members != null) {
				enter(members.head.name, members.head.type);
				members = members.tail;
			}
		}
	}

	/***********************************************/
	/* Find the inner-most scope element with name */
	/***********************************************/
	public Type find(String name)
	{
		SymbolTableEntry e;
				
		for (e = table[hash(name)]; e != null; e = e.next)
		{
			if (name.equals(e.name))
			{
				return e.type;
			}
		}
		
		return null;
	}

	/*********************************************************/
	/* Find the offset of the inner-most scope element name  */
	/*********************************************************/
	public int getOffset(String name)
	{
		SymbolTableEntry e;
		
		/*****************************************************************/
		/* [1] Compute hash value and traverse the linked list at table[i] */
		/*****************************************************************/
		for (e = table[hash(name)]; e != null; e = e.next)
		{
			/**********************************************************/
			/* [2] If the name matches, return the offset we stored  */
			/**********************************************************/
			if (name.equals(e.name))
			{
				return e.offset;
			}
		}
		
		/*******************************************************************/
		/* [3] If not found (shouldn't happen after semantMe), return 0    */
		/*******************************************************************/
		return 0;
	}

	/***********************************************/
	/* Find name in current scope */
	/***********************************************/
	public Type findInCurrentScope(String name) {
		for (SymbolTableEntry e = top; e != null; e = e.prevtop) {
			if (e.name.equals("SCOPE-BOUNDARY")) {
				break;  /* reached scope boundary */
			}
			if (e.name.equals(name)) {
				return e.type; /* found in current scope */
			}
		}
		return null;
	}
	public void setFunctionReturnType(Type t) {
		this.currentFunctionReturnType = t;
	}

	public Type getFunctionReturnType() {
		return this.currentFunctionReturnType;
	}

	/***************************************************************************/
	/* begine scope = Enter the <SCOPE-BOUNDARY> element to the data structure */
	/***************************************************************************/
	public void beginScope()
	{
		/************************************************************************/
		/* Though <SCOPE-BOUNDARY> entries are present inside the symbol table, */
		/* they are not really types. In order to be able to debug print them,  */
		/* a special TYPE_FOR_SCOPE_BOUNDARIES was developed for them. This     */
		/* class only contain their type name which is the bottom sign: _|_     */
		/************************************************************************/
		enter(
			"SCOPE-BOUNDARY",
			new TypeForScopeBoundaries("NONE"));
		/* Update number of scopes */
		scopeDepth++;
		/*********************************************/
		/* Print the symbol table after every change */
		/*********************************************/
		printMe();
	}

	/********************************************************************************/
	/* end scope = Keep popping elements out of the data structure,                 */
	/* from most recent element entered, until a <NEW-SCOPE> element is encountered */
	/********************************************************************************/
	public void endScope()
	{
		/**************************************************************************/
		/* Pop elements from the symbol table stack until a SCOPE-BOUNDARY is hit */		
		/**************************************************************************/
		while (!top.name.equals("SCOPE-BOUNDARY"))
		{
			table[top.index] = top.next;
			topIndex = topIndex -1;
			top = top.prevtop;
		}
		/**************************************/
		/* Pop the SCOPE-BOUNDARY sign itself */		
		/**************************************/
		table[top.index] = top.next;
		topIndex = topIndex -1;
		top = top.prevtop;

		/* Update number of scopes */
		scopeDepth--;
		/*********************************************/
		/* Print the symbol table after every change */		
		/*********************************************/
		printMe();
	}

	public boolean isGlobalScope() {
        return scopeDepth == 0;
    }
	
	public static int n=0;
	
	public void printMe()
	{
		int i=0;
		int j=0;
		String dirname="./output/";
		String filename=String.format("SYMBOL_TABLE_%d_IN_GRAPHVIZ_DOT_FORMAT.txt",n++);

		try
		{
			/*******************************************/
			/* [1] Open Graphviz text file for writing */
			/*******************************************/
			PrintWriter fileWriter = new PrintWriter(dirname+filename);

			/*********************************/
			/* [2] Write Graphviz dot prolog */
			/*********************************/
			fileWriter.print("digraph structs {\n");
			fileWriter.print("rankdir = LR\n");
			fileWriter.print("node [shape=record];\n");

			/*******************************/
			/* [3] Write Hash Table Itself */
			/*******************************/
			fileWriter.print("hashTable [label=\"");
			for (i=0;i<hashArraySize-1;i++) { fileWriter.format("<f%d>\n%d\n|",i,i); }
			fileWriter.format("<f%d>\n%d\n\"];\n",hashArraySize-1,hashArraySize-1);
		
			/****************************************************************************/
			/* [4] Loop over hash table array and print all linked lists per array cell */
			/****************************************************************************/
			for (i=0;i<hashArraySize;i++)
			{
				if (table[i] != null)
				{
					/*****************************************************/
					/* [4a] Print hash table array[i] -> entry(i,0) edge */
					/*****************************************************/
					fileWriter.format("hashTable:f%d -> node_%d_0:f0;\n",i,i);
				}
				j=0;
				for (SymbolTableEntry it = table[i]; it!=null; it=it.next)
				{
					/*******************************/
					/* [4b] Print entry(i,it) node */
					/*******************************/
					fileWriter.format("node_%d_%d ",i,j);
					fileWriter.format("[label=\"<f0>%s|<f1>%s|<f2>prevtop=%d|<f3>next\"];\n",
						it.name,
						it.type.name,
						it.prevtopIndex);

					if (it.next != null)
					{
						/***************************************************/
						/* [4c] Print entry(i,it) -> entry(i,it.next) edge */
						/***************************************************/
						fileWriter.format(
							"node_%d_%d -> node_%d_%d [style=invis,weight=10];\n",
							i,j,i,j+1);
						fileWriter.format(
							"node_%d_%d:f3 -> node_%d_%d:f0;\n",
							i,j,i,j+1);
					}
					j++;
				}
			}
			fileWriter.print("}\n");
			fileWriter.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}		
	}
	
	/**************************************/
	/* USUAL SINGLETON IMPLEMENTATION ... */
	/**************************************/
	private static SymbolTable instance = null;

	/*****************************/
	/* PREVENT INSTANTIATION ... */
	/*****************************/
	protected SymbolTable() {}

	/******************************/
	/* GET SINGLETON INSTANCE ... */
	/******************************/
	public static SymbolTable getInstance()
	{
		if (instance == null)
		{
			/*******************************/
			/* [0] The instance itself ... */
			/*******************************/
			instance = new SymbolTable();

			/*****************************************/
			/* [1] Enter primitive types int, string */
			/*****************************************/
			instance.enter("int",   TypeInt.getInstance());
			instance.enter("string", TypeString.getInstance());
			instance.enter("void",  TypeVoid.getInstance());

			/*************************************/
			/* [2] How should we handle void ??? */
			/*************************************/

			/***************************************/
			/* [3] Enter library functions PrintInt and PrintString*/
			/***************************************/
			instance.enter(
				"PrintInt",
				new TypeFunction(
					TypeVoid.getInstance(),
					"PrintInt",
					new TypeList(
						TypeInt.getInstance(),
						null)));
			
			instance.enter(
				"PrintString",
				new TypeFunction(
					TypeVoid.getInstance(),
					"PrintString",
					new TypeList(
						TypeString.getInstance(),
						null)));
			
		}
		return instance;
	}
}
