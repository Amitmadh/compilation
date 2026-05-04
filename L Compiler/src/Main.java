import java.io.*;
import java.util.List;
import java.util.Collections;

import java_cup.runtime.Symbol;
import ast.*;
import ir.Ir;
import cfg.Cfg;
import mips.MipsGenerator;

public class Main
{
	static public void main(String argv[])
	{
		Lexer l;
		Parser p;
		Symbol s;
		Ir ir;
		AstProgram ast;
		FileReader fileReader;
		PrintWriter fileWriter = null;
		String inputFileName = argv[0];
		String outputFileName = argv[1];

		try
		{
			/********************************/
			/* [1] Initialize a file reader */
			/********************************/
			fileReader = new FileReader(inputFileName);

			/********************************/
			/* [2] Initialize a file writer */
			/********************************/
			fileWriter = new PrintWriter(outputFileName);

			/******************************/
			/* [3] Initialize a new lexer */
			/******************************/
			l = new Lexer(fileReader);

			/*******************************/
			/* [4] Initialize a new parser */
			/*******************************/
			p = new Parser(l);

			/***********************************/
			/* [5] 3 ... 2 ... 1 ... Parse !!! */
			/***********************************/
			ast = (AstProgram) p.parse().value;

			/*************************/
			/* [6] Print the AST ... */
			/*************************/
			//ast.printMe();

			/**************************/
			/* [7] Semant the AST ... */
			/**************************/
			ast.semantMe();
			ast.annotateAst();


			/**********************/
			/* [8] IR the AST ... */
			/**********************/
			ast.irMe();
			ir = Ir.getInstance();
	

			/********************************/
			/* [9] Register allocation		 */
			/********************************/
			Cfg cfg = new Cfg(ir);
			RegisterAllocation ra = new RegisterAllocation(cfg);

			
			/*****************************/
			/* [10] Print IR with regs   */
			/*****************************/
			//ir.printIrCommands(outputFileName.replace(".txt", "_ir.txt"));

			/*************************************/
			/* [11] Finalize AST GRAPHIZ DOT file */
			/*************************************/
			//AstGraphviz.getInstance().finalizeFile();

			/***********************/
			/* [12] MIPS the Ir ... */
			/***********************/
			MipsGenerator.outputPath = outputFileName;
			Ir.getInstance().mipsMe();

			/***************************/
			/* [13] Finalize MIPS file */
			/***************************/
			MipsGenerator.getInstance().finalizeFile();
		}

		catch (Exception e)
		{
			String errorString = e.getMessage();

			if (fileWriter != null)
			{
				fileWriter.print(errorString);
				fileWriter.close();
			}
			else
			{
				System.out.println(errorString);
			}
		}
	}
}


