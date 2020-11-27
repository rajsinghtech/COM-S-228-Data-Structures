package edu.iastate.cs228.hw2;

import java.util.Comparator;

/**
 * An implementation of {@link Sorter} that performs merge sort to sort the
 * list.
 * 
 * @author
 */
public class MergeSorter extends Sorter {
	@Override
	/**
	 * checks if vals null and calls them into recusive sort
	 */
	public void sort(WordList toSort, Comparator<String> comp) throws NullPointerException {
		// TODO
		if (toSort == null || comp == null) {
			throw new NullPointerException();
		}
		mergeSortRec(toSort, comp);

	}

	/**
	 * recursivly calls itself assigning left and right of section and splitting
	 * into 1 lengths then calls merge to combine changes
	 * 
	 * @param list
	 * @param comp
	 */
	private void mergeSortRec(WordList list, Comparator<String> comp) {
		if (list.getArray().length > 1) { // checks if selected is 1
			String[] partition1 = new String[list.length() / 2];
			System.arraycopy(list.getArray(), 0, partition1, 0, list.length() / 2);
			WordList list1 = new WordList(partition1);
			String[] partition2 = new String[list.length() - partition1.length];
			System.arraycopy(list.getArray(), partition1.length, partition2, 0, partition2.length);
			WordList list2 = new WordList(partition2);
			mergeSortRec(list1, comp); // calls
			mergeSortRec(list2, comp);
			merge(list1, list2, list, comp); // merges string
		}
	}

	/**
	 * checks if vals should be swapped by comparing them then copies them in
	 * 
	 * @param left
	 * @param right
	 * @param list
	 * @param comp
	 */
	private void merge(WordList left, WordList right, WordList list, Comparator<String> comp) {
		int i = 0;
		int j = 0;
		int k = 0;
		while (i < left.length() && j < right.length()) { // moves positions
			if (comp.compare(left.get(i), right.get(j)) < 0) {
				list.set(k, left.get(i));
				i++;
			} else {
				list.set(k, right.get(j));
				j++;
			}
			k++;
		}
		System.arraycopy(left.getArray(), i, list.getArray(), k, left.length() - i);
		System.arraycopy(right.getArray(), j, list.getArray(), k, right.length() - j);

	}
}