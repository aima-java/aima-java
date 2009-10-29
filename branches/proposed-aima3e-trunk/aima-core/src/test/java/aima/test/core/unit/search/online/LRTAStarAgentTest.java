package aima.test.core.unit.search.online;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.agent.Action;
import aima.core.agent.Agent;
import aima.core.agent.EnvironmentState;
import aima.core.agent.EnvironmentView;
import aima.core.search.framework.HeuristicFunction;
import aima.core.search.map.BidirectionalMapProblem;
import aima.core.search.map.ExtendableMap;
import aima.core.search.map.MapEnvironment;
import aima.core.search.online.LRTAStarAgent;

public class LRTAStarAgentTest {
	ExtendableMap aMap;

	StringBuffer envChanges;

	HeuristicFunction hf;

	@Before
	public void setUp() {
		aMap = new ExtendableMap();
		aMap.addBidirectionalLink("A", "B", 4.0);
		aMap.addBidirectionalLink("B", "C", 4.0);
		aMap.addBidirectionalLink("C", "D", 4.0);
		aMap.addBidirectionalLink("D", "E", 4.0);
		aMap.addBidirectionalLink("E", "F", 4.0);
		hf = new HeuristicFunction() {
			public double getHeuristicValue(Object state) {
				return 1;
			}
		};

		envChanges = new StringBuffer();
	}

	@Test
	public void testAlreadyAtGoal() {
		MapEnvironment me = new MapEnvironment(aMap);
		LRTAStarAgent agent = new LRTAStarAgent(new BidirectionalMapProblem(me
				.getMap(), "A", "A", hf));
		me.addAgent(agent, "A");
		me.addEnvironmentView(new EnvironmentView() {
			public void notify(String msg) {
				envChanges.append(msg).append("->");
			}

			public void envChanged(Agent agent, Action action, EnvironmentState state) {
				envChanges.append(action).append("->");
			}
		});
		me.stepUntilDone();

		Assert.assertEquals("Action[name==NoOp]->", envChanges.toString());
	}

	@Test
	public void testNormalSearch() {
		MapEnvironment me = new MapEnvironment(aMap);
		LRTAStarAgent agent = new LRTAStarAgent(new BidirectionalMapProblem(me
				.getMap(), "A", "F", hf));
		me.addAgent(agent, "A");
		me.addEnvironmentView(new EnvironmentView() {
			public void notify(String msg) {
				envChanges.append(msg).append("->");
			}

			public void envChanged(Agent agent, Action action, EnvironmentState state) {
				envChanges.append(action).append("->");
			}
		});
		me.stepUntilDone();

		Assert
				.assertEquals(
						"Action[name==moveTo, location==B, distance==4.0]->Action[name==moveTo, location==A, distance==4.0]->Action[name==moveTo, location==B, distance==4.0]->Action[name==moveTo, location==C, distance==4.0]->Action[name==moveTo, location==B, distance==4.0]->Action[name==moveTo, location==C, distance==4.0]->Action[name==moveTo, location==D, distance==4.0]->Action[name==moveTo, location==C, distance==4.0]->Action[name==moveTo, location==D, distance==4.0]->Action[name==moveTo, location==E, distance==4.0]->Action[name==moveTo, location==D, distance==4.0]->Action[name==moveTo, location==E, distance==4.0]->Action[name==moveTo, location==F, distance==4.0]->Action[name==NoOp]->",
						envChanges.toString());
	}

	@Test
	public void testNoPath() {
		MapEnvironment me = new MapEnvironment(aMap);
		LRTAStarAgent agent = new LRTAStarAgent(new BidirectionalMapProblem(me
				.getMap(), "A", "G", hf));
		me.addAgent(agent, "A");
		me.addEnvironmentView(new EnvironmentView() {
			public void notify(String msg) {
				envChanges.append(msg).append("->");
			}

			public void envChanged(Agent agent, Action action, EnvironmentState state) {
				envChanges.append(action).append("->");
			}
		});
		// Note: Will search forever if no path is possible,
		// Therefore restrict the number of steps to something
		// reasonablbe, against which to test.
		me.step(14);

		Assert.assertEquals("Action[name==moveTo, location==B, distance==4.0]->Action[name==moveTo, location==A, distance==4.0]->Action[name==moveTo, location==B, distance==4.0]->Action[name==moveTo, location==C, distance==4.0]->Action[name==moveTo, location==B, distance==4.0]->Action[name==moveTo, location==C, distance==4.0]->Action[name==moveTo, location==D, distance==4.0]->Action[name==moveTo, location==C, distance==4.0]->Action[name==moveTo, location==D, distance==4.0]->Action[name==moveTo, location==E, distance==4.0]->Action[name==moveTo, location==D, distance==4.0]->Action[name==moveTo, location==E, distance==4.0]->Action[name==moveTo, location==F, distance==4.0]->Action[name==moveTo, location==E, distance==4.0]->",
				envChanges.toString());
	}
}
