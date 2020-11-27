package edu.iastate.cs228.hw4;

import java.util.Stack;

/**
 * 
 * @author rajsingh
 *
 */
public class MsgTree {
	public char payloadChar;
	public MsgTree left;
	public MsgTree right;
	// Need static char idx to the tree string for recursive solution
	private static int staticCharIdx = 0;

	/**
	 * Constructor building the tree from a string
	 * 
	 * @param encodingString string pulled from data file
	 */
	public MsgTree(String encodingString) {
		if (encodingString == null || encodingString.length() < 2) {
			return;
		}
		Stack<MsgTree> stk = new Stack<>();
		int idx = 0;
		this.payloadChar = encodingString.charAt(idx++);
		stk.push(this);
		MsgTree cur = this;
		String lastOpt = "in";
		while (idx < encodingString.length()) {
			MsgTree node = new MsgTree(encodingString.charAt(idx++));
			if (lastOpt.equals("in")) {
				cur.left = node;
				if (node.payloadChar == '^') {
					cur = stk.push(node);
					lastOpt = "in";
				} else {
					if (!stk.empty())
						cur = stk.pop();
					lastOpt = "out";
				}
			} else { // lastOpt is out
				cur.right = node;
				if (node.payloadChar == '^') {
					cur = stk.push(node);
					lastOpt = "in";
				} else {
					if (!stk.empty())
						cur = stk.pop();
					lastOpt = "out";
				}
			}
		}
	}

	/**
	 * Constructor for a single node with null children
	 * 
	 * @param payloadChar
	 */
	public MsgTree(char payloadChar) {
		this.payloadChar = payloadChar;
		this.left = null;
		this.right = null;
	}

	/**
	 * Method to print characters and their binary codes
	 * 
	 * @param root
	 * @param code
	 */
	public static void printCodes(MsgTree root, String code) {
		System.out.println("character code\n-------------------------");
		for (char ch : code.toCharArray()) {
			getCode(root, ch, binCode = "");
			System.out.println("    " + (ch == '\n' ? "\\n" : ch + " ") + "    " + binCode);
		}
	}

	private static String binCode;

	/**
	 * Gets code and recursivly calls itself setting the alphabet
	 * 
	 * @param root
	 * @param ch
	 * @param path
	 * @return
	 */
	private static boolean getCode(MsgTree root, char ch, String path) {
		if (root != null) {
			if (root.payloadChar == ch) {
				binCode = path;
				return true;
			}
			return getCode(root.left, ch, path + "0") || getCode(root.right, ch, path + "1");
		}
		return false;
	}

	/**
	 * Decodes message using the pulled code alphabet
	 * 
	 * @param codes
	 * @param msg
	 */
	public void decode(MsgTree codes, String msg) {
		System.out.println("MESSAGE:");
		MsgTree cur = codes;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < msg.length(); i++) {
			char ch = msg.charAt(i);
			cur = (ch == '0' ? cur.left : cur.right);
			if (cur.payloadChar != '^') {
				getCode(codes, cur.payloadChar, binCode = "");
				sb.append(cur.payloadChar);
				cur = codes;
			}
		}
		System.out.println(sb.toString());
		statistc(msg, sb.toString());
	}

	/**
	 * Extra credit statistics. Pulls the encoded and decoded strings data to print
	 * 
	 * @param encodeStr
	 * @param decodeStr
	 */
	private void statistc(String encodeStr, String decodeStr) {
		System.out.println("STATISTICS:");
		System.out.println(String.format("Avg bits/char:\t%.1f", encodeStr.length() / (double) decodeStr.length()));
		System.out.println("Total Characters:\t" + decodeStr.length());
		System.out.println(
				String.format("Space Saving:\t%.1f%%", (1d - decodeStr.length() / (double) encodeStr.length()) * 100));
	}
}
