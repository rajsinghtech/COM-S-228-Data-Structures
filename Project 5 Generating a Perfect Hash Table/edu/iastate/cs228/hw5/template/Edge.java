package edu.iastate.cs228.hw5.template;

/**
 * An interface representing an edge in the PHT graph.
 */
public interface Edge extends Visitable {
	/**
	 * Returns the index of the word represented by this edge within the word list.
	 *
	 * @return the index of the word represented by this edge
	 */
	int index();

	/**
	 * Returns the word represented by this edge.
	 *
	 * @return the word represented by this edge
	 */
	String data();

	/**
	 * Returns the vertex from which that this edge is outgoing.
	 *
	 * @return the vertex from which that this edge is outgoing
	 */
	Vertex getFrom();

	/**
	 * Returns the vertex to which that this edge is incoming.
	 *
	 * @return the vertex to which that this edge is incoming
	 */
	Vertex getTo();
}
