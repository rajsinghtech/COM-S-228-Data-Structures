package edu.iastate.cs228.hw3;

import java.util.AbstractSequentialList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Implementation of the list interface based on linked nodes that store
 * multiple items per node. Rules for adding and removing elements ensure that
 * each node (except possibly the last one) is at least half full.
 */
public class StoutList<E extends Comparable<? super E>> extends AbstractSequentialList<E> {
	/**
	 * Default number of elements that may be stored in each node.
	 */
	private static final int DEFAULT_NODESIZE = 4;

	/**
	 * Number of elements that can be stored in each node.
	 */
	private final int nodeSize;

	/**
	 * Dummy node for head. It should be private but set to public here only for
	 * grading purpose. In practice, you should always make the head of a linked
	 * list a private instance variable.
	 */
	public Node head;

	/**
	 * Dummy node for tail.
	 */
	private Node tail;

	/**
	 * Number of elements in the list.
	 */
	private int size;

	/**
	 * Constructs an empty list with the default node size.
	 */
	public StoutList() {
		this(DEFAULT_NODESIZE);
	}

	/**
	 * Constructs an empty list with the given node size.
	 * 
	 * @param nodeSize number of elements that may be stored in each node, must be
	 *                 an even number
	 */
	public StoutList(int nodeSize) {
		if (nodeSize <= 0 || nodeSize % 2 != 0)
			throw new IllegalArgumentException();

		// dummy nodes
		head = new Node();
		tail = new Node();
		head.next = tail;
		tail.previous = head;
		this.nodeSize = nodeSize;
	}

	/**
	 * Constructor for grading only. Fully implemented.
	 * 
	 * @param head
	 * @param tail
	 * @param nodeSize i am a bitch
	 * @param size
	 */
	public StoutList(Node head, Node tail, int nodeSize, int size) {
		this.head = head;
		this.tail = tail;
		this.nodeSize = nodeSize;
		this.size = size;
	}

	/**
	 * @return the number of elements in the list
	 */
	@Override
	public int size() {
		return size;
	}

	/**
	 * Adds the item to the end of the list
	 * 
	 * @param item the item to add to the list
	 * @return true, if item was added successfully
	 * 		   false, if item already exists in the list
	 */
	@Override
	public boolean add(E item) {
		// if item is null throw NullPointerException
		if (item == null) {
			throw new NullPointerException();
		}
		
		if(contains(item))
			return false;

		// if it is an empty list
		if (size == 0) {
			Node n = new Node();
			n.addItem(item);
			head.next = n;
			n.previous = head;
			n.next = tail;
			tail.previous = n;
		} else {
			// if last node is NOT full, just add the item to the last node
			if (tail.previous.count < nodeSize) {
				tail.previous.addItem(item);
			}
			// if last node is full, create another node at the end and add the item
			else {
				Node n = new Node();
				n.addItem(item);
				Node temp = tail.previous;
				temp.next = n;
				n.previous = temp;
				n.next = tail;
				tail.previous = n;
			}
		}
		// increase the size of list, since item has been added
		size++;
		return true;
	}

	/**
	 * A simple contains method to search through list to check for a duplicate before adding
	 *
	 * @param item the item to search for
	 * @return whether the list contains the item or not
	 */
	public boolean contains(E item) {
		if(size < 1)
			return false;
		Node temp = head.next;
		while(temp != tail) {
			for(int i=0;i<temp.count;i++) {
				if(temp.data[i].equals(item))
					return true;
				temp = temp.next;
			}
		}
		return false;
	}

	/**
	 * Adds item to a specific index
	 * Basically followed the guideline from the project description
	 *
	 * @param pos the position where the item should go to
	 * @param item the item to add to the list
	 */
	@Override
	public void add(int pos, E item) {
		// TODO Auto-generated method stub
		// if pos is out of bound, throw IndexOutOfBoundsException
		if (pos < 0 || pos > size)
			throw new IndexOutOfBoundsException();

		// if the list is empty, create a new node and put X at offset 0
		if (head.next == tail)
			add(item);

		NodeInfo nodeInfo = find(pos);
		Node temp = nodeInfo.node;
		int offset = nodeInfo.offset;

		// otherwise if off = 0 and one of the following two cases occurs,
		if (offset == 0) {
			// if n has a predecessor which has fewer than M elements (and is not the head),
			// put X in n�s predecessor
			if (temp.previous.count < nodeSize && temp.previous != head) {
				temp.previous.addItem(item);
				size++;
				return;
			}
			// if n is the tail node and n�s predecessor has M elements, create a new node
			// and put X at offset 0
			else if (temp == tail) {
				add(item);
				size++;
				return;
			}
		}
		// otherwise if there is space in node n, put X in node n at offset off,
		// shifting array elements as necessary
		if (temp.count < nodeSize) {
			temp.addItem(offset, item);
		}
		// otherwise, perform a split operation: move the last M/2 elements of node n into
		// a new successor node n', and then 
		else {
			Node newSuccesor = new Node();
			int halfPoint = nodeSize / 2;
			int count = 0;
			while (count < halfPoint) {
				newSuccesor.addItem(temp.data[halfPoint]);
				temp.removeItem(halfPoint);
				count++;
			}

			Node oldSuccesor = temp.next;

			temp.next = newSuccesor;
			newSuccesor.previous = temp;
			newSuccesor.next = oldSuccesor;
			oldSuccesor.previous = newSuccesor;

			// if off <= M/2, put X in node n at offset off
			if (offset <= nodeSize / 2) {
				temp.addItem(offset, item);
			}
			// if off >= M/2, put X in node n' at offset (off - M/2)
			if (offset > nodeSize / 2) {
				newSuccesor.addItem((offset - nodeSize / 2), item);
			}

		}
		// increase the size of list, since item has been added
		size++;
	}

	/**
	 * Removes an item that is in a specific index
	 * Basically followed the guideline from the project description
	 * 
	 * @param pos the position where the item should be removed
	 * @return item that was removed
	 */
	@Override
	public E remove(int pos) {
		// TODO Auto-generated method stub
		if (pos < 0 || pos > size)
			throw new IndexOutOfBoundsException();
		NodeInfo nodeInfo = find(pos);
		Node temp = nodeInfo.node;
		int offset = nodeInfo.offset;
		E nodeValue = temp.data[offset];

		// if the node n containing X is the last node and has only one element, delete it
		if (temp.next == tail && temp.count == 1) {
			Node predecessor = temp.previous;
			predecessor.next = temp.next;
			temp.next.previous = predecessor;
			temp = null;
		}
		// otherwise, if n is the last node (thus with two or more elements),
		// or if n has more than M/2 elements, remove X from n, shifting elements as necessary; 
		else if (temp.next == tail || temp.count > nodeSize / 2) {
			temp.removeItem(offset);
		}
		// otherwise (the node n must have at most  elements), look at its successor n'
		// (note that we don�t look at the predecessor of n) and perform a merge operation as follows: 
		else {
			temp.removeItem(offset);
			Node succesor = temp.next;
			
			// if the successor node n' has more than  elements, move the first element from n' to n. (mini-merge) 
			if (succesor.count > nodeSize / 2) {
				temp.addItem(succesor.data[0]);
				succesor.removeItem(0);
			}
			// if the successor node n' has  or fewer elements, then move all elements from n' to n and delete n' (full merge) 
			else if (succesor.count <= nodeSize / 2) {
				for (int i = 0; i < succesor.count; i++) {
					temp.addItem(succesor.data[i]);
				}
				temp.next = succesor.next;
				succesor.next.previous = temp;
				succesor = null;
			}
		}
		// decrease the size of list, since item has been removed
		size--;
		return nodeValue;
	}

	/**
	 * Sort all elements in the stout list in the NON-DECREASING order. You may do
	 * the following. Traverse the list and copy its elements into an array,
	 * deleting every visited node along the way. Then, sort the array by calling
	 * the insertionSort() method. (Note that sorting efficiency is not a concern
	 * for this project.) Finally, copy all elements from the array back to the
	 * stout list, creating new nodes for storage. After sorting, all nodes but
	 * (possibly) the last one must be full of elements.
	 * 
	 * Comparator<E> must have been implemented for calling insertionSort().
	 */
	public void sort() {
		// TODO
		E[] sortDataList = (E[]) new Comparable[size];

		int tempIndex = 0;
		Node temp = head.next;
		while (temp != tail) {
			for (int i = 0; i < temp.count; i++) {
				sortDataList[tempIndex] = temp.data[i];
				tempIndex++;
			}
			temp = temp.next;
		}

		head.next = tail;
		tail.previous = head;

		insertionSort(sortDataList, new ElementComparator());
		size = 0;
		for (int i = 0; i < sortDataList.length; i++) {
			add(sortDataList[i]);
		}

	}

	/**
	 * Sort all elements in the stout list in the NON-INCREASING order. Call the
	 * bubbleSort() method. After sorting, all but (possibly) the last nodes must be
	 * filled with elements.
	 * 
	 * Comparable<? super E> must be implemented for calling bubbleSort().
	 */
	public void sortReverse() {
		// TODO
		E[] rsortDataList = (E[]) new Comparable[size];

		int tempIndex = 0;
		Node temp = head.next;
		while (temp != tail) {
			for (int i = 0; i < temp.count; i++) {
				rsortDataList[tempIndex] = temp.data[i];
				tempIndex++;
			}
			temp = temp.next;
		}

		head.next = tail;
		tail.previous = head;

		bubbleSort(rsortDataList);
		size = 0;
		for (int i = 0; i < rsortDataList.length; i++) {
			add(rsortDataList[i]);
		}
	}

	@Override
	public Iterator<E> iterator() {
		// TODO Auto-generated method stub
		return new StoutListIterator();
	}

	@Override
	public ListIterator<E> listIterator() {
		// TODO Auto-generated method stub
		return new StoutListIterator();
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		// TODO Auto-generated method stub
		return new StoutListIterator(index);
	}

	/**
	 * Returns a string representation of this list showing the internal structure
	 * of the nodes.
	 */
	public String toStringInternal() {
		return toStringInternal(null);
	}

	/**
	 * Returns a string representation of this list showing the internal structure
	 * of the nodes and the position of the iterator.
	 *
	 * @param iter an iterator for this list
	 */
	public String toStringInternal(ListIterator<E> iter) {
		int count = 0;
		int position = -1;
		if (iter != null) {
			position = iter.nextIndex();
		}

		StringBuilder sb = new StringBuilder();
		sb.append('[');
		Node current = head.next;
		while (current != tail) {
			sb.append('(');
			E data = current.data[0];
			if (data == null) {
				sb.append("-");
			} else {
				if (position == count) {
					sb.append("| ");
					position = -1;
				}
				sb.append(data.toString());
				++count;
			}

			for (int i = 1; i < nodeSize; ++i) {
				sb.append(", ");
				data = current.data[i];
				if (data == null) {
					sb.append("-");
				} else {
					if (position == count) {
						sb.append("| ");
						position = -1;
					}
					sb.append(data.toString());
					++count;

					// iterator at end
					if (position == size && count == size) {
						sb.append(" |");
						position = -1;
					}
				}
			}
			sb.append(')');
			current = current.next;
			if (current != tail)
				sb.append(", ");
		}
		sb.append("]");
		return sb.toString();
	}

	/**
	 * Node type for this list. Each node holds a maximum of nodeSize elements in an
	 * array. Empty slots are null.
	 */
	private class Node {
		/**
		 * Array of actual data elements.
		 */
		// Unchecked warning unavoidable.
		public E[] data = (E[]) new Comparable[nodeSize];

		/**
		 * Link to next node.
		 */
		public Node next;

		/**
		 * Link to previous node;
		 */
		public Node previous;

		/**
		 * Index of the next available offset in this node, also equal to the number of
		 * elements in this node.
		 */
		public int count;

		/**
		 * Adds an item to this node at the first available offset. Precondition: count
		 * < nodeSize
		 * 
		 * @param item element to be added
		 */
		void addItem(E item) {
			if (count >= nodeSize) {
				return;
			}
			data[count++] = item;
			// useful for debugging
			// System.out.println("Added " + item.toString() + " at index " + count + " to
			// node " + Arrays.toString(data));
		}

		/**
		 * Adds an item to this node at the indicated offset, shifting elements to the
		 * right as necessary.
		 * 
		 * Precondition: count < nodeSize
		 * 
		 * @param offset array index at which to put the new element
		 * @param item   element to be added
		 */
		void addItem(int offset, E item) {
			if (count >= nodeSize) {
				return;
			}
			for (int i = count - 1; i >= offset; --i) {
				data[i + 1] = data[i];
			}
			++count;
			data[offset] = item;
			// useful for debugging
//      System.out.println("Added " + item.toString() + " at index " + offset + " to node: "  + Arrays.toString(data));
		}

		/**
		 * Deletes an element from this node at the indicated offset, shifting elements
		 * left as necessary. Precondition: 0 <= offset < count
		 * 
		 * @param offset
		 */
		void removeItem(int offset) {
			E item = data[offset];
			for (int i = offset + 1; i < nodeSize; ++i) {
				data[i - 1] = data[i];
			}
			data[count - 1] = null;
			--count;
		}
	}

	/**
	 * Helper class to represent a specific point of the list
	 */
	private class NodeInfo {
		public Node node;
		public int offset;

		public NodeInfo(Node node, int offset) {
			this.node = node;
			this.offset = offset;
		}
	}

	/**
	 * Helper method to locate an specific item
	 * 
	 * @param pos position of item that needs a info
	 * @return NodeInfo of specific point of the list
	 */
	private NodeInfo find(int pos) {
		Node temp = head.next;
		int currPos = 0;
		while (temp != tail) {
			if (currPos + temp.count <= pos) {
				currPos += temp.count;
				temp = temp.next;
				continue;
			}

			NodeInfo nodeInfo = new NodeInfo(temp, pos - currPos);
			return nodeInfo;

		}
		return null;
	}

	/**
	 * Custom Iterator for StoutList
	 */
	private class StoutListIterator implements ListIterator<E> {

		final int LAST_ACTION_PREV = 0;
		final int LAST_ACTION_NEXT = 1;

		/**
		 * pointer of iterator
		 */
		int currentPosition;
		
		/**
		 * data structure of iterator in array form
		 */
		public E[] dataList;
		
		/**
		 * tracks the lastAction taken by the program
		 * it is mainly used for remove() and set() method to determine
		 * which item to remove or set
		 */
		int lastAction;

		/**
		 * Default constructor
		 * Sets the pointer of iterator to the beginning of the list
		 */
		public StoutListIterator() {
			currentPosition = 0;
			lastAction = -1;
			setup();
		}

		/**
		 * Constructor finds node at a given position.
		 * Sets the pointer of iterator to the specific index of the list
		 * 
		 * @param pos
		 */
		public StoutListIterator(int pos) {
			// TODO
			currentPosition = pos;
			lastAction = -1;
			setup();
		}

		/**
		 * Takes the StoutList and put its data into dataList in an array form
		 */
		private void setup() {
			dataList = (E[]) new Comparable[size];

			int tempIndex = 0;
			Node temp = head.next;
			while (temp != tail) {
				for (int i = 0; i < temp.count; i++) {
					dataList[tempIndex] = temp.data[i];
					tempIndex++;
				}
				temp = temp.next;
			}
		}

		/**
		 * @return whether iterator has next available value or not
		 */
		@Override
		public boolean hasNext() {
			if (currentPosition >= size)
				return false;
			else
				return true;
		}

		/**
		 * Returns the next ready value and shifts the pointer by 1
		 * 
		 * @return the next ready value of the iterator
		 */
		@Override
		public E next() {
			if (!hasNext())
				throw new NoSuchElementException();
			lastAction = LAST_ACTION_NEXT;
			return dataList[currentPosition++];
		}

		/**
		 * Removes from the list the last element returned by next() or previous().
		 * Can only be called once per call of next() or previous()
		 * Also removes the element from the StoutList
		 */
		@Override
		public void remove() {
			// TODO
			if (lastAction == LAST_ACTION_NEXT) {
				StoutList.this.remove(currentPosition - 1);
				setup();
				lastAction = -1;
				currentPosition--;
				if (currentPosition < 0)
					currentPosition = 0;
			} else if (lastAction == LAST_ACTION_PREV) {
				StoutList.this.remove(currentPosition);
				setup();
				lastAction = -1;
			} else {
				throw new IllegalStateException();
			}
		}

		/**
		 * @return whether iterator has previous available value or not
		 */
		@Override
		public boolean hasPrevious() {
			// TODO Auto-generated method stub
			if (currentPosition <= 0)
				return false;
			else
				return true;
		}

		/**
		 * @return index of next available element
		 */
		@Override
		public int nextIndex() {
			// TODO Auto-generated method stub
			return currentPosition;
		}
		
		/**
		 * Returns previous available element and shifts pointer by -1
		 * 
		 * @return previous available element
		 */
		@Override
		public E previous() {
			// TODO Auto-generated method stub
			if (!hasPrevious())
				throw new NoSuchElementException();
			lastAction = LAST_ACTION_PREV;
			currentPosition--;
			return dataList[currentPosition];
		}

		/**
		 * @return index of previous element
		 */
		@Override
		public int previousIndex() {
			// TODO Auto-generated method stub
			return currentPosition - 1;
		}

		/**
		 * Replaces the element at the current pointer
		 * 
		 * @param arg0 replacing element
		 */
		@Override
		public void set(E arg0) {
			// TODO Auto-generated method stub
			if (lastAction == LAST_ACTION_NEXT) {
				NodeInfo nodeInfo = find(currentPosition - 1);
				nodeInfo.node.data[nodeInfo.offset] = arg0;
				dataList[currentPosition - 1] = arg0;
			} else if (lastAction == LAST_ACTION_PREV) {
				NodeInfo nodeInfo = find(currentPosition);
				nodeInfo.node.data[nodeInfo.offset] = arg0;
				dataList[currentPosition] = arg0;
			} else {
				throw new IllegalStateException();
			}

		}

		/**
		 * Adds an element to the end of the list
		 * 
		 * @param arg0 adding element
		 */
		@Override
		public void add(E arg0) {
			// TODO Auto-generated method stub
			if (arg0 == null)
				throw new NullPointerException();

			StoutList.this.add(currentPosition, arg0);
			currentPosition++;
			setup();
			lastAction = -1;

		}

		// Other methods you may want to add or override that could possibly facilitate
		// other operations, for instance, addition, access to the previous element,
		// etc.
		//
		// ...
		//
	}

	/**
	 * Sort an array arr[] using the insertion sort algorithm in the NON-DECREASING
	 * order.
	 * 
	 * @param arr  array storing elements from the list
	 * @param comp comparator used in sorting
	 */
	private void insertionSort(E[] arr, Comparator<? super E> comp) {
		for (int i = 1; i < arr.length; i++) {
			E key = arr[i];
			int j = i - 1;

			while (j >= 0 && comp.compare(arr[j], key) > 0) {
				arr[j + 1] = arr[j];
				j--;
			}
			arr[j + 1] = key;
		}
	}

	/**
	 * Sort arr[] using the bubble sort algorithm in the NON-INCREASING order. For a
	 * description of bubble sort please refer to Section 6.1 in the project
	 * description. You must use the compareTo() method from an implementation of
	 * the Comparable interface by the class E or ? super E.
	 * 
	 * @param arr array holding elements from the list
	 */
	private void bubbleSort(E[] arr) {
		int n = arr.length;
		for (int i = 0; i < n - 1; i++) {
			for (int j = 0; j < n - i - 1; j++) {
				if (arr[j].compareTo(arr[j + 1]) < 0) {
					E temp = arr[j];
					arr[j] = arr[j + 1];
					arr[j + 1] = temp;
				}
			}
		}

	}

	/**
	 * Custom Comparator to be used by insertion sort.
	 */
	class ElementComparator<E extends Comparable<E>> implements Comparator<E> {
		@Override
		public int compare(E arg0, E arg1) {
			// TODO Auto-generated method stub
			return arg0.compareTo(arg1);
		}

	}

}