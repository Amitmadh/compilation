import java.io.*;
import java.util.List;

import java_cup.runtime.Symbol;
import java.util.Collections;
import ast.*;
import ir.Ir;
import cfg.Cfg;


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
		PrintWriter fileWriter;
		String inputFileName = argv[0];
		String outputFileName = argv[1];

		try
		{
			/********************************/
			/* [1] Initialize a file reader */
			/********************************/
			fileReader = new FileReader(inputFileName);

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
			ast.printMe();

			/**************************/
			/* [7] Semant the AST ... */
			/**************************/
			ast.semantMe();


			/**********************/
			/* [8] IR the AST ... */
			/**********************/
			ast.irMe();
			ir = Ir.getInstance();
			ir.printIrCommands(outputFileName.replace(".txt", "_ir.txt"));

			Ir commands = Ir.getInstance();
			Cfg cfg = new Cfg(commands);

			/********************************/
			/* [2] Initialize a file writer */
			/********************************/
			List<String> uninitVars = cfg.usedBeforeSet();

			fileWriter = new PrintWriter(outputFileName);

			if (uninitVars.size() == 0) {
				fileWriter.print("!OK");
				System.out.println("!OK\n");
			} else {
				Collections.sort(uninitVars);
				for (int i = 0; i < uninitVars.size() - 1; i++) {
					fileWriter.format("%s\n", uninitVars.get(i));
					System.out.format("%s\n", uninitVars.get(i));
				}
				fileWriter.format("%s", uninitVars.get(uninitVars.size() - 1));
				System.out.format("%s\n", uninitVars.get(uninitVars.size() - 1));
			}

			/**************************/
			/* [9] Close output file */
			/**************************/
			fileWriter.close();

			/*************************************/
			/* [10] Finalize AST GRAPHIZ DOT file */
			/*************************************/
			AstGraphviz.getInstance().finalizeFile();
		}

		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}


