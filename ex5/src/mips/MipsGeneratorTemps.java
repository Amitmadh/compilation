// /***********/
// /* PACKAGE */
// /***********/
// package mips;

// /*******************/
// /* GENERAL IMPORTS */
// /*******************/
// import java.io.PrintWriter;
// import java.util.List;

// /*******************/
// /* PROJECT IMPORTS */
// /*******************/
// import temp.*;

// public class MipsGenerator
// {
// 	private static final int WORD_SIZE=4;
// 	/***********************/
// 	/* The file writer ... */
// 	/***********************/
// 	private PrintWriter fileWriter;

// 	/***********************/
// 	/* The file writer ... */
// 	/***********************/
// 	public void finalizeFile()
// 	{
// 		fileWriter.print("\tli $v0,10\n");
// 		fileWriter.print("\tsyscall\n");
// 		fileWriter.close();
// 	}

// 	//labels
// 	private static int labelCounter = 0;
// 	public String genLabel(String s) {
// 		return s + "_" + labelCounter++;
// 	}

// 	public void printInt(Temp t)
// 	{
// 		int idx=t.getSerialNumber();
// 		// fileWriter.format("\taddi $a0,Temp_%d,0\n",idx);
// 		fileWriter.format("\tmove $a0,Temp_%d\n",idx);
// 		fileWriter.format("\tli $v0,1\n");
// 		fileWriter.format("\tsyscall\n");
// 		fileWriter.format("\tli $a0,32\n");
// 		fileWriter.format("\tli $v0,11\n");
// 		fileWriter.format("\tsyscall\n");
// 	}
// //	public Temp addressLocalVar(int serialLocalVarNum)
// //	{
// //		Temp t  = TempFactory.getInstance().getFreshTemp();
// //		int idx = t.getSerialNumber();
// //
// //		fileWriter.format("\taddi Temp_%d,$fp,%d\n",idx,-serialLocalVarNum*WORD_SIZE);
// //
// //		return t;
// //	}
// 	public void allocate(String varName)
// 	{
// 		fileWriter.format(".data\n");
// 		fileWriter.format("\tglobal_%s: .word 721\n",varName);
// 	}
// 	public void load(Temp dst, String varName)
// 	{
// 		int idxdst=dst.getRegister();
// 		fileWriter.format("\tlw Temp_%d,global_%s\n",idxdst,varName);
// 	}
// 	public void store(String varName, Temp src)
// 	{
// 		int idxsrc=src.getRegister();
// 		fileWriter.format("\tsw Temp_%d,global_%s\n",idxsrc,varName);
// 	}
// 	public void storeLocal(Temp src, int offset)
// 	{
// 		int idxsrc=src.getRegister();
// 		fileWriter.format("\tsw Temp_%d,%d($fp)\n",idxsrc,-(offset+11)*WORD_SIZE);
// 	}
// 	public void loadLocal(Temp dst, int offset)
// 	{
// 		int idxdst=dst.getRegister();
// 		fileWriter.format("\tlw Temp_%d,%d($fp)\n",idxdst,-(offset+11)*WORD_SIZE);
// 	}
// 	public void returnValue(int offset) {
// 		fileWriter.format("\tlw $v0,%d($fp)\n",-(offset+1)*WORD_SIZE);
// 	}
// 	public void pushStack(Temp t)
// 	{
// 		int idx=t.getRegister();
// 		fileWriter.format("\tsubu $sp,$sp,%d\n",WORD_SIZE);
// 		fileWriter.format("\tsw Temp_%d,0($sp)\n",idx);
// 	}
// 	public void funcPrologue(String label, int numOfLocalVar)
// 	{
// 		label(label);
// 		fileWriter.format("\tsubu $sp,$sp,%d\n",WORD_SIZE);
// 		fileWriter.format("\tsw $ra,0($sp)\n");
// 		fileWriter.format("\tsubu $sp,$sp,%d\n",WORD_SIZE);
// 		fileWriter.format("\tsw $fp,0($sp)\n");
// 		fileWriter.format("\tmove $fp,$sp\n");

// 		//backup registers
// 		fileWriter.format("\tsubu $sp,$sp,%d\n",WORD_SIZE);
// 		fileWriter.format("\tsw $Temp_0,0($sp)\n");
// 		fileWriter.format("\tsubu $sp,$sp,%d\n",WORD_SIZE);
// 		fileWriter.format("\tsw $Temp_1,0($sp)\n");
// 		fileWriter.format("\tsubu $sp,$sp,%d\n",WORD_SIZE);
// 		fileWriter.format("\tsw $Temp_2,0($sp)\n");
// 		fileWriter.format("\tsubu $sp,$sp,%d\n",WORD_SIZE);
// 		fileWriter.format("\tsw $Temp_3,0($sp)\n");
// 		fileWriter.format("\tsubu $sp,$sp,%d\n",WORD_SIZE);
// 		fileWriter.format("\tsw $Temp_4,0($sp)\n");
// 		fileWriter.format("\tsubu $sp,$sp,%d\n",WORD_SIZE);
// 		fileWriter.format("\tsw $Temp_5,0($sp)\n");
// 		fileWriter.format("\tsubu $sp,$sp,%d\n",WORD_SIZE);
// 		fileWriter.format("\tsw $Temp_6,0($sp)\n");
// 		fileWriter.format("\tsubu $sp,$sp,%d\n",WORD_SIZE);
// 		fileWriter.format("\tsw $Temp_7,0($sp)\n");
// 		fileWriter.format("\tsubu $sp,$sp,%d\n",WORD_SIZE);
// 		fileWriter.format("\tsw $Temp_8,0($sp)\n");
// 		fileWriter.format("\tsubu $sp,$sp,%d\n",WORD_SIZE);
// 		fileWriter.format("\tsw $Temp_9,0($sp)\n");

// 		fileWriter.format("\tsubu $sp,$sp,%d\n",WORD_SIZE * numOfLocalVar);
// 	}
// 	public void funcEpilogue()
// 	{
// 		fileWriter.format("\tmove $sp,$fp\n");

// 		//restore registers
// 		fileWriter.format("\tlw $Temp_9,-40($sp)\n");
// 		fileWriter.format("\tlw $Temp_8,-36($sp)\n");
// 		fileWriter.format("\tlw $Temp_7,-32($sp)\n");
// 		fileWriter.format("\tlw $Temp_6,-28($sp)\n");
// 		fileWriter.format("\tlw $Temp_5,-24($sp)\n");
// 		fileWriter.format("\tlw $Temp_4,-20($sp)\n");
// 		fileWriter.format("\tlw $Temp_3,-16($sp)\n");
// 		fileWriter.format("\tlw $Temp_2,-12($sp)\n");
// 		fileWriter.format("\tlw $Temp_1,-8($sp)\n");
// 		fileWriter.format("\tlw $Temp_0,-4($sp)\n");

// 		fileWriter.format("\tlw $fp,0($sp)\n");
// 		fileWriter.format("\tlw $ra,4($sp)\n");
// 		fileWriter.format("\taddu $sp,$sp,%d\n",WORD_SIZE * 2);
// 		fileWriter.format("\tjr $ra\n");
// 	}
// 	public void callFunc(String funcName, List<Temp> args, Temp returnValue) {
// 		int reg = returnValue.getRegister();
// 		int idx;
// 		for (int i = args.size() - 1; i >= 0; i--) {
// 			idx=args.get(i).getRegister();
// 			fileWriter.format("\tsubu $sp,$sp,%d\n",WORD_SIZE);
// 			fileWriter.format("\tsw Temp_%d,0($sp)\n",idx);
// 		}
// 		fileWriter.format("\tjal %s\n",funcName);
// 		fileWriter.format("\taddu $sp,$sp,%d\n",WORD_SIZE * args.size());
// 		fileWriter.format("\tmove Temp_%d,$v0\n",reg);
// 	}
// 	public void loadArg(Temp dst, int index) {
// 		int idxdst=dst.getRegister();
// 		int offset = 8 + index * WORD_SIZE;
// 		fileWriter.format("\tlw Temp_%d,%d($sp)\n",idxdst,offset);
// 	}
// 	public void li(Temp t, int value)
// 	{
// 		int idx=t.getRegister();
// 		fileWriter.format("\tli Temp_%d,%d\n",idx,value);
// 	}
// 	public void add(Temp dst, Temp oprnd1, Temp oprnd2)
// 	{
// 		int i1 =oprnd1.getRegister();
// 		int i2 =oprnd2.getRegister();
// 		int dstidx=dst.getRegister();

// 		fileWriter.format("\tadd Temp_%d,Temp_%d,Temp_%d\n",dstidx,i1,i2);
// 	}
// 	public void sub(Temp dst, Temp oprnd1, Temp oprnd2)
// 	{
// 		int i1 =oprnd1.getRegister();
// 		int i2 =oprnd2.getRegister();
// 		int dstidx=dst.getRegister();

// 		fileWriter.format("\tsub Temp_%d,Temp_%d,Temp_%d\n",dstidx,i1,i2);
// 	}
// 	public void mul(Temp dst, Temp oprnd1, Temp oprnd2)
// 	{
// 		int i1 =oprnd1.getRegister();
// 		int i2 =oprnd2.getRegister();
// 		int dstidx=dst.getRegister();

// 		fileWriter.format("\tmul Temp_%d,Temp_%d,Temp_%d\n",dstidx,i1,i2);
// 	}
// 	public void divIntegers(Temp dst, Temp oprnd1, Temp oprnd2)
// 	{
// 		int i1 =oprnd1.getRegister();
// 		int i2 =oprnd2.getRegister();
// 		int dstidx=dst.getRegister();

// 		fileWriter.format("\tdiv Temp_%d,Temp_%d\n",i1,i2);
// 		fileWriter.format("\tmflo Temp_%d\n",dstidx);
// 	}
// 	public void seq(Temp dst, Temp oprnd1, Temp oprnd2)
// 	{
// 		int i1 =oprnd1.getRegister();
// 		int i2 =oprnd2.getRegister();
// 		int dstidx=dst.getRegister();

// 		fileWriter.format("\tseq Temp_%d,Temp_%d,Temp_%d\n",dstidx,i1,i2);
// 	}
// 	public void sgt(Temp dst, Temp oprnd1, Temp oprnd2)
// 	{
// 		int i1 =oprnd1.getRegister();
// 		int i2 =oprnd2.getRegister();
// 		int dstidx=dst.getRegister();

// 		fileWriter.format("\tsgt Temp_%d,Temp_%d,Temp_%d\n",dstidx,i1,i2);
// 	}
// 	public void slt(Temp dst, Temp oprnd1, Temp oprnd2)
// 	{
// 		int i1 =oprnd1.getRegister();
// 		int i2 =oprnd2.getRegister();
// 		int dstidx=dst.getRegister();

// 		fileWriter.format("\tslt Temp_%d,Temp_%d,Temp_%d\n",dstidx,i1,i2);
// 	}
// 	public void label(String inlabel)
// 	{
// 		if (inlabel.equals("main"))
// 		{
// 			fileWriter.format(".text\n");
// 			fileWriter.format("%s:\n",inlabel);
// 		}
// 		else
// 		{
// 			fileWriter.format("%s:\n",inlabel);
// 		}
// 	}	
// 	public void jump(String inlabel)
// 	{
// 		fileWriter.format("\tj %s\n",inlabel);
// 	}	
// 	public void jal(String label)
// 	{
// 		fileWriter.format("\tjal %s\n",label);
// 	}
// 	public void blt(Temp oprnd1, Temp oprnd2, String label)
// 	{
// 		int i1 =oprnd1.getRegister();
// 		int i2 =oprnd2.getRegister();
		
// 		fileWriter.format("\tblt Temp_%d,Temp_%d,%s\n",i1,i2,label);				
// 	}
// 	public void bge(Temp oprnd1, Temp oprnd2, String label)
// 	{
// 		int i1 =oprnd1.getRegister();
// 		int i2 =oprnd2.getRegister();
		
// 		fileWriter.format("\tbge Temp_%d,Temp_%d,%s\n",i1,i2,label);				
// 	}
// 	public void bne(Temp oprnd1, Temp oprnd2, String label)
// 	{
// 		int i1 =oprnd1.getRegister();
// 		int i2 =oprnd2.getRegister();
		
// 		fileWriter.format("\tbne Temp_%d,Temp_%d,%s\n",i1,i2,label);				
// 	}
// 	public void beq(Temp oprnd1, Temp oprnd2, String label)
// 	{
// 		int i1 =oprnd1.getRegister();
// 		int i2 =oprnd2.getRegister();
		
// 		fileWriter.format("\tbeq Temp_%d,Temp_%d,%s\n",i1,i2,label);				
// 	}
// 	public void beqz(Temp oprnd1, String label)
// 	{
// 		int i1 =oprnd1.getRegister();
				
// 		fileWriter.format("\tbeq Temp_%d,$zero,%s\n",i1,label);				
// 	}
	
// 	/**************************************/
// 	/* USUAL SINGLETON IMPLEMENTATION ... */
// 	/**************************************/
// 	private static MipsGenerator instance = null;

// 	/*****************************/
// 	/* PREVENT INSTANTIATION ... */
// 	/*****************************/
// 	protected MipsGenerator() {}

// 	/******************************/
// 	/* GET SINGLETON INSTANCE ... */
// 	/******************************/
// 	public static MipsGenerator getInstance()
// 	{
// 		if (instance == null)
// 		{
// 			/*******************************/
// 			/* [0] The instance itself ... */
// 			/*******************************/
// 			instance = new MipsGenerator();

// 			try
// 			{
// 				/*********************************************************************************/
// 				/* [1] Open the MIPS text file and write data section with error message strings */
// 				/*********************************************************************************/
// 				String dirname="./output/";
// 				String filename=String.format("MIPS.txt");

// 				/***************************************/
// 				/* [2] Open MIPS text file for writing */
// 				/***************************************/
// 				instance.fileWriter = new PrintWriter(dirname+filename);
// 			}
// 			catch (Exception e)
// 			{
// 				e.printStackTrace();
// 			}

// 			/*****************************************************/
// 			/* [3] Print data section with error message strings */
// 			/*****************************************************/
// 			instance.fileWriter.print(".data\n");
// 			instance.fileWriter.print("string_access_violation: .asciiz \"Access Violation\"\n");
// 			instance.fileWriter.print("string_illegal_div_by_0: .asciiz \"Illegal Division By Zero\"\n");
// 			instance.fileWriter.print("string_invalid_ptr_dref: .asciiz \"Invalid Pointer Dereference\"\n");
// 		}
// 		return instance;
// 	}
// }
