package edu.iastate.cs228.hw2;

import java.util.Comparator;

/**
 * A string comparator that uses an ordering of an {@link Alphabet} to determine
 * how to compare individual characters.
 * 
 * @author Raj Singh
 */
public class AlphabetComparator implements Comparator<String> {
	/**
	 * The ordering used to compare characters.
	 */

	private Alphabet alphabet;

	/**
	 * Constructs and initializes the comparator to use the given ordering.
	 * 
	 * @param ordering the ordering to use to compare characters
	 * @throws NullPointerException if {@code ordering} is {@code null}
	 */
	public AlphabetComparator(Alphabet ordering) throws NullPointerException {
		// TODO
		this.alphabet = ordering;
	}

	/**
	 * Compares the two given strings based on the ordering used by this comparator.
	 * 
	 * Returns a negative value if the first string is considered less than the
	 * second, a positive value if greater than the second, and zero if the two
	 * strings are equal. Note that an exception must be thrown if the strings
	 * contain invalid characters, even if the two strings are equal.
	 * 
	 * For each character of the given strings, the ordering is consulted to
	 * determine which of the two characters should go first, with the one with a
	 * lesser position in the ordering being determined to be lesser. If the
	 * position of the characters are the same, the next character is examined.
	 * After the end of one of the strings is reached, the shorter string is
	 * considered to be lesser than the other.
	 * 
	 * @throws NullPointerException     if either of {@code a} or {@code b} are
	 *                                  {@code null}
	 * @throws IllegalArgumentException if either of {@code a} or {@code b} contain
	 *                                  a character not found in this comparator's
	 *                                  ordering
	 */
	@Override
	public int compare(String a, String b) throws NullPointerException, IllegalArgumentException {
		// TODO

		if (a == null || b == null)
			throw new NullPointerException("String a or b was null");
		for (int i = 0; i < a.length(); i++) {
			if (!this.alphabet.isValid(a.charAt(i)))
				throw new IllegalArgumentException("Value from string a was not in alphabet");
		}
		for (int i = 0; i < b.length(); i++) {
			if (!this.alphabet.isValid(b.charAt(i)))
				throw new IllegalArgumentException("Value from string b was not in alphabet");
		}

		int length1 = a.length();
		int length2 = b.length();
		int checker = (length1 < length2) ? length1 : length2;

		for (int i = 0; i < checker; i++) {
			int location1 = this.alphabet.getPosition(a.charAt(i));
			int location2 = this.alphabet.getPosition(b.charAt(i));
			if (this.alphabet.getPosition(a.charAt(i)) < this.alphabet.getPosition(b.charAt(i)))
				return -1;
			if (this.alphabet.getPosition(a.charAt(i)) > this.alphabet.getPosition(b.charAt(i)))
				return 1;
		}
		if (length1 < length2)
			return -1;
		else if (length1 > length2)
			return 1;
		else
			return 0;
	}
}
