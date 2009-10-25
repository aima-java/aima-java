package aima.test.core.unit.search.uninformed;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import aima.core.agent.Action;
import aima.core.search.framework.GraphSearch;
import aima.core.search.framework.Problem;
import aima.core.search.framework.Search;
import aima.core.search.framework.SearchAgent;
import aima.core.search.nqueens.NQueensBoard;
import aima.core.search.nqueens.NQueensGoalTest;
import aima.core.search.nqueens.NQueensSuccessorFunction;
import aima.core.search.uninformed.DepthFirstSearch;

public class DepthFirstSearchTest {

	@Test
	public void testDepthFirstSuccesfulSearch() throws Exception {
		Problem problem = new Problem(new NQueensBoard(8),
				new NQueensSuccessorFunction(), new NQueensGoalTest());
		Search search = new DepthFirstSearch(new GraphSearch());
		SearchAgent agent = new SearchAgent(problem, search);
		List<Action> actions = agent.getActions();
		assertCorrectPlacement(actions);
		Assert.assertEquals("113", agent.getInstrumentation().getProperty(
				"nodesExpanded"));

	}

	@Test
	public void testDepthFirstUnSuccessfulSearch() throws Exception {
		Problem problem = new Problem(new NQueensBoard(3),
				new NQueensSuccessorFunction(), new NQueensGoalTest());
		Search search = new DepthFirstSearch(new GraphSearch());
		SearchAgent agent = new SearchAgent(problem, search);
		List<Action> actions = agent.getActions();
		Assert.assertEquals(0, actions.size());
		Assert.assertEquals("6", agent.getInstrumentation().getProperty(
				"nodesExpanded"));
	}

	//
	// PRIVATE METHODS
	//
	private void assertCorrectPlacement(List<Action> actions) {
		Assert.assertEquals(8, actions.size());		
		Assert.assertEquals("Action[name==placeQueenAt, x==0, y==7]", actions.get(0).toString());
		Assert.assertEquals("Action[name==placeQueenAt, x==1, y==3]", actions.get(1).toString());
		Assert.assertEquals("Action[name==placeQueenAt, x==2, y==0]", actions.get(2).toString());
		Assert.assertEquals("Action[name==placeQueenAt, x==3, y==2]", actions.get(3).toString());
		Assert.assertEquals("Action[name==placeQueenAt, x==4, y==5]", actions.get(4).toString());
		Assert.assertEquals("Action[name==placeQueenAt, x==5, y==1]", actions.get(5).toString());
		Assert.assertEquals("Action[name==placeQueenAt, x==6, y==6]", actions.get(6).toString());
		Assert.assertEquals("Action[name==placeQueenAt, x==7, y==4]", actions.get(7).toString());
	}
}
