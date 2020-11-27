package edu.iastate.cs228.hw5.template;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

/**
 * A class that generates Java code for the perfect hash table implementation.
 *
 * @author Raj Singh
 */
public class CodeGenerator {
	/**
	 * The comment for the variable holding the keys of the hash table.
	 */
	private static final String[] COMMENT_KEY_LIST = {
			"To reduce space usage, remove the key list if it is not needed. The",
			"list is not needed for hash table operation. If you hash a value",
			"which is not in the table (which you shouldn't), the only way to",
			"detect the miss is to compare against the corresponding table index." };

	/**
	 * The documentation comment for the hash function.
	 */
	private static final String[] COMMENT_HASH = {
			"Determines the hash code of the given string, which is equal to its", "index in the key list.",
			"Precondition: the given string is within the key list.", "", "@param key",
			"  the string for which to calculate a hash", "@return", "  the hash code of the given string" };

	/**
	 * The comment for the generated test main function.
	 */
	private static final String[] COMMENT_TEST_MAIN = {
			"Example main method to test the hash function. Every key should hash",
			"to its own index. An error message will be printed if this doesn't", "hold.", "", "Uncomment to use." };

	/**
	 * Array holding indent strings.
	 */
	private static final String[] INDENT = new String[6];

	static {
		final String indent = "  ";

		INDENT[0] = "";

		for (int i = 1; i < INDENT.length; ++i) {
			INDENT[i] = INDENT[i - 1] + indent;
		}
	}

	/**
	 * The first table, T1.
	 */
	private int[][] table1;

	/**
	 * The second table, T2.
	 */
	private int[][] table2;

	/**
	 * The G array.
	 */
	private int[] gArray;

	/**
	 * The modulus.
	 */
	private int modulus;

	/**
	 * The list of words.
	 */
	private List<String> words;

	/**
	 * Constructs a CodeGenerator that uses the given values. Does not perform deep
	 * copies.
	 * <p>
	 * Preconditions: - elements of T1 are all the same length - T2 is the same size
	 * as T1 - G.length == words.size()
	 *
	 * @param table1  the table T1
	 * @param table2  the table T2
	 * @param gArray  the array G
	 * @param modulus the modulus
	 * @param words   the list of keys of the hash table
	 * @throws NullPointerException     if any of the reference arguments are null
	 * @throws IllegalArgumentException if modulus is not positive
	 */
	public CodeGenerator(int[][] table1, int[][] table2, int[] gArray, int modulus, List<String> words)
			throws NullPointerException, IllegalArgumentException {
		if (null == table1 || null == table2 || null == gArray || null == words) {
			throw new NullPointerException();
		}

		if (0 >= modulus) {
			throw new IllegalArgumentException(String.valueOf(modulus));
		}

		this.table1 = table1;
		this.table2 = table2;
		this.gArray = gArray;
		this.modulus = modulus;
		this.words = words;
	}

	/**
	 * Generates the perfect hash table code, writing it to the given stream. <b>The
	 * given stream is not closed.</b>
	 *
	 * @param output    the output stream to write to
	 * @param className the name of the class that will be generated
	 * @throws NullPointerException     if either argument is null
	 * @throws IllegalArgumentException if the class name is not a valid Java
	 *                                  identifier
	 */
	public void generate(OutputStream output, String className) throws NullPointerException, IllegalArgumentException {
		if (null == output || null == className) {
			throw new NullPointerException();
		}

		if (!className.matches("[A-Za-z_][A-Za-z_0-9]*")) {
			throw new IllegalArgumentException("invalid class name \"" + className + "\"");
		}

		final String keyListName = "KEY_LIST";
		final String table1Name = "TABLE_1";
		final String table2Name = "TABLE_2";
		final String graphArrName = "G";

		PrintStream ps = new PrintStream(output);

		begin(ps, "public", className);

		printComment(ps, COMMENT_KEY_LIST, INDENT[1]);
		array(ps, words.toArray(new String[0]), "public static final", keyListName);

		ps.println();

		table(ps, table1, "private static final", table1Name);

		ps.println();

		table(ps, table2, "private static final", table2Name);

		ps.println();

		array(ps, gArray, "private static final", graphArrName);

		ps.println();

		printDocComment(ps, COMMENT_HASH, INDENT[1]);
		hashFunction(ps, "public static", table1Name, table2Name, graphArrName, modulus, words.size());

		ps.println();

		printComment(ps, COMMENT_TEST_MAIN, INDENT[1]);
		testMain(ps, keyListName);

		end(ps);

		ps.flush();
	}

	/**
	 * Prints the class declaration and the initial curly brace ({) to the given
	 * stream.
	 *
	 * @param ps        the stream to print to
	 * @param access    the access modifier for the class
	 * @param className the name of the class
	 */
	private void begin(PrintStream ps, String access, String className) {
		ps.print(access + " class " + className + "{");
	}

	/**
	 * Prints the given array declaration and initialization to the given stream.
	 *
	 * @param ps      the stream to print to
	 * @param data    the array to print
	 * @param access  the access modifier for the array
	 * @param varName the variable name for the array
	 */
	private void array(PrintStream ps, String[] data, String access, String varName) {
		ps.println(access + " String[] " + varName + " = " + buildStringArrayStr(data) + ";");
	}

	/**
	 * Prints the given array declaration and initialization to the given stream.
	 *
	 * @param ps      the stream to print to
	 * @param data    the array to print
	 * @param access  the access modifier for the array
	 * @param varName the variable name for the array
	 */
	private void array(PrintStream ps, int[] data, String access, String varName) {
		ps.println(access + " int[] " + varName + " = " + buildIntArrayStr(data) + ";");
	}

	/**
	 * Prints the given 2D array declaration and initialization to the given stream.
	 *
	 * @param ps      the stream to print to
	 * @param data    the array to print
	 * @param access  the access modifier for the array
	 * @param varName the variable name for the array
	 */
	private void table(PrintStream ps, int[][] data, String access, String varName) {
		ps.println(access + " int[][] " + varName + " = " + buildInt2DArrayStr(data) + ";");
	}

	/**
	 * Prints the hash function to the given stream.
	 *
	 * @param ps        the stream to print to
	 * @param access    the access modifier of the function
	 * @param t1Name    the name of the T1 "global" variable
	 * @param t2Name    the name of the T2 "global" variable
	 * @param graphName the name of the G "global" variable
	 * @param modulus   the modulus
	 * @param size      the length of the key list
	 */
	private void hashFunction(PrintStream ps, String access, String t1Name, String t2Name, String graphName,
			int modulus, int size) {
		final int indentLevel = 1;

		if (0 != access.length()) {
			access += " ";
		}

		// method
		ps.print(INDENT[indentLevel]);
		ps.printf("%sint hash(String key) {", access);
		ps.println();

		// variables
		ps.print(INDENT[indentLevel + 1]);
		ps.println("int i, u, v;");

		ps.println();

		// for ... {
		ps.print(INDENT[indentLevel + 1]);
		ps.println("for (u = v = i = 0; i < key.length(); ++i) {");

		// u = (u + T1[i % T1.length][key.charAt(i) % T1[0].length]) % mod;
		ps.print(INDENT[indentLevel + 2]);
		ps.printf("u = (u + %s[i %% %d][key.charAt(i) %% %d])", t1Name, table1.length, table1[0].length);
		ps.println();

		ps.print("    ");
		ps.print(INDENT[indentLevel + 3]);
		ps.printf("%% %d;", modulus);
		ps.println();

		// v = (v + T2[i % T2.length][key.charAt(i) % T2[0].length]) % mod;
		ps.print(INDENT[indentLevel + 2]);
		ps.printf("v = ((v + %s[i %% %d][key.charAt(i) %% %d])", t2Name, table2.length, table2[0].length);
		ps.println();

		ps.print("    ");
		ps.print(INDENT[indentLevel + 3]);
		ps.printf("%% %d);", modulus);
		ps.println();

		// } (end for)
		ps.print(INDENT[indentLevel + 1]);
		ps.println("}");

		ps.println();

		// return
		ps.print(INDENT[indentLevel + 1]);
		ps.printf("return (%s[u] + %s[v]) %% %d;", graphName, graphName, size);
		ps.println();

		// } (end method)
		ps.print(INDENT[indentLevel]);
		ps.println("}");
	}

	/**
	 * Prints the (commented) test main function to the given stream.
	 *
	 * @param ps         the stream to write to
	 * @param keyArrName the name of the "global" key array
	 */
	private void testMain(PrintStream ps, String keyArrName) {
		final int indentLevel = 1;

		ps.print(INDENT[indentLevel]);
		ps.println("/*");

		ps.print(INDENT[indentLevel]);
		ps.println("public static void main(String[] args) {");

		ps.print(INDENT[indentLevel + 1]);
		ps.printf("for (int i = 0; i < %s.length; ++i) {", keyArrName);
		ps.println();

		ps.print(INDENT[indentLevel + 2]);
		ps.printf("int h = hash(%s[i]);", keyArrName);
		ps.println();

		ps.print(INDENT[indentLevel + 2]);
		ps.printf("if (h != i) {");
		ps.println();

		ps.print(INDENT[indentLevel + 3]);
		ps.printf("System.err.printf(\"Error while hashing \\\"%%s\\\": "
				+ "expected %%d but was %%d%%n\", %s[i], i, h);", keyArrName);
		ps.println();

		ps.print(INDENT[indentLevel + 2]);
		ps.println("}");

		ps.print(INDENT[indentLevel + 1]);
		ps.println("}");

		ps.print(INDENT[indentLevel]);
		ps.println("}");

		ps.print(INDENT[indentLevel]);
		ps.println("*/");
	}

	/**
	 * Prints the end of the class definition to the given stream.
	 *
	 * @param ps the stream to print to
	 */
	private void end(PrintStream ps) {
		ps.println();
		ps.print("}");
	}

	/**
	 * Prints the given block comment to the given stream. Each string within the
	 * given array is a line in the comment.
	 *
	 * @param ps          the stream to print to
	 * @param commentText the content of the comment
	 * @param indent      the indent string to prepend to each line of the comment
	 */
	private void printComment(PrintStream ps, String[] commentText, String indent) {
		ps.print(indent);
		ps.println("/*");

		for (String line : commentText) {
			ps.print(indent);
			ps.print(" * ");
			ps.println(line);
		}

		ps.print(indent);
		ps.println(" */");
	}

	/**
	 * Prints the given javadoc block comment to the given stream. Each string
	 * within the given array is a line in the comment.
	 *
	 * @param ps          the stream to print to
	 * @param commentText the content of the comment
	 * @param indent      the indent string to prepend to each line of the comment
	 */
	private void printDocComment(PrintStream ps, String[] commentText, String indent) {
		ps.print(indent);
		ps.println("/**");

		for (String line : commentText) {
			ps.print(indent);
			ps.print(" * ");
			ps.println(line);
		}

		ps.print(indent);
		ps.println(" */");
	}

	/**
	 * method of constructing one-dimensional int array in string form
	 *
	 * @param array The int array
	 * @return Converted digital rent string
	 */
	private static String buildIntArrayStr(int[] array) {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		for (int i = 0; i < array.length; i++) {
			if (i != array.length - 1) {
				sb.append(array[i]).append(",");
			} else {
				sb.append(array[i]);
			}
		}
		sb.append("}");
		return sb.toString();
	}

	/**
	 * method of constructing one-dimensional int 2D array in string form
	 * 
	 * @param array 2D int array
	 * @return Converted digital rent string
	 */
	private static String buildInt2DArrayStr(int[][] array) {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		for (int i = 0; i < array.length; i++) {
			if (i != array.length - 1) {
				sb.append(buildIntArrayStr(array[i])).append(",");
			} else {
				sb.append(buildIntArrayStr(array[i]));
			}
		}
		sb.append("}");
		return sb.toString();
	}

	/**
	 * method of constructing one-dimensional String array in string form
	 *
	 * @param array The String array
	 * @return Converted digital rent string
	 */
	private static String buildStringArrayStr(String[] array) {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		for (int i = 0; i < array.length; i++) {
			if (i != array.length - 1) {
				sb.append("\"").append(array[i]).append("\"").append(",");
			} else {
				sb.append("\"").append(array[i]).append("\"");
			}
		}
		sb.append("}");
		return sb.toString();
	}

	/**
	 * method of constructing one-dimensional int 2D array in string form
	 * 
	 * @param array 2D String array
	 * @return Converted digital rent string
	 */
	private static String buildString2DArrayStr(String[][] array) {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		for (int i = 0; i < array.length; i++) {
			if (i != array.length - 1) {
				sb.append(buildStringArrayStr(array[i])).append(",");
			} else {
				sb.append(buildStringArrayStr(array[i]));
			}
		}
		sb.append("}");
		return sb.toString();
	}
}
