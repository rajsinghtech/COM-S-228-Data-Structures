package edu.iastate.cs228.hw2;

import java.util.Comparator;

/**
 * An implementation of {@link Sorter} that performs quick sort to sort the
 * list.
 * 
 * @author
 */
public class QuickSorter extends Sorter {
	@Override
	/**
	 * sets start and end and calls recursive quick sort
	 */
	public void sort(WordList toSort, Comparator<String> comp) throws NullPointerException {
		// TODO
		int start = 0;
		int end = toSort.length() - 1;
		quickSortRec(toSort, comp, start, end);
	}

	/**
	 * recursivly calls itself assigning partition beforehand
	 * 
	 * @param list
	 * @param comp
	 * @param start
	 * @param end
	 */
	private void quickSortRec(WordList list, Comparator<String> comp, int start, int end) {
		// TODO
		if (start < end) {
			int pi = partition(list, comp, start, end);

			quickSortRec(list, comp, start, pi - 1);
			quickSortRec(list, comp, pi + 1, end);
		}
	}

	/**
	 * constructs array and pivot point and notifies changes
	 * 
	 * @param list
	 * @param comp
	 * @param start
	 * @param end
	 * @return
	 */
	private int partition(WordList list, Comparator<String> comp, int start, int end) {
		// TODO
		String[] listArray = list.getArray();
		String pivot = listArray[end];
		int i = start - 1;

		for (int j = start; j < end; j++) {
			if (comp.compare(listArray[j], pivot) < 0) { // compares to pivot
				i++;
				String temp = listArray[i];
				listArray[i] = listArray[j];
				listArray[j] = temp;
			}
		}

		String temp = listArray[i + 1];
		listArray[i + 1] = listArray[end];
		listArray[end] = temp;

		return i + 1;

	}
}