package edu.iastate.cs228.hw4;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Asks user for file name pulls file and finds the pattern and encodes message.
 * Calling msgtree
 * 
 * @author rajsingh
 *
 */
public class Main {
	public static void main(String[] args) throws IOException {
		System.out.println("Please enter filename to decode:");
		Scanner sc = new Scanner(System.in);
		String fileName = sc.nextLine();
		sc.close();

		// read all chars to variable content
		String content = new String(Files.readAllBytes(Paths.get(fileName))).trim();
		int pos = content.lastIndexOf('\n');
		String pattern = content.substring(0, pos); // pattern
		String binCode = content.substring(pos).trim(); // encoded message

		Set<Character> chars = new HashSet<>();
		for (char c : pattern.toCharArray()) {
			if (c != '^') {
				chars.add(c);
			}
		}
		String chardict = chars.stream().map(String::valueOf).collect(Collectors.joining());

		// call edu.iastate.cs228.hw4.MsgTree method
		MsgTree root = new MsgTree(pattern);
		MsgTree.printCodes(root, chardict);
		root.decode(root, binCode);
	}
}
