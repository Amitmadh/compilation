import java.io.*;

import java_cup.runtime.Symbol;
import ast.*;

public class Main
{
	static public void main(String argv[])
	{
		Lexer l;
		Parser p;
		Symbol s;
		AstProgram ast = null;
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
			try {
    			ast = (AstProgram) p.parse().value;
    			fileWriter.print("OK");

				/*************************/
				/* [6] Print the AST ... */
				/*************************/
				ast.printMe();
			} catch (Exception e) {
				if (e.getMessage() == "lexical error") {
                    fileWriter.print("ERROR"); 
                } 
                else {
					fileWriter.print(e.getMessage());       // Prints: ERROR
                }
			}

			/*************************/
			/* [7] Close output file */
			/*************************/
			fileWriter.close();
			
			/*************************************/
			/* [8] Finalize AST GRAPHIZ DOT file */
			/*************************************/
			AstGraphviz.getInstance().finalizeFile();
    	}
			     
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}




