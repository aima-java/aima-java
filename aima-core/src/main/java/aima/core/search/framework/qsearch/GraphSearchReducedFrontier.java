package aima.core.search.framework.qsearch;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import aima.core.agent.Action;
import aima.core.search.framework.Node;
import aima.core.search.framework.Problem;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Figure 3.7, page 77.
 * <br>
 * 
 * <pre>
 * function GRAPH-SEARCH(problem) returns a solution, or failure
 *   initialize the frontier using the initial state of problem
 *   initialize the explored set to be empty
 *   loop do
 *     if the frontier is empty then return failure
 *     choose a leaf node and remove it from the frontier
 *     if the node contains a goal state then return the corresponding solution
 *     add the node to the explored set
 *     expand the chosen node, adding the resulting nodes to the frontier
 *       only if not in the frontier or explored set
 * </pre>
 * 
 * Figure 3.7 An informal description of the general graph-search algorithm.
 * 
 * <br>
 * This implementation is based on the template method
 * {@link #search(Problem, Queue)} from superclass {@link QueueSearch} and
 * provides implementations for the needed primitive operations. It implements a
 * special version of graph search which keeps the frontier short by focusing on
 * the best node for each state only. It should only be used in combination with
 * priority queue frontiers. If a node is added to the frontier, this
 * implementation checks whether another node for the same state already exists
 * and decides whether to replace it or ignore the new node depending on the
 * node's costs (comparator of priority queue is used, if available).
 * 
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 */
public class GraphSearchReducedFrontier extends QueueSearch {

	private Set<Object> explored = new HashSet<Object>();
	private Map<Object, Node> frontierNodeLookup = new HashMap<Object, Node>();
	private Comparator<Node> nodeComparator = null;

	public Comparator<Node> getNodeComparator() {
		return nodeComparator;
	}

	/**
	 * Sets the comparator if a priority queue is used, resets explored list and
	 * state map and calls the inherited version of search.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Action> search(Problem problem, Queue<Node> frontier) {
		// initialize the explored set to be empty
		if (frontier instanceof PriorityQueue<?>)
			nodeComparator = (Comparator<Node>) ((PriorityQueue<?>) frontier).comparator();
		explored.clear();
		frontierNodeLookup.clear();
		return super.search(problem, frontier);
	}

	/**
	 * Inserts the node at the tail of the frontier.
	 */
	@Override
	protected void addToFrontier(Node node) {
		Node frontierNode = frontierNodeLookup.get(node.getState());

		if (null == frontierNode) {
			if (!explored.contains(node.getState())) {
				// child.STATE is not in frontier and not yet explored
				frontier.add(node);
				frontierNodeLookup.put(node.getState(), node);
				updateMetrics(frontier.size());
			}
		} else if (null != nodeComparator && nodeComparator.compare(node, frontierNode) < 0) {
			// child.STATE is in frontier with higher cost
			// replace that frontier node with child
			if (frontier.remove(frontierNode))
				frontierNodeLookup.remove(frontierNode.getState());
			frontier.add(node);
			frontierNodeLookup.put(node.getState(), node);
			updateMetrics(frontier.size());
		}
	}

	/**
	 * Removes the node at the head of the frontier, adds the corresponding
	 * state to the explored set, and returns the node.
	 * 
	 * @return the node at the head of the frontier.
	 */
	@Override
	protected Node removeFromFrontier() {
		Node result = frontier.remove();
		frontierNodeLookup.remove(result.getState());
		// add the node to the explored set
		explored.add(result.getState());
		updateMetrics(frontier.size());
		return result;
	}

	/**
	 * Checks whether the frontier contains not yet expanded nodes.
	 */
	@Override
	protected boolean isFrontierEmpty() {
		return frontier.isEmpty();
	}
}