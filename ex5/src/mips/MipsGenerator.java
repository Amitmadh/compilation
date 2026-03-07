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
import ir.IrCommandList;
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

	/***********************/
	/* The file writer ... */
	/***********************/
	public void finalizeFile()
	{
		fileWriter.format(".text\n");
		fileWriter.print("main:\n");
		Ir.getInstance().mipsGlobals();
		fileWriter.print("\tjal user_main\n");
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
		int idx=t.getRegister()+7;
		// fileWriter.format("\taddi $a0,$%d,0\n",idx);
		fileWriter.format("\tmove $4,$%d\n",idx);
		fileWriter.format("\tli $2,1\n");
		fileWriter.format("\tsyscall\n");
		fileWriter.format("\tli $4,32\n");
		fileWriter.format("\tli $2,11\n");
		fileWriter.format("\tsyscall\n");
	}
	public void printString(Temp t)
	{
		int reg=t.getRegister()+7;
		fileWriter.format("\tmove $4,$%d\n",reg);
		fileWriter.format("\tli $2,4\n");
		fileWriter.format("\tsyscall\n");
	}
	public void allocate(String varName)
	{
		fileWriter.format(".data\n");
		fileWriter.format("\tglobal_%s: .word 0\n",varName);
	}
	public void load(Temp dst, String varName)
	{
		int idxdst=dst.getRegister()+7;
		fileWriter.format("\tlw $%d,global_%s\n",idxdst,varName);
	}
	public void store(String varName, Temp src)
	{
		int idxsrc=src.getRegister()+7;
		fileWriter.format("\tsw $%d,global_%s\n",idxsrc,varName);
	}
	public void storeLocal(Temp src, int offset)
	{
		int idxsrc=src.getRegister()+7;
		fileWriter.format("\tsw $%d,%d($fp)\n",idxsrc,-(offset+11)*WORD_SIZE);
	}
	public void storeArg(Temp src, int index) {
		int idxdst=src.getRegister()+7;
		int offset = 8 + index * WORD_SIZE;
		fileWriter.format("\tsw $%d,%d($fp)\n",idxdst,offset);
	}
	public void storeField(Temp src, int index) {
		int idxdst=src.getRegister()+7;
		int offset = 4 + index * WORD_SIZE;
		fileWriter.format("\tlw $16,8($fp)\n");
		fileWriter.format("\tbeq $16,$zero,%s\n",invalidPointer);
		fileWriter.format("\tsw $%d,%d($16)\n",idxdst,offset);
	}
	public void loadLocal(Temp dst, int index)
	{
		int idxdst=dst.getRegister()+7;
		int offset = -(index + 11) * WORD_SIZE;
		fileWriter.format("\tlw $%d,%d($fp)\n",idxdst,offset);
	}
	public void loadArg(Temp dst, int index) {
		int idxdst=dst.getRegister()+7;
		int offset = 8 + index * WORD_SIZE;
		fileWriter.format("\tlw $%d,%d($fp)\n",idxdst,offset);
	}
	public void loadField(Temp dst, int index) {
		int idxdst=dst.getRegister()+7;
		int offset = 4 + index * WORD_SIZE;
		fileWriter.format("\tlw $16,8($fp)\n");
		fileWriter.format("\tbeq $16,$zero,%s\n",invalidPointer);
		fileWriter.format("\tlw $%d,%d($16)\n",idxdst,offset);
	}
	public void la(Temp dst, int index) {
		int idxdst=dst.getRegister()+7;
		String label = "string" + index;
		fileWriter.format("\tla $%d,%s\n",idxdst,label);
	}
	public void returnValue(Temp rt, String funcName) {
		if (rt != null) {
			int rg = rt.getRegister() + 7;
			fileWriter.format("\tmove $2,$%d\n",rg);
		}
		fileWriter.format("\tj %s\n",funcName + "_epilogue");
	}
	public void pushStack(Temp t)
	{
		int idx=t.getRegister()+7;
		fileWriter.format("\tsubu $sp,$sp,%d\n",WORD_SIZE);
		fileWriter.format("\tsw $%d,0($sp)\n",idx);
	}
	public void funcPrologue(String label, int numOfLocalVar)
	{
		fileWriter.format(".text\n");
		label(label);
		fileWriter.format("\tsubu $sp,$sp,%d\n",WORD_SIZE);
		fileWriter.format("\tsw $ra,0($sp)\n");
		fileWriter.format("\tsubu $sp,$sp,%d\n",WORD_SIZE);
		fileWriter.format("\tsw $fp,0($sp)\n");
		fileWriter.format("\tmove $fp,$sp\n");

		//backup registers
		fileWriter.format("\tsubu $sp,$sp,%d\n",WORD_SIZE);
		fileWriter.format("\tsw $8,0($sp)\n");
		fileWriter.format("\tsubu $sp,$sp,%d\n",WORD_SIZE);
		fileWriter.format("\tsw $9,0($sp)\n");
		fileWriter.format("\tsubu $sp,$sp,%d\n",WORD_SIZE);
		fileWriter.format("\tsw $10,0($sp)\n");
		fileWriter.format("\tsubu $sp,$sp,%d\n",WORD_SIZE);
		fileWriter.format("\tsw $11,0($sp)\n");
		fileWriter.format("\tsubu $sp,$sp,%d\n",WORD_SIZE);
		fileWriter.format("\tsw $12,0($sp)\n");
		fileWriter.format("\tsubu $sp,$sp,%d\n",WORD_SIZE);
		fileWriter.format("\tsw $13,0($sp)\n");
		fileWriter.format("\tsubu $sp,$sp,%d\n",WORD_SIZE);
		fileWriter.format("\tsw $14,0($sp)\n");
		fileWriter.format("\tsubu $sp,$sp,%d\n",WORD_SIZE);
		fileWriter.format("\tsw $15,0($sp)\n");
		fileWriter.format("\tsubu $sp,$sp,%d\n",WORD_SIZE);
		fileWriter.format("\tsw $16,0($sp)\n");
		fileWriter.format("\tsubu $sp,$sp,%d\n",WORD_SIZE);
		fileWriter.format("\tsw $17,0($sp)\n");

		fileWriter.format("\tsubu $sp,$sp,%d\n",WORD_SIZE * numOfLocalVar);
	}
	public void funcEpilogue(String funcName)
	{
		label(funcName + "_epilogue");
		fileWriter.format("\tmove $sp,$fp\n");

		//restore registers
		fileWriter.format("\tlw $17,-40($sp)\n");
		fileWriter.format("\tlw $16,-36($sp)\n");
		fileWriter.format("\tlw $15,-32($sp)\n");
		fileWriter.format("\tlw $14,-28($sp)\n");
		fileWriter.format("\tlw $13,-24($sp)\n");
		fileWriter.format("\tlw $12,-20($sp)\n");
		fileWriter.format("\tlw $11,-16($sp)\n");
		fileWriter.format("\tlw $10,-12($sp)\n");
		fileWriter.format("\tlw $9,-8($sp)\n");
		fileWriter.format("\tlw $8,-4($sp)\n");

		fileWriter.format("\tlw $fp,0($sp)\n");
		fileWriter.format("\tlw $ra,4($sp)\n");
		fileWriter.format("\taddu $sp,$sp,%d\n",WORD_SIZE * 2);
		fileWriter.format("\tjr $ra\n");
	}
	public void callFunc(String funcName, List<Temp> args, Temp returnValue, boolean haveReturnVal) {
		int reg;
		int idx;
		for (int i = args.size() - 1; i >= 0; i--) {
			idx=args.get(i).getRegister()+7;
			fileWriter.format("\tsubu $sp,$sp,%d\n",WORD_SIZE);
			fileWriter.format("\tsw $%d,0($sp)\n",idx);
		}
		fileWriter.format("\tjal %s\n",funcName);
		fileWriter.format("\taddu $sp,$sp,%d\n",WORD_SIZE * args.size());
		if (haveReturnVal) {
			reg = returnValue.getRegister()+7;
			fileWriter.format("\tmove $%d,$v0\n",reg);
		}
	}
	public void li(Temp t, int value)
	{
		int idx=t.getRegister()+7;
		fileWriter.format("\tli $%d,%d\n",idx,value);
	}
	public void add(Temp dst, Temp oprnd1, Temp oprnd2)
	{
		int i1 =oprnd1.getRegister()+7;
		int i2 =oprnd2.getRegister()+7;
		int dstidx=dst.getRegister()+7;

		fileWriter.format("\tadd $%d,$%d,$%d\n",dstidx,i1,i2);

		String minOverflow = genLabel("minOverflow");
		String end = genLabel("endOverflow");

		fileWriter.format("\tslti $16,$%d,%d\n",dstidx,MIN);
		fileWriter.format("\tbne $16,$zero,%s\n", minOverflow);
		fileWriter.format("\tslti $16,$%d,%d\n",dstidx,MAX);
		fileWriter.format("\tbne $16,$zero,%s\n", end);
		fileWriter.format("\tli $%d,%d\n",dstidx,MAX);
		fileWriter.format("\tj %s\n", end);
		fileWriter.format("%s:\n", minOverflow);
		fileWriter.format("\tli $%d,%d\n",dstidx,MIN);
		fileWriter.format("%s:\n", end);
	}
	public void sub(Temp dst, Temp oprnd1, Temp oprnd2)
	{
		int i1 =oprnd1.getRegister()+7;
		int i2 =oprnd2.getRegister()+7;
		int dstidx=dst.getRegister()+7;

		fileWriter.format("\tsub $%d,$%d,$%d\n",dstidx,i1,i2);

		String minOverflow = genLabel("minOverflow");
		String end = genLabel("endOverflow");

		fileWriter.format("\tslti $16,$%d,%d\n",dstidx,MIN);
		fileWriter.format("\tbne $16,$zero,%s\n", minOverflow);
		fileWriter.format("\tslti $16,$%d,%d\n",dstidx,MAX);
		fileWriter.format("\tbne $16,$zero,%s\n", end);
		fileWriter.format("\tli $%d,%d\n",dstidx,MAX);
		fileWriter.format("\tj %s\n", end);
		fileWriter.format("%s:\n", minOverflow);
		fileWriter.format("\tli $%d,%d\n",dstidx,MIN);
		fileWriter.format("%s:\n", end);
	}
	public void mul(Temp dst, Temp oprnd1, Temp oprnd2)
	{
		int i1 =oprnd1.getRegister()+7;
		int i2 =oprnd2.getRegister()+7;
		int dstidx=dst.getRegister()+7;

		fileWriter.format("\tmul $%d,$%d,$%d\n",dstidx,i1,i2);

		String minOverflow = genLabel("minOverflow");
		String end = genLabel("endOverflow");

		fileWriter.format("\tslti $16,$%d,%d\n",dstidx,MIN);
		fileWriter.format("\tbne $16,$zero,%s\n", minOverflow);
		fileWriter.format("\tslti $16,$%d,%d\n",dstidx,MAX);
		fileWriter.format("\tbne $16,$zero,%s\n", end);
		fileWriter.format("\tli $%d,%d\n",dstidx,MAX);
		fileWriter.format("\tj %s\n", end);
		fileWriter.format("%s:\n", minOverflow);
		fileWriter.format("\tli $%d,%d\n",dstidx,MIN);
		fileWriter.format("%s:\n", end);
	}
	public void divIntegers(Temp dst, Temp oprnd1, Temp oprnd2)
	{
		int i1 =oprnd1.getRegister()+7;
		int i2 =oprnd2.getRegister()+7;
		int dstidx=dst.getRegister()+7;

		//division by zero
		fileWriter.format("\tbeq $%d,$zero,%s\n",i2,divByZero);

		//divide
		fileWriter.format("\tdiv $%d,$%d\n",i1,i2);
		fileWriter.format("\tmflo $%d\n",dstidx);

		//overflow
		String minOverflow = genLabel("minOverflow");
		String end = genLabel("endOverflow");

		fileWriter.format("\tslti $16,$%d,%d\n",dstidx,MIN);
		fileWriter.format("\tbne $16,$zero,%s\n", minOverflow);
		fileWriter.format("\tslti $16,$%d,%d\n",dstidx,MAX);
		fileWriter.format("\tbne $16,$zero,%s\n", end);
		fileWriter.format("\tli $%d,%d\n",dstidx,MAX);
		fileWriter.format("\tj %s\n", end);
		fileWriter.format("%s:\n", minOverflow);
		fileWriter.format("\tli $%d,%d\n",dstidx,MIN);
		fileWriter.format("%s:\n", end);
	}
	public void seq(Temp dst, Temp oprnd1, Temp oprnd2)
	{
		int i1 =oprnd1.getRegister()+7;
		int i2 =oprnd2.getRegister()+7;
		int dstidx=dst.getRegister()+7;

		fileWriter.format("\tseq $%d,$%d,$%d\n",dstidx,i1,i2);
	}
	public void sgt(Temp dst, Temp oprnd1, Temp oprnd2)
	{
		int i1 =oprnd1.getRegister()+7;
		int i2 =oprnd2.getRegister()+7;
		int dstidx=dst.getRegister()+7;

		fileWriter.format("\tsgt $%d,$%d,$%d\n",dstidx,i1,i2);
	}
	public void slt(Temp dst, Temp oprnd1, Temp oprnd2)
	{
		int i1 =oprnd1.getRegister()+7;
		int i2 =oprnd2.getRegister()+7;
		int dstidx=dst.getRegister()+7;

		fileWriter.format("\tslt $%d,$%d,$%d\n",dstidx,i1,i2);
	}
	public void label(String inlabel)
	{
		fileWriter.format("%s:\n",inlabel);
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
		int i1 =oprnd1.getRegister()+7;
		int i2 =oprnd2.getRegister()+7;
		
		fileWriter.format("\tblt $%d,$%d,%s\n",i1,i2,label);				
	}
	public void bge(Temp oprnd1, Temp oprnd2, String label)
	{
		int i1 =oprnd1.getRegister()+7;
		int i2 =oprnd2.getRegister()+7;
		
		fileWriter.format("\tbge $%d,$%d,%s\n",i1,i2,label);				
	}
	public void bne(Temp oprnd1, Temp oprnd2, String label)
	{
		int i1 =oprnd1.getRegister()+7;
		int i2 =oprnd2.getRegister()+7;
		
		fileWriter.format("\tbne $%d,$%d,%s\n",i1,i2,label);				
	}
	public void beq(Temp oprnd1, Temp oprnd2, String label)
	{
		int i1 =oprnd1.getRegister()+7;
		int i2 =oprnd2.getRegister()+7;
		
		fileWriter.format("\tbeq $%d,$%d,%s\n",i1,i2,label);				
	}
	public void beqz(Temp oprnd1, String label)
	{
		int i1 =oprnd1.getRegister()+7;
				
		fileWriter.format("\tbeq $%d,$zero,%s\n",i1,label);				
	}
	public void creatArray(Temp dst, Temp size)
	{
		int dstreg = dst.getRegister()+7;
		int sizereg = size.getRegister()+7;

		fileWriter.format("\tli $v0,9\n");	
		fileWriter.format("\tmove $4,$%d\n", sizereg);
		fileWriter.format("\tadd $4,$4,1\n");
		fileWriter.format("\tmul $4,$4,4\n");
		fileWriter.format("\tsyscall\n");
		fileWriter.format("\tsw $%d,0($2)\n", sizereg);
		fileWriter.format("\tmove $%d,$2\n", dstreg);						
	}
	public void arrayAccess(Temp dst, Temp arrayAddr, Temp index)
	{
		int dstreg = dst.getRegister()+7;
		int addrReg = arrayAddr.getRegister()+7;
		int idxReg = index.getRegister()+7;

		fileWriter.format("\tbltz $%d,%s\n", idxReg, accessViolation);
		fileWriter.format("\tlw $16,0($%d)\n", addrReg);
		fileWriter.format("\tbge $%d,$16,%s\n", idxReg, accessViolation);
	
		fileWriter.format("\tmove $16,$%d\n", idxReg);
		fileWriter.format("\tadd $16,$16,1\n");
		fileWriter.format("\tmul $16,$16,4\n");
		fileWriter.format("\tadd $16,$%d,$16\n", addrReg);
		fileWriter.format("\tlw $%d,0($16)\n", dstreg);					
	}
	public void arraySet(Temp src, Temp arrayAddr, Temp index)
	{
		int srcReg = src.getRegister()+7;
		int addrReg = arrayAddr.getRegister()+7;
		int idxReg = index.getRegister()+7;

		fileWriter.format("\tbltz $%d,%s\n", idxReg, accessViolation);
		fileWriter.format("\tlw $16,0($%d)\n", addrReg);
		fileWriter.format("\tbge $%d,$16,%s\n", idxReg, accessViolation);
	
		fileWriter.format("\tmove $16,$%d\n", idxReg);
		fileWriter.format("\tadd $16,$16,1\n");
		fileWriter.format("\tmul $16,$16,4\n");
		fileWriter.format("\tadd $16,$%d,$16\n", addrReg);
		fileWriter.format("\tsw $%d,0($16)\n", srcReg);					
	}
	public void eqStrings(Temp dst, Temp str1, Temp str2)
	{
		int resReg = dst.getRegister()+7;
		int addr1Reg = str1.getRegister()+7;
		int addr2Reg = str2.getRegister()+7;

		fileWriter.format("\tli $20,1\n");
		fileWriter.format("\tmove $16,$%d\n", addr1Reg);
		fileWriter.format("\tmove $17,$%d\n", addr2Reg);
		
		String eqLabel = genLabel("eq");
		String eqEndLabel = genLabel("eqEnd");
		String neqLabel = genLabel("neq");

		fileWriter.format("%s:\n", eqLabel);
		fileWriter.format("\tlb $18,0($16)\n");
		fileWriter.format("\tlb $19,0($17)\n");
		fileWriter.format("\tbne $18,$19,%s\n", neqLabel);
		fileWriter.format("\tbeq $18,$0,%s\n", eqEndLabel);
		fileWriter.format("\taddu $16,$16,1\n");
		fileWriter.format("\taddu $17,$17,1\n");
		fileWriter.format("\tj %s\n", eqLabel);
		fileWriter.format("%s:\n", neqLabel);
		fileWriter.format("\tli $20,0\n");
		fileWriter.format("\tmove $%d,$20\n", resReg);
		fileWriter.format("%s:\n", eqEndLabel);			
	}
	public void addStrings(Temp dst, Temp str1, Temp str2)
	{
		int resReg = dst.getRegister()+7;
		int addr1Reg = str1.getRegister()+7;
		int addr2Reg = str2.getRegister()+7;

		fileWriter.format("\tmove $16,$%d\n", addr1Reg);
		fileWriter.format("\tmove $17,$%d\n", addr2Reg);
		
		//compute length
		fileWriter.format("\tli $19,0\n");

		String len1 = genLabel("len1");
		String len2 = genLabel("len2");
		String endLen = genLabel("end_len");

		fileWriter.format("%s:\n", len1);
		fileWriter.format("\tlb $18,0($16)\n");
		fileWriter.format("\tbeq $18,$zero,%s\n", len2);
		fileWriter.format("\taddu $16,$16,1\n");
		fileWriter.format("\taddu $19,$19,1\n");
		fileWriter.format("\tj %s\n", len1);

		fileWriter.format("%s:\n", len2);
		fileWriter.format("\tlb $18,0($17)\n");
		fileWriter.format("\tbeq $18,$zero,%s\n", endLen);
		fileWriter.format("\taddu $17,$17,1\n");
		fileWriter.format("\taddu $19,$19,1\n");
		fileWriter.format("\tj %s\n", len2);

		fileWriter.format("%s:\n", endLen);

		//allocate memory
		fileWriter.format("\taddu $19,$19,1\n");
		fileWriter.format("\tmove $4,$19\n");
		fileWriter.format("\tli $v0,9\n");
		fileWriter.format("\tsyscall\n");

		fileWriter.format("\tmove $16,$%d\n", addr1Reg);
		fileWriter.format("\tmove $17,$%d\n", addr2Reg);
		fileWriter.format("\tmove $20,$2\n");

		//concat
		String copy1 = genLabel("copy1");
		String copy2 = genLabel("copy2");
		String end = genLabel("end_concating");

		fileWriter.format("%s:\n", copy1);
		fileWriter.format("\tlb $18,0($16)\n");
		fileWriter.format("\tbeq $18,$zero,%s\n", copy2);
		fileWriter.format("\tsb $18,0($20)\n");
		fileWriter.format("\taddu $16,$16,1\n");
		fileWriter.format("\taddu $20,$20,1\n");
		fileWriter.format("\tj %s\n", copy1);

		fileWriter.format("%s:\n", copy2);
		fileWriter.format("\tlb $18,0($17)\n");
		fileWriter.format("\tbeq $18,$zero,%s\n", end);
		fileWriter.format("\tsb $18,0($20)\n");
		fileWriter.format("\taddu $17,$17,1\n");
		fileWriter.format("\taddu $20,$20,1\n");
		fileWriter.format("\tj %s\n", copy2);

		fileWriter.format("%s:\n", end);
		fileWriter.format("\tsb $zero,0($20)\n");
		fileWriter.format("\tmove $%d,$2\n", resReg);
	}
	public void classDec(String className, List<String> methods) 
	{
		fileWriter.format(".data\n");
		fileWriter.format("vt_%s:\n", className);
		for (String method : methods) {
			fileWriter.format(".word %s\n", method);
		}
	}
	public void newClass(Temp dst, String className, List<String> vars, List<String> varsNoOffset, Map<String,Integer> intVals, Map<String,String> strVals)
	{
		int dstReg = dst.getRegister()+7;
		fileWriter.format("\tli $2,9\n");
		fileWriter.format("\tli $4,%d\n", (vars.size()+1)*WORD_SIZE);
		fileWriter.format("\tsyscall\n");
		fileWriter.format("\tmove $%d,$2\n", dstReg);
		fileWriter.format("\tla $16,vt_%s\n", className);
		fileWriter.format("\tsw $16,0($%d)\n", dstReg);
		for(int i = 0; i < vars.size(); i++) {
			String var = varsNoOffset.get(i);
			int offset = (i+1)*WORD_SIZE;
			if (intVals.containsKey(var)) {
				fileWriter.format("\tli $16,%d\n", intVals.get(var));
				fileWriter.format("\tsw $16,%d($%d)\n", offset, dstReg);
			} else if (strVals.containsKey(var)) {
				String label = "class" + className + var + "defVal";
				fileWriter.format("\tla $16,%s\n",label);
				fileWriter.format("\tsw $16,%d($%d)\n", offset, dstReg);
			} else {
				fileWriter.format("\tli $16,0\n");
				fileWriter.format("\tsw $16,%d($%d)\n", offset, dstReg);
			}
		}
	}
	public void fieldAccess(Temp dst, Temp instanceAddr, int index)
	{
		int dstReg = dst.getRegister()+7;
		int srcReg = instanceAddr.getRegister()+7;

		int offset = (index+1)*WORD_SIZE;
		fileWriter.format("\tbeq $%d,0,%s\n",srcReg,invalidPointer);
		fileWriter.format("\tlw $%d,%d($%d)\n",dstReg,offset,srcReg);
	}
	public void fieldSet(Temp instanceAddr, int index, Temp src)
	{
		int clsReg = instanceAddr.getRegister()+7;
		int srcReg = src.getRegister()+7;

		int offset = (index+1)*WORD_SIZE;
		fileWriter.format("\tbeq $%d,0,%s\n",clsReg,invalidPointer);
		fileWriter.format("\tsw $%d,%d($%d)\n",srcReg,offset,clsReg);
	}
	public void callMethod(Temp baseClass, int methodIndex, List<Temp> args, Temp returnValue, boolean haveReturnVal)
	{
		int clsReg = baseClass.getRegister()+7;
		int retReg = returnValue.getRegister()+7;

		fileWriter.format("\tbeq $%d,0,%s\n",clsReg,invalidPointer);
		int idx;
		for (int i = args.size() - 1; i >= 0; i--) {
			idx=args.get(i).getRegister()+7;
			fileWriter.format("\tsubu $sp,$sp,%d\n",WORD_SIZE);
			fileWriter.format("\tsw $%d,0($sp)\n",idx);
		}
		fileWriter.format("\tsubu $sp,$sp,%d\n",WORD_SIZE);
		fileWriter.format("\tsw $%d,0($sp)\n",clsReg);
		fileWriter.format("\tlw $16,0($%d)\n",clsReg);
		fileWriter.format("\tlw $17,%d($16)\n", methodIndex*WORD_SIZE);
		fileWriter.format("\tjalr $17\n");
		fileWriter.format("\taddu $sp,$sp,%d\n",WORD_SIZE*(args.size()+1));
		if (haveReturnVal) {
			fileWriter.format("\tmove $%d,$v0\n", retReg);
		}
	}
	public void loadNil(Temp dst)
	{
		int dstReg = dst.getRegister()+7;
		fileWriter.format("\tli $%d,0\n", dstReg);
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
			instance.fileWriter.format("\tla $4,string_access_violation\n");
			instance.fileWriter.format("\tli $2,4\n");
			instance.fileWriter.format("\tsyscall\n");
			instance.fileWriter.format("\tli $2,10\n");
			instance.fileWriter.format("\tsyscall\n");

			instance.fileWriter.format("%s:\n", invalidPointer);
			instance.fileWriter.format("\tla $4,string_invalid_ptr_dref\n");
			instance.fileWriter.format("\tli $2,4\n");
			instance.fileWriter.format("\tsyscall\n");
			instance.fileWriter.format("\tli $2,10\n");
			instance.fileWriter.format("\tsyscall\n");

			instance.fileWriter.format("%s:\n", divByZero);
			instance.fileWriter.format("\tla $4,string_illegal_div_by_0\n");
			instance.fileWriter.format("\tli $2,4\n");
			instance.fileWriter.format("\tsyscall\n");
			instance.fileWriter.format("\tli $2,10\n");
			instance.fileWriter.format("\tsyscall\n");
		}
		return instance;
	}
}
