package aima.core.search.framework;

import java.util.Set;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): page ??.<br>
 * <br>
 * Given a particular state s, ACTIONS(s) returns the set of actions that can be
 * executed in s. We say that each of these actions is <b>applicable</b> in s.
 * 
 * @author Ciaran O'Reilly
 * 
 */
public interface ActionsFunction<A> {
	/**
	 * Given a particular state s, returns the set of actions that can be
	 * executed in s.
	 * 
	 * @param s
	 *            a particular state.
	 * @return the set of actions that can be executed in s.
	 */
	Set<A> actions(Object s);
}
