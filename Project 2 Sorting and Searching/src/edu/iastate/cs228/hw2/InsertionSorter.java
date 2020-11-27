package edu.iastate.cs228.hw2;

import java.util.Comparator;

/**
 * An implementation of {@link Sorter} that performs insertion sort to sort the
 * list.
 * 
 * @author Raj Singh
 */
public class InsertionSorter extends Sorter {
	@Override
	public void sort(WordList toSort, Comparator<String> comp) throws NullPointerException {
		// TODO

		for (int i = 1; i < toSort.length(); i++) {// loops based on length
			int j = i;

			while (j > 0 && (comp.compare(toSort.get(j), toSort.get(j - 1)) < 0)) {
				String temp = toSort.get(j); // swaps values
				toSort.set(j, toSort.get(j - 1));
				toSort.set(j - 1, temp);
				j--;
			}
		}
	}
}
