package aima.core.search.framework;

import java.util.ArrayList;
import java.util.List;

import aima.core.agent.Action;
import aima.core.util.datastructure.Queue;

/**
 * @author Ravi Mohan
 * 
 */
public abstract class QueueSearch extends NodeExpander {
	public static final String METRIC_QUEUE_SIZE = "queueSize";

	public static final String METRIC_MAX_QUEUE_SIZE = "maxQueueSize";

	public static final String METRIC_PATH_COST = "pathCost";

	// Note: This follows the general structure of TREE-SEARCH as described in fig 3.7.
	public List<Action> search(Problem problem, Queue<Node> frontier) {
		clearInstrumentation();
		// initialize the frontier using the initial state of the problem
		frontier.insert(new Node(problem.getInitialState()));
		setQueueSize(frontier.size());
		while (!(frontier.isEmpty())) {
			// choose a leaf node and remove it from the frontier
			Node nodeToExpand = removeNodeFromFrontier(frontier);
			setQueueSize(frontier.size());
			// if the node contains a goal state then return the corresponding solution
			if (problem.isGoalState(nodeToExpand.getState())) {
				setPathCost(nodeToExpand.getPathCost());
				return SearchUtils.actionsFromNodes(nodeToExpand.getPathFromRoot());
			}
			// expand the chosen node, adding the resulting nodes to the frontier
			expandNodeAddingResultingNodesToFrontier(frontier, nodeToExpand, problem);
			setQueueSize(frontier.size());
		}
		// if the frontier is empty then return failure
		// Note: we use the empty List to indicate failure
		return new ArrayList<Action>();
	}
	
	public Node removeNodeFromFrontier(Queue<Node> frontier) {
		return frontier.pop();
	}
	
	public abstract void expandNodeAddingResultingNodesToFrontier(Queue<Node> frontier, Node nodeToExpand,
			Problem p);
	
	@Override
	public void clearInstrumentation() {
		super.clearInstrumentation();
		metrics.set(METRIC_QUEUE_SIZE, 0);
		metrics.set(METRIC_MAX_QUEUE_SIZE, 0);
		metrics.set(METRIC_PATH_COST, 0);
	}

	public int getQueueSize() {
		return metrics.getInt("queueSize");
	}

	public void setQueueSize(int queueSize) {

		metrics.set(METRIC_QUEUE_SIZE, queueSize);
		int maxQSize = metrics.getInt(METRIC_MAX_QUEUE_SIZE);
		if (queueSize > maxQSize) {
			metrics.set(METRIC_MAX_QUEUE_SIZE, queueSize);
		}
	}

	public int getMaxQueueSize() {
		return metrics.getInt(METRIC_MAX_QUEUE_SIZE);
	}

	public double getPathCost() {
		return metrics.getDouble(METRIC_PATH_COST);
	}

	public void setPathCost(Double pathCost) {
		metrics.set(METRIC_PATH_COST, pathCost);
	}
}