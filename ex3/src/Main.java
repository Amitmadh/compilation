   
import java.io.*;

// import java_cup.runtime.Symbol;
import ast.*;

public class Main
{
	static public void main(String argv[])
	{
		Lexer l;
		Parser p;
		// Symbol s;
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
				/*************************/
				/* [6] Print the AST ... */
				/*************************/
				ast.printMe();
				/**************************/
				/* [7] Semant the AST ... */
				/**************************/
				ast.semantMe();
				fileWriter.print("OK");
				System.out.println("OK");
			} catch (ast.SemanticException e) {
				// Semantic errors produced during semantMe()
				// TODO: print line number and change format to ERROR(line)
				fileWriter.print(e.getMessage());
				System.out.println(e.getMessage());
			} catch (Exception e) {
				// lexical errors or other runtime exceptions
				if ("lexical error".equals(e.getMessage())) {
					fileWriter.print("ERROR");
					System.out.println("lexical error");
				} else {
					// other errors (syntax errors print as ERROR(line))
					fileWriter.print(e.getMessage());
					System.out.println(e.getMessage());
				}
			}
			
			/*************************/
			/* [8] Close output file */
			/*************************/
			fileWriter.close();

			/*************************************/
			/* [9] Finalize AST GRAPHIZ DOT file */
			/*************************************/
			AstGraphviz.getInstance().finalizeFile();
    	}
			     
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}


