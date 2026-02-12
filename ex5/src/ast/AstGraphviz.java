package ast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class AstGraphviz
{
	/***********************/
	/* The file writer ... */
	/***********************/
	private PrintWriter fileWriter;
	
	/**************************************/
	/* USUAL SINGLETON IMPLEMENTATION ... */
	/**************************************/
	private static AstGraphviz instance = null;

	/*****************************/
	/* PREVENT INSTANTIATION ... */
	/*****************************/
	private AstGraphviz() {}

	/******************************/
	/* GET SINGLETON INSTANCE ... */
	/******************************/
	public static AstGraphviz getInstance()
	{
		if (instance == null)
		{
			instance = new AstGraphviz();
			
			/****************************/
			/* Initialize a file writer */
			/****************************/
			try
			{
				String dirname = "./output";
				String filename = "AST_IN_GRAPHVIZ_DOT_FORMAT.txt";
				File dir = new File(dirname);
				if (!dir.exists()) dir.mkdirs();
				File out = new File(dir, filename);
				instance.fileWriter = new PrintWriter(new FileOutputStream(out));

				/******************************************************/
				/* Print Directed Graph header in Graphviz dot format */
				/******************************************************/
				instance.fileWriter.print("digraph\n");
				instance.fileWriter.print("{\n");
				instance.fileWriter.print("graph [ordering = \"out\"]\n");
			}
			catch (Exception e)
			{
				e.printStackTrace();
				// Fall back to using System.err to avoid NPEs during AST logging
				instance.fileWriter = new PrintWriter(System.err, true);
			}
		}
		return instance;
	}

	/***********************************/
	/* Log node in graphviz dot format */
	/***********************************/
	public void logNode(int nodeSerialNumber,String nodeName)
	{
		if (fileWriter == null) return;
		// Escape backslashes, double quotes and newlines so the label is
		// a valid Graphviz string literal (use \n for newlines).
		String safe = nodeName;
		if (safe == null) safe = "";
		safe = safe.replace("\\", "\\\\");
		safe = safe.replace("\"", "\\\"");
		safe = safe.replace("\n", "\\n");
		fileWriter.format(
			"v%d [label = \"%s\"];\n",
			nodeSerialNumber,
			safe);
	}

	/***********************************/
	/* Log edge in graphviz dot format */
	/***********************************/
	public void logEdge(
		int fatherNodeSerialNumber,
		int sonNodeSerialNumber)
	{
		if (fileWriter == null) return;
		fileWriter.format(
			"v%d -> v%d;\n",
			fatherNodeSerialNumber,
			sonNodeSerialNumber);
	}
	
	/******************************/
	/* Finalize graphviz dot file */
	/******************************/
	public void finalizeFile()
	{
		if (fileWriter == null) return;
		fileWriter.print("}\n");
		fileWriter.close();
	}
}
