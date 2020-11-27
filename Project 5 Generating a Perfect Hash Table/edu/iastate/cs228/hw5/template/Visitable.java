package edu.iastate.cs228.hw5.template;

/**
 * An interface representing an entity that can be marked as visited or
 * unvisited.
 */
public interface Visitable {
	/**
	 * Returns whether this object is marked as visited.
	 *
	 * @return whether this object is marked as visited
	 */
	boolean isVisited();

	/**
	 * Sets whether this object is marked as visited. Additional operations may be
	 * performed.
	 *
	 * @param visited the new visited state
	 */
	void setVisited(boolean visited);

	/**
	 * Equivalent to {@link #setVisited(boolean) setVisited}{@code (true)}.
	 */
	default void visit() {
		this.setVisited(true);
	}

	/**
	 * Equivalent to {@link #setVisited(boolean) setVisited}{@code (false)}.
	 */
	default void unvisit() {
		this.setVisited(false);
	}
}
