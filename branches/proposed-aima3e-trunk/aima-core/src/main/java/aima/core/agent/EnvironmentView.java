package aima.core.agent;

/**
 * Allows external applications/logic to view the interaction of Agents with an Environment. 
 */

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public interface EnvironmentView {
	/**
	 * A simple notification message from the Environment, from one of its objects.
	 * 
	 * @param msg
	 *            the message received.
	 */
	void notify(String msg);

	/**
	 * Indicates an Agent has been added to the environment and what it
	 * perceives initially.
	 * 
	 * @param agent
	 *            the Agent just added to the Environment.
	 * @param perceives
	 *            what the Agent initially perceives.
	 */
	void agentAdded(Agent agent, Percept perceives);

	/**
	 * Indicates the Environment has changed as a result of an Agent's action.
	 * 
	 * @param agent
	 *            the Agent that performed the Action.
	 * @param action
	 *            the Action the Agent performed.
	 * @param resultingState
	 *            the EnvironmentState that resulted from the Agent's Action on
	 *            the Environment.
	 */
	void agentActed(Agent agent, Action action, EnvironmentState resultingState);
}
