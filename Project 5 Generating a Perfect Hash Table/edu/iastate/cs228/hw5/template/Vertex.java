package edu.iastate.cs228.hw5.template;

import java.util.Collection;

/**
 * An interface representing a vertex in the PHT graph.
 */
public interface Vertex extends Visitable {
	/**
	 * The index of this vertex within the graph.
	 *
	 * @return the index of this vertex within the graph
	 */
	int index();

	/**
	 * Returns the collection of the outgoing edges of this vertex. (The collection
	 * is the one used internally by the vertex.)
	 *
	 * @return the collection of the outgoing edges
	 */
	Collection<Edge> edges();

	/**
	 * Recursively fills the G array starting at this vertex.
	 *
	 * @param g     the array to fill
	 * @param words the number of keys in the PHT
	 *
	 * @throws IndexOutOfBoundsException if one occurs (e.g. the array is of
	 *                                   insufficient length)
	 */
	void fillGArray(int[] g, int words) throws IndexOutOfBoundsException;

	/**
	 * Determines if this vertex leads to a cycle with a depth-first traversal.
	 *
	 * If the vertex is already visited, a cycle has been detected. Otherwise, marks
	 * the vertex as visited, then checks its neighbors (except for {@code from}) by
	 * calling {@code hasCycle(this)} on them.
	 *
	 * @param from the vertex from which this vertex was visited
	 * @return true if and only if there is a cycle
	 */
	boolean hasCycle(Vertex from);

	/**
	 * {@inheritDoc}
	 *
	 * If {@code visited} is false, also unvisits the outgoing edges of this vertex.
	 */
	@Override
	void setVisited(boolean visited);
}
