/***********/
/* PACKAGE */
/***********/
package mips;

/*******************/
/* GENERAL IMPORTS */
/*******************/
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ir.Ir;
/*******************/
/* PROJECT IMPORTS */
/*******************/
import temp.*;

public class MipsGenerator
{
	public static String outputPath;
	private static final int WORD_SIZE=4;
	public static List<String> strings = new ArrayList<>();
	public static Map<String,Map<String, String>> classesToDefaultStrVals = new HashMap<>();
	private static final int MAX=32767;
	private static final int MIN=-32768;
	/***********************/
	/* The file writer ... */
	/***********************/
	private PrintWriter fileWriter;

	private String reg(Temp t) {
    	return "t" + t.getRegister();
	}

	/***********************/
	/* The file writer ... */
	/***********************/
	public void finalizeFile()
	{
		fileWriter.format(".text\n");
		fileWriter.print("main:\n");
		Ir.getInstance().mipsGlobals();
		fileWriter.print("\tjal user_main_function\n");
		fileWriter.print("\tli $v0,10\n");
		fileWriter.print("\tsyscall\n");
		fileWriter.close();
	}

	//labels
	private static int labelCounter = 0;
	public static String genLabel(String s) {
		return s + "_" + labelCounter++;
	}
	

	static String accessViolation = genLabel("access_violation");
	static String divByZero = genLabel("division_by_zero");
	static String invalidPointer = genLabel("invalid_pointer_dereference");

	public void printInt(Temp t)
	{
		fileWriter.format("\tmove $a0,$%s\n",reg(t));
		fileWriter.format("\tli $v0,1\n");
		fileWriter.format("\tsyscall\n");
		fileWriter.format("\tli $a0,32\n");
		fileWriter.format("\tli $v0,11\n");
		fileWriter.format("\tsyscall\n");
	}
	public void printString(Temp t)
	{
		fileWriter.format("\tmove $a0,$%s\n",reg(t));
		fileWriter.format("\tli $v0,4\n");
		fileWriter.format("\tsyscall\n");
	}
	public void allocate(String varName)
	{
		fileWriter.format(".data\n");
		fileWriter.format("\tglobal_%s: .word 0\n",varName);
	}
	public void load(Temp dst, String varName)
	{
		fileWriter.format("\tlw $%s,global_%s\n",reg(dst),varName);
	}
	public void store(String varName, Temp src)
	{
		fileWriter.format("\tsw $%s,global_%s\n",reg(src),varName);
	}
	public void storeLocal(Temp src, int offset)
	{
		fileWriter.format("\tsw $%s,%d($fp)\n",reg(src),-(offset+11)*WORD_SIZE);
	}
	public void storeArg(Temp src, int index) {
		int offset = 8 + index * WORD_SIZE;
		fileWriter.format("\tsw $%s,%d($fp)\n",reg(src),offset);
	}
	public void storeField(Temp src, int index) {
		int offset = 4 + index * WORD_SIZE;
		fileWriter.format("\tlw $s0,8($fp)\n");
		fileWriter.format("\tbeq $s0,$zero,%s\n",invalidPointer);
		fileWriter.format("\tsw $%s,%d($s0)\n",reg(src),offset);
	}
	public void loadLocal(Temp dst, int index)
	{
		int offset = -(index + 11) * WORD_SIZE;
		fileWriter.format("\tlw $%s,%d($fp)\n",reg(dst),offset);
	}
	public void loadArg(Temp dst, int index, boolean isMethod) {
		int offset = (isMethod ? 12 : 8) + index * WORD_SIZE;
		fileWriter.format("\tlw $%s,%d($fp)\n",reg(dst),offset);
	}
	public void loadField(Temp dst, int index) {
		int offset = 4 + index * WORD_SIZE;
		fileWriter.format("\tlw $s0,8($fp)\n");
		fileWriter.format("\tbeq $s0,$zero,%s\n",invalidPointer);
		fileWriter.format("\tlw $%s,%d($s0)\n",reg(dst),offset);
	}
	public void la(Temp dst, int index) {
		String label = "string" + index;
		fileWriter.format("\tla $%s,%s\n",reg(dst),label);
	}
	public void returnValue(Temp rt, String funcName) {
		if (rt != null) {
			fileWriter.format("\tmove $v0,$%s\n",reg(rt));
		}
		fileWriter.format("\tj %s\n",funcName + "_function_epilogue");
	}
	public void pushStack(Temp t)
	{
		fileWriter.format("\tsubu $sp,$sp,%d\n",WORD_SIZE);
		fileWriter.format("\tsw $%s,0($sp)\n",reg(t));
	}
	public void label(String inlabel)
	{
		fileWriter.format("%s:\n",inlabel);
	}
	public void funcLabel(String inlabel)
	{
		fileWriter.format("%s_function:\n",inlabel);
	}
	public void classLabel(String inlabel)
	{
		fileWriter.format("%s_class:\n",inlabel);
	}
	public void funcPrologue(String label, int numOfLocalVar)
	{
		fileWriter.format(".text\n");
		funcLabel(label);
		fileWriter.format("\tsubu $sp,$sp,%d\n",WORD_SIZE);
		fileWriter.format("\tsw $ra,0($sp)\n");
		fileWriter.format("\tsubu $sp,$sp,%d\n",WORD_SIZE);
		fileWriter.format("\tsw $fp,0($sp)\n");
		fileWriter.format("\tmove $fp,$sp\n");
		fileWriter.format("\tli $v0,0\n");

		//backup registers
		fileWriter.format("\tsubu $sp,$sp,%d\n",WORD_SIZE);
		fileWriter.format("\tsw $t0,0($sp)\n");
		fileWriter.format("\tsubu $sp,$sp,%d\n",WORD_SIZE);
		fileWriter.format("\tsw $t1,0($sp)\n");
		fileWriter.format("\tsubu $sp,$sp,%d\n",WORD_SIZE);
		fileWriter.format("\tsw $t2,0($sp)\n");
		fileWriter.format("\tsubu $sp,$sp,%d\n",WORD_SIZE);
		fileWriter.format("\tsw $t3,0($sp)\n");
		fileWriter.format("\tsubu $sp,$sp,%d\n",WORD_SIZE);
		fileWriter.format("\tsw $t4,0($sp)\n");
		fileWriter.format("\tsubu $sp,$sp,%d\n",WORD_SIZE);
		fileWriter.format("\tsw $t5,0($sp)\n");
		fileWriter.format("\tsubu $sp,$sp,%d\n",WORD_SIZE);
		fileWriter.format("\tsw $t6,0($sp)\n");
		fileWriter.format("\tsubu $sp,$sp,%d\n",WORD_SIZE);
		fileWriter.format("\tsw $t7,0($sp)\n");
		fileWriter.format("\tsubu $sp,$sp,%d\n",WORD_SIZE);
		fileWriter.format("\tsw $t8,0($sp)\n");
		fileWriter.format("\tsubu $sp,$sp,%d\n",WORD_SIZE);
		fileWriter.format("\tsw $t9,0($sp)\n");

		fileWriter.format("\tsubu $sp,$sp,%d\n",WORD_SIZE * numOfLocalVar);
	}
	public void funcEpilogue(String funcName)
	{
		label(funcName + "_function_epilogue");
		fileWriter.format("\tmove $sp,$fp\n");

		//restore registers
		fileWriter.format("\tlw $t9,-40($sp)\n");
		fileWriter.format("\tlw $t8,-36($sp)\n");
		fileWriter.format("\tlw $t7,-32($sp)\n");
		fileWriter.format("\tlw $t6,-28($sp)\n");
		fileWriter.format("\tlw $t5,-24($sp)\n");
		fileWriter.format("\tlw $t4,-20($sp)\n");
		fileWriter.format("\tlw $t3,-16($sp)\n");
		fileWriter.format("\tlw $t2,-12($sp)\n");
		fileWriter.format("\tlw $t1,-8($sp)\n");
		fileWriter.format("\tlw $t0,-4($sp)\n");

		fileWriter.format("\tlw $fp,0($sp)\n");
		fileWriter.format("\tlw $ra,4($sp)\n");
		fileWriter.format("\taddu $sp,$sp,%d\n",WORD_SIZE * 2);
		fileWriter.format("\tjr $ra\n");
	}
	public void callFunc(String funcName, List<Temp> args, Temp returnValue, boolean haveReturnVal) {
		Temp arg;
		for (int i = args.size() - 1; i >= 0; i--) {
			arg = args.get(i);
			fileWriter.format("\tsubu $sp,$sp,%d\n",WORD_SIZE);
			fileWriter.format("\tsw $%s,0($sp)\n",reg(arg));
		}
		fileWriter.format("\tjal %s_function\n",funcName);
		fileWriter.format("\taddu $sp,$sp,%d\n",WORD_SIZE * args.size());
		if (haveReturnVal) {
			fileWriter.format("\tmove $%s,$v0\n",reg(returnValue));
		}
	}
	public void li(Temp t, int value)
	{
		fileWriter.format("\tli $%s,%d\n",reg(t),value);
	}
	public void add(Temp dst, Temp oprnd1, Temp oprnd2)
	{
		fileWriter.format("\tadd $%s,$%s,$%s\n",reg(dst),reg(oprnd1),reg(oprnd2));

		String minOverflow = genLabel("minOverflow");
		String end = genLabel("endOverflow");

		fileWriter.format("\tslti $s0,$%s,%d\n",reg(dst),MIN);
		fileWriter.format("\tbne $s0,$zero,%s\n", minOverflow);
		fileWriter.format("\tslti $s0,$%s,%d\n",reg(dst),MAX);
		fileWriter.format("\tbne $s0,$zero,%s\n", end);
		fileWriter.format("\tli $%s,%d\n",reg(dst),MAX);
		fileWriter.format("\tj %s\n", end);
		fileWriter.format("%s:\n", minOverflow);
		fileWriter.format("\tli $%s,%d\n",reg(dst),MIN);
		fileWriter.format("%s:\n", end);
	}
	public void sub(Temp dst, Temp oprnd1, Temp oprnd2)
	{
		fileWriter.format("\tsub $%s,$%s,$%s\n",reg(dst),reg(oprnd1),reg(oprnd2));

		String minOverflow = genLabel("minOverflow");
		String end = genLabel("endOverflow");

		fileWriter.format("\tslti $s0,$%s,%d\n",reg(dst),MIN);
		fileWriter.format("\tbne $s0,$zero,%s\n", minOverflow);
		fileWriter.format("\tslti $s0,$%s,%d\n",reg(dst),MAX);
		fileWriter.format("\tbne $s0,$zero,%s\n", end);
		fileWriter.format("\tli $%s,%d\n",reg(dst),MAX);
		fileWriter.format("\tj %s\n", end);
		fileWriter.format("%s:\n", minOverflow);
		fileWriter.format("\tli $%s,%d\n",reg(dst),MIN);
		fileWriter.format("%s:\n", end);
	}
	public void mul(Temp dst, Temp oprnd1, Temp oprnd2)
	{
		fileWriter.format("\tmul $%s,$%s,$%s\n",reg(dst),reg(oprnd1),reg(oprnd2));

		String minOverflow = genLabel("minOverflow");
		String end = genLabel("endOverflow");

		fileWriter.format("\tslti $s0,$%s,%d\n",reg(dst),MIN);
		fileWriter.format("\tbne $s0,$zero,%s\n", minOverflow);
		fileWriter.format("\tslti $s0,$%s,%d\n",reg(dst),MAX);
		fileWriter.format("\tbne $s0,$zero,%s\n", end);
		fileWriter.format("\tli $%s,%d\n",reg(dst),MAX);
		fileWriter.format("\tj %s\n", end);
		fileWriter.format("%s:\n", minOverflow);
		fileWriter.format("\tli $%s,%d\n",reg(dst),MIN);
		fileWriter.format("%s:\n", end);
	}
	public void divIntegers(Temp dst, Temp oprnd1, Temp oprnd2)
	{
		//division by zero
		fileWriter.format("\tbeq $%s,$zero,%s\n",reg(oprnd2),divByZero);

		//divide
		fileWriter.format("\tdiv $%s,$%s\n",reg(oprnd1),reg(oprnd2));
		fileWriter.format("\tmflo $%s\n",reg(dst));

		//overflow
		String minOverflow = genLabel("minOverflow");
		String end = genLabel("endOverflow");

		fileWriter.format("\tslti $s0,$%s,%d\n",reg(dst),MIN);
		fileWriter.format("\tbne $s0,$zero,%s\n", minOverflow);
		fileWriter.format("\tslti $s0,$%s,%d\n",reg(dst),MAX);
		fileWriter.format("\tbne $s0,$zero,%s\n", end);
		fileWriter.format("\tli $%s,%d\n",reg(dst),MAX);
		fileWriter.format("\tj %s\n", end);
		fileWriter.format("%s:\n", minOverflow);
		fileWriter.format("\tli $%s,%d\n",reg(dst),MIN);
		fileWriter.format("%s:\n", end);
	}
	public void seq(Temp dst, Temp oprnd1, Temp oprnd2)
	{
		fileWriter.format("\tseq $%s,$%s,$%s\n",reg(dst),reg(oprnd1),reg(oprnd2));
	}
	public void sgt(Temp dst, Temp oprnd1, Temp oprnd2)
	{
		fileWriter.format("\tsgt $%s,$%s,$%s\n",reg(dst),reg(oprnd1),reg(oprnd2));
	}
	public void slt(Temp dst, Temp oprnd1, Temp oprnd2)
	{
		fileWriter.format("\tslt $%s,$%s,$%s\n",reg(dst),reg(oprnd1),reg(oprnd2));
	}	
	public void jump(String inlabel)
	{
		fileWriter.format("\tj %s\n",inlabel);
	}	
	public void jal(String label)
	{
		fileWriter.format("\tjal %s\n",label);
	}
	public void blt(Temp oprnd1, Temp oprnd2, String label)
	{
		fileWriter.format("\tblt $%s,$%s,%s\n",reg(oprnd1),reg(oprnd2),label);				
	}
	public void bge(Temp oprnd1, Temp oprnd2, String label)
	{
		fileWriter.format("\tbge $%s,$%s,%s\n",reg(oprnd1),reg(oprnd2),label);				
	}
	public void bne(Temp oprnd1, Temp oprnd2, String label)
	{
		fileWriter.format("\tbne $%s,$%s,%s\n",reg(oprnd1),reg(oprnd2),label);				
	}
	public void beq(Temp oprnd1, Temp oprnd2, String label)
	{
		fileWriter.format("\tbeq $%s,$%s,%s\n",reg(oprnd1),reg(oprnd2),label);				
	}
	public void beqz(Temp oprnd1, String label)
	{			
		fileWriter.format("\tbeq $%s,$zero,%s\n",reg(oprnd1),label);				
	}
	public void creatArray(Temp dst, Temp size)
	{
		fileWriter.format("\tli $v0,9\n");	
		fileWriter.format("\tmove $a0,$%s\n", reg(size));
		fileWriter.format("\tadd $a0,$a0,1\n");
		fileWriter.format("\tmul $a0,$a0,4\n");
		fileWriter.format("\tsyscall\n");
		fileWriter.format("\tsw $%s,0($v0)\n", reg(size));
		fileWriter.format("\tmove $%s,$v0\n", reg(dst));						
	}
	public void arrayAccess(Temp dst, Temp arrayAddr, Temp index)
	{
		fileWriter.format("\tbeq $%s,0,%s\n",reg(arrayAddr),invalidPointer);
		fileWriter.format("\tbltz $%s,%s\n", reg(index), accessViolation);
		fileWriter.format("\tlw $s0,0($%s)\n", reg(arrayAddr));
		fileWriter.format("\tbge $%s,$s0,%s\n", reg(index), accessViolation);
	
		fileWriter.format("\tmove $s0,$%s\n", reg(index));
		fileWriter.format("\tadd $s0,$s0,1\n");
		fileWriter.format("\tmul $s0,$s0,4\n");
		fileWriter.format("\tadd $s0,$%s,$s0\n", reg(arrayAddr));
		fileWriter.format("\tlw $%s,0($s0)\n", reg(dst));					
	}
	public void arraySet(Temp src, Temp arrayAddr, Temp index)
	{
		fileWriter.format("\tbeq $%s,0,%s\n",reg(arrayAddr),invalidPointer);
		fileWriter.format("\tblez $%s,%s\n", reg(arrayAddr), accessViolation);
		fileWriter.format("\tbltz $%s,%s\n", reg(index), accessViolation);
		fileWriter.format("\tlw $s0,0($%s)\n", reg(arrayAddr));
		fileWriter.format("\tbge $%s,$s0,%s\n", reg(index), accessViolation);
	
		fileWriter.format("\tmove $s0,$%s\n", reg(index));
		fileWriter.format("\tadd $s0,$s0,1\n");
		fileWriter.format("\tmul $s0,$s0,4\n");
		fileWriter.format("\tadd $s0,$%s,$s0\n", reg(arrayAddr));
		fileWriter.format("\tsw $%s,0($s0)\n", reg(src));					
	}
	public void eqStrings(Temp dst, Temp str1, Temp str2)
	{
		fileWriter.format("\tli $s4,1\n");
		fileWriter.format("\tmove $s0,$%s\n", reg(str1));
		fileWriter.format("\tmove $s1,$%s\n", reg(str2));
		
		String eqLabel = genLabel("eq");
		String eqEndLabel = genLabel("eqEnd");
		String neqLabel = genLabel("neq");

		fileWriter.format("%s:\n", eqLabel);
		fileWriter.format("\tlb $s2,0($s0)\n");
		fileWriter.format("\tlb $s3,0($s1)\n");
		fileWriter.format("\tbne $s2,$s3,%s\n", neqLabel);
		fileWriter.format("\tbeq $s2,$0,%s\n", eqEndLabel);
		fileWriter.format("\taddu $s0,$s0,1\n");
		fileWriter.format("\taddu $s1,$s1,1\n");
		fileWriter.format("\tj %s\n", eqLabel);
		fileWriter.format("%s:\n", neqLabel);
		fileWriter.format("\tli $s4,0\n");
		fileWriter.format("%s:\n", eqEndLabel);		
		fileWriter.format("\tmove $%s,$s4\n", reg(dst));	
	}
	public void addStrings(Temp dst, Temp str1, Temp str2)
	{
		fileWriter.format("\tmove $s0,$%s\n", reg(str1));
		fileWriter.format("\tmove $s1,$%s\n", reg(str2));
		
		//compute length
		fileWriter.format("\tli $s3,0\n");

		String len1 = genLabel("len1");
		String len2 = genLabel("len2");
		String endLen = genLabel("end_len");

		fileWriter.format("%s:\n", len1);
		fileWriter.format("\tlb $s2,0($s0)\n");
		fileWriter.format("\tbeq $s2,$zero,%s\n", len2);
		fileWriter.format("\taddu $s0,$s0,1\n");
		fileWriter.format("\taddu $s3,$s3,1\n");
		fileWriter.format("\tj %s\n", len1);

		fileWriter.format("%s:\n", len2);
		fileWriter.format("\tlb $s2,0($s1)\n");
		fileWriter.format("\tbeq $s2,$zero,%s\n", endLen);
		fileWriter.format("\taddu $s1,$s1,1\n");
		fileWriter.format("\taddu $s3,$s3,1\n");
		fileWriter.format("\tj %s\n", len2);

		fileWriter.format("%s:\n", endLen);

		//allocate memory
		fileWriter.format("\taddu $s3,$s3,1\n");
		fileWriter.format("\tmove $a0,$s3\n");
		fileWriter.format("\tli $v0,9\n");
		fileWriter.format("\tsyscall\n");

		fileWriter.format("\tmove $s0,$%s\n", reg(str1));
		fileWriter.format("\tmove $s1,$%s\n", reg(str2));
		fileWriter.format("\tmove $s4,$v0\n");

		//concat
		String copy1 = genLabel("copy1");
		String copy2 = genLabel("copy2");
		String end = genLabel("end_concating");

		fileWriter.format("%s:\n", copy1);
		fileWriter.format("\tlb $s2,0($s0)\n");
		fileWriter.format("\tbeq $s2,$zero,%s\n", copy2);
		fileWriter.format("\tsb $s2,0($s4)\n");
		fileWriter.format("\taddu $s0,$s0,1\n");
		fileWriter.format("\taddu $s4,$s4,1\n");
		fileWriter.format("\tj %s\n", copy1);

		fileWriter.format("%s:\n", copy2);
		fileWriter.format("\tlb $s2,0($s1)\n");
		fileWriter.format("\tbeq $s2,$zero,%s\n", end);
		fileWriter.format("\tsb $s2,0($s4)\n");
		fileWriter.format("\taddu $s1,$s1,1\n");
		fileWriter.format("\taddu $s4,$s4,1\n");
		fileWriter.format("\tj %s\n", copy2);

		fileWriter.format("%s:\n", end);
		fileWriter.format("\tsb $zero,0($s4)\n");
		fileWriter.format("\tmove $%s,$v0\n", reg(dst));
	}
	public void classDec(String className, List<String> methods) 
	{
		fileWriter.format(".data\n");
		fileWriter.format("vt_%s_class:\n", className);
		for (String method : methods) {
			fileWriter.format(".word %s_function\n", method);
		}
	}
	public void newClass(Temp dst, String className, List<String> vars, List<String> varsNoOffset, Map<String,Integer> intVals, Map<String,String> strVals)
	{
		fileWriter.format("\tli $v0,9\n");
		fileWriter.format("\tli $a0,%d\n", (vars.size()+1)*WORD_SIZE);
		fileWriter.format("\tsyscall\n");
		fileWriter.format("\tmove $%s,$v0\n", reg(dst));
		fileWriter.format("\tla $s0,vt_%s_class\n", className);
		fileWriter.format("\tsw $s0,0($%s)\n", reg(dst));
		for(int i = 0; i < vars.size(); i++) {
			String var = varsNoOffset.get(i);
			int offset = (i+1)*WORD_SIZE;
			if (intVals.containsKey(var)) {
				fileWriter.format("\tli $s0,%d\n", intVals.get(var));
				fileWriter.format("\tsw $s0,%d($%s)\n", offset, reg(dst));
			} else if (strVals.containsKey(var)) {
				String label = "class" + className + var + "defVal";
				fileWriter.format("\tla $s0,%s\n",label);
				fileWriter.format("\tsw $s0,%d($%s)\n", offset, reg(dst));
			} else {
				fileWriter.format("\tli $s0,0\n");
				fileWriter.format("\tsw $s0,%d($%s)\n", offset, reg(dst));
			}
		}
	}
	public void fieldAccess(Temp dst, Temp instanceAddr, int index)
	{
		int offset = (index+1)*WORD_SIZE;
		fileWriter.format("\tbeq $%s,0,%s\n",reg(instanceAddr),invalidPointer);
		fileWriter.format("\tlw $%s,%d($%s)\n",reg(dst),offset,reg(instanceAddr));
	}
	public void fieldSet(Temp instanceAddr, int index, Temp src)
	{
		int offset = (index+1)*WORD_SIZE;
		fileWriter.format("\tbeq $%s,0,%s\n",reg(instanceAddr),invalidPointer);
		fileWriter.format("\tsw $%s,%d($%s)\n",reg(src),offset,reg(instanceAddr));
	}
	public void callMethod(Temp baseClass, int methodIndex, List<Temp> args, Temp returnValue, boolean haveReturnVal)
	{
		fileWriter.format("\tbeq $%s,0,%s\n",reg(baseClass),invalidPointer);
		Temp arg;
		for (int i = args.size() - 1; i >= 0; i--) {
			arg = args.get(i);
			fileWriter.format("\tsubu $sp,$sp,%d\n",WORD_SIZE);
			fileWriter.format("\tsw $%s,0($sp)\n", reg(arg));
		}
		fileWriter.format("\tsubu $sp,$sp,%d\n",WORD_SIZE);
		fileWriter.format("\tsw $%s,0($sp)\n",reg(baseClass));
		fileWriter.format("\tlw $s0,0($%s)\n",reg(baseClass));
		fileWriter.format("\tlw $s1,%d($s0)\n", methodIndex*WORD_SIZE);
		fileWriter.format("\tjalr $s1\n");
		fileWriter.format("\taddu $sp,$sp,%d\n",WORD_SIZE*(args.size()+1));
		if (haveReturnVal) {
			fileWriter.format("\tmove $%s,$v0\n", reg(returnValue));
		}
	}
	public void loadNil(Temp dst)
	{
		fileWriter.format("\tli $%s,0\n", reg(dst));
	}

	/**************************************/
	/* USUAL SINGLETON IMPLEMENTATION ... */
	/**************************************/
	private static MipsGenerator instance = null;

	/*****************************/
	/* PREVENT INSTANTIATION ... */
	/*****************************/
	protected MipsGenerator() {}

	/******************************/
	/* GET SINGLETON INSTANCE ... */
	/******************************/
	public static MipsGenerator getInstance()
	{
		if (instance == null)
		{
			/*******************************/
			/* [0] The instance itself ... */
			/*******************************/
			instance = new MipsGenerator();

			try
			{
				/*********************************************************************************/
				/* [1] Open the MIPS text file and write data section with error message strings */
				/*********************************************************************************/
				String filename=String.format(outputPath);

				/***************************************/
				/* [2] Open MIPS text file for writing */
				/***************************************/
				instance.fileWriter = new PrintWriter(filename);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

			/*****************************************************/
			/* [3] Print data section with error message strings */
			/*****************************************************/
			instance.fileWriter.print(".data\n");
			instance.fileWriter.print("string_access_violation: .asciiz \"Access Violation\"\n");
			instance.fileWriter.print("string_illegal_div_by_0: .asciiz \"Illegal Division By Zero\"\n");
			instance.fileWriter.print("string_invalid_ptr_dref: .asciiz \"Invalid Pointer Dereference\"\n");

			int i = 0;
			for (String str : strings) {
				String label = "string" + i;
				instance.fileWriter.format("%s: .asciiz %s\n", label, str);
				i++;
			}
			for (String cls : classesToDefaultStrVals.keySet()) {
				Map<String,String> defaultStrVals = classesToDefaultStrVals.get(cls); 
				for (String var : defaultStrVals.keySet()) {
					String label = "class" + cls + var + "defVal";
					instance.fileWriter.format("%s: .asciiz %s\n", label, defaultStrVals.get(var));
				}
			}

			instance.fileWriter.print(".text\n");
			instance.fileWriter.format("%s:\n", accessViolation);
			instance.fileWriter.format("\tla $a0,string_access_violation\n");
			instance.fileWriter.format("\tli $2,4\n");
			instance.fileWriter.format("\tsyscall\n");
			instance.fileWriter.format("\tli $2,10\n");
			instance.fileWriter.format("\tsyscall\n");

			instance.fileWriter.format("%s:\n", invalidPointer);
			instance.fileWriter.format("\tla $a0,string_invalid_ptr_dref\n");
			instance.fileWriter.format("\tli $2,4\n");
			instance.fileWriter.format("\tsyscall\n");
			instance.fileWriter.format("\tli $2,10\n");
			instance.fileWriter.format("\tsyscall\n");

			instance.fileWriter.format("%s:\n", divByZero);
			instance.fileWriter.format("\tla $a0,string_illegal_div_by_0\n");
			instance.fileWriter.format("\tli $2,4\n");
			instance.fileWriter.format("\tsyscall\n");
			instance.fileWriter.format("\tli $2,10\n");
			instance.fileWriter.format("\tsyscall\n");
		}
		return instance;
	}
}