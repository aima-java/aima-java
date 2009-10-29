package aima.core.search.online;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import aima.core.agent.Action;
import aima.core.agent.Percept;
import aima.core.agent.impl.AbstractAgent;
import aima.core.agent.impl.NoOpAction;
import aima.core.search.framework.Problem;

/**
 * Artificial Intelligence A Modern Approach (2nd Edition): Figure 4.20, page 126.
 * <code>
 * function ONLINE-DFS-AGENT(s') returns an action
 *   inputs: s', a percept that identifies the current state
 *   static: result, a table, indexed by action and state, initially empty
 *           unexplored, a table that lists, for each visited state, the actions not yet tried
 *           unbacktracked, a table that lists, for each visited state, the backtracks not yet tried
 *           s, a, the previous state and action, initially null
 *    
 *   if GOAL-TEST(s') then return stop
 *   if s' is a new state then unexplored[s'] <- ACTIONS(s')
 *   if s is not null then do
 *       result[a, s] <- s'
 *       add s to the front of the unbacktracked[s']
 *   if unexplored[s'] is empty then
 *       if unbacktracked[s'] is empty then return stop
 *       else a <- an action b such that result[b, s'] = POP(unbacktracked[s'])
 *   else a <- POP(unexplored[s'])
 *   s <- s'
 *   return a
 * </code>
 * Figure 4.20 An online search agent that uses depth-first exploration. The agent is
 * applicable only in bidirectional search spaces.<br>
 * <br>
 * Note: This algorithm fails to exit if the goal does not exist (e.g. A<->B Goal=X),
 * this could be an issue with the implementation. Comments welcome.
 */

/**
 * @author Ciaran O'Reilly
 * 
 */
public class OnlineDFSAgent extends AbstractAgent {

	private Problem problem;
	// static: result, a table, indexed by action and state, initially empty
	private final Map<ActionState, Percept> result = new HashMap<ActionState, Percept>();
	// unexplored, a table that lists, for each visited state, the actions not
	// yet tried
	private final Map<Percept, List<Action>> unexplored = new HashMap<Percept, List<Action>>();
	// unbacktracked, a table that lists,
	// for each visited state, the backtracks not yet tried
	private final Map<Percept, List<Percept>> unbacktracked = new HashMap<Percept, List<Percept>>();
	// s, a, the previous state and action, initially null
	private Percept s = null;
	private Action a = null;

	public OnlineDFSAgent(Problem problem) {
		setProblem(problem);
	}

	public Problem getProblem() {
		return problem;
	}

	public void setProblem(Problem problem) {
		this.problem = problem;
		init();
	}

	// function ONLINE-DFS-AGENT(s') returns an action
	// inputs: s', a percept that identifies the current state
	@Override
	public Action execute(Percept sComma) {
		// if GOAL-TEST(s') then return stop
		if (!goalTest(sComma)) {
			// if s' is a new state then unexplored[s'] <- ACTIONS(s')
			if (!unexplored.containsKey(sComma)) {
				unexplored.put(sComma, actions(sComma));
			}

			// if s is not null then do
			if (null != s) {
				// result[a, s] <- s'
				result.put(new ActionState(a, s), sComma);

				// Ensure the unbacktracked always has a list for s'
				if (!unbacktracked.containsKey(sComma)) {
					unbacktracked.put(sComma, new ArrayList<Percept>());
				}

				// add s to the front of the unbacktracked[s']
				unbacktracked.get(sComma).add(s);
			}
			// if unexplored[s'] is empty then
			if (unexplored.get(sComma).size() == 0) {
				// if unbacktracked[s'] is empty then return stop
				if (unbacktracked.get(sComma).size() == 0) {
					a = NoOpAction.NO_OP;
				} else {
					// else a <- an action b such that result[b, s'] =
					// POP(unbacktracked[s'])
					Percept popped = unbacktracked.get(sComma).remove(
							unbacktracked.get(sComma).size() - 1);
					for (ActionState as : result.keySet()) {
						if (as.getState().equals(sComma)
								&& result.get(as).equals(popped)) {
							a = as.getAction();
							break;
						}
					}
				}
			} else {
				// else a <- POP(unexplored[s'])
				a = unexplored.get(sComma).remove(
						unexplored.get(sComma).size() - 1);
			}
		} else {
			a = NoOpAction.NO_OP;
		}

		if (a.isNoOp()) {
			// I'm either at the Goal or can't get to it,
			// which in either case I'm finished so just die.
			setAlive(false);
		}

		// s <- s'
		s = sComma;
		// return a
		return a;
	}

	//
	// PRIVATE METHODS
	//

	private void init() {
		setAlive(true);
		result.clear();
		unexplored.clear();
		unbacktracked.clear();
		s = null;
		a = null;
	}

	private boolean goalTest(Percept state) {
		return getProblem().isGoalState(state);
	}

	private List<Action> actions(Percept state) {
		return new ArrayList<Action>(problem.getActionsFunction().actions(state));
	}
}
