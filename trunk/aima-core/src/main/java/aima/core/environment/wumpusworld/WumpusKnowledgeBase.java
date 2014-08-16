package aima.core.environment.wumpusworld;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import aima.core.agent.Action;
import aima.core.agent.impl.DynamicAction;
import aima.core.environment.wumpusworld.action.Forward;
import aima.core.environment.wumpusworld.action.Shoot;
import aima.core.environment.wumpusworld.action.TurnLeft;
import aima.core.environment.wumpusworld.action.TurnRight;
import aima.core.logic.propositional.kb.KnowledgeBase;
import aima.core.logic.propositional.parsing.ast.ComplexSentence;
import aima.core.logic.propositional.parsing.ast.Connective;
import aima.core.logic.propositional.parsing.ast.PropositionSymbol;
import aima.core.logic.propositional.parsing.ast.Sentence;

/**
 * A Knowledge base tailored to the Wumpus World environment.
 * 
 * @author Federico Baron
 * @author Alessandro Daniele
 * @author Ciaran O'Reilly
 */
public class WumpusKnowledgeBase extends KnowledgeBase {

	private int caveXDimension;
	private int caveYDimension;
	//
	private static final String LOCATION          = "L";
	private static final String BREEZE            = "B";
	private static final String STENCH            = "S";
	private static final String PIT               = "P";
	private static final String WUMPUS            = "W";
	private static final String WUMPUS_ALIVE      = "WumpusAlive";
	private static final String HAVE_ARROW        = "HaveArrow";
	private static final String FACING_NORTH      = AgentPosition.Orientation.FACING_NORTH.toString();
	private static final String FACING_SOUTH      = AgentPosition.Orientation.FACING_SOUTH.toString();
	private static final String FACING_EAST       = AgentPosition.Orientation.FACING_EAST.toString();
	private static final String FACING_WEST       = AgentPosition.Orientation.FACING_WEST.toString();
	private static final String PERCEPT_STENCH    = "Stench";
	private static final String PERCEPT_BREEZE    = "Breeze";
	private static final String PERCEPT_GLITTER   = "Glitter";
	private static final String PERCEPT_BUMP      = "Bump";
	private static final String PERCEPT_SCREAM    = "Scream";
	private static final String ACTION_FORWARD    = Forward.FORWARD_ACTION_NAME;
	private static final String ACTION_SHOOT      = Shoot.SHOOT_ACTION_NAME;
	private static final String ACTION_TURN_LEFT  = TurnLeft.TURN_LEFT_ACTION_NAME;
	private static final String ACTION_TURN_RIGHT = TurnRight.TURN_RIGHT_ACTION_NAME;
	private static final String OK_TO_MOVE_INTO   = "OK";

	/**
	 * Create a Knowledge Base that contains the atemporal "wumpus physics" and
	 * temporal rules with time zero.
	 * 
	 * @param caveXandYDimensions
	 *            x and y dimensions of the wumpus world's cave.
	 * */
	public WumpusKnowledgeBase(int caveXandYDimensions) {
		super();

		this.caveXDimension = caveXandYDimensions;
		this.caveYDimension = caveXandYDimensions;

		//
		// 7.7.1 - The current state of the World
		// The agent knows that the starting square contains no pit
		this.tell(new ComplexSentence(Connective.NOT, newSymbol(PIT, 1, 1)));
		// and no wumpus.
		this.tell(new ComplexSentence(Connective.NOT, newSymbol(WUMPUS, 1, 1)));

		// Atemporal rules about breeze and stench
		// For each square, the agent knows that the square is breezy
		// if and only if a neighboring square has a pit; and a square
		// is smelly if and only if a neighboring square has a wumpus.
		for (int y = 1; y <= caveYDimension; y++) {
			for (int x = 1; x <= caveXDimension; x++) {
				
				List<PropositionSymbol> pitsIn  = new ArrayList<PropositionSymbol>();
				List<PropositionSymbol> wumpsIn = new ArrayList<PropositionSymbol>();
				
				if (x > 1) { // West room exists
					pitsIn.add(newSymbol(PIT, x-1, y));
					wumpsIn.add(newSymbol(WUMPUS, x-1, y));
				}
				if (y < caveYDimension) { // North room exists
					pitsIn.add(newSymbol(PIT, x, y+1));
					wumpsIn.add(newSymbol(WUMPUS, x, y+1));
				}
				if (x < caveXDimension) { // East room exists
					pitsIn.add(newSymbol(PIT, x+1, y));
					wumpsIn.add(newSymbol(WUMPUS, x+1, y));
				}
				if (y > 1) { // South room exists
					pitsIn.add(newSymbol(PIT, x, y-1));
					wumpsIn.add(newSymbol(WUMPUS, x, y-1));
				}
			
				tell(new ComplexSentence(newSymbol(BREEZE, x, y), Connective.BICONDITIONAL, Sentence.newDisjunction(pitsIn)));
				tell(new ComplexSentence(newSymbol(STENCH, x, y), Connective.BICONDITIONAL, Sentence.newDisjunction(wumpsIn)));
			}
		}

		// The agent also knows there is exactly one wumpus. This is represented
		// in two parts. First, we have to say that there is at least one wumpus
		List<PropositionSymbol> wumpsAtLeast = new ArrayList<PropositionSymbol>();
		for (int x = 1; x <= caveXDimension; x++) {
			for (int y = 1; y <= caveYDimension; y++) {
				wumpsAtLeast.add(newSymbol(WUMPUS, x, y));
			}
		}
		tell(Sentence.newDisjunction(wumpsAtLeast));

		// Then, we have to say that there is at most one wumpus.
		// For each pair of locations, we add a sentence saying
		// that at least one of them must be wumpus-free.
		int numRooms = (caveXDimension*caveYDimension);
		for (int i = 0; i < numRooms; i++) {
			for (int j = i+1; j < numRooms; j++) {
				tell(new ComplexSentence(Connective.OR,
						new ComplexSentence(Connective.NOT, newSymbol(WUMPUS, (i / caveXDimension)+1, (i % caveYDimension)+1)),
						new ComplexSentence(Connective.NOT, newSymbol(WUMPUS, (j / caveXDimension)+1, (j % caveYDimension)+1))));
			}
		}
	}
	
	public AgentPosition askCurrentPosition(int t) {
		return null; // TODO
	}
	
	public Set<Room> askSafeRooms(int t) {
		return null; // TODO
	}
	
	public boolean askGlitter(int t) {
		return ask(newSymbol(PERCEPT_GLITTER, t));
	}
	
	public Set<Room> askUnvisitedRooms(int t) {
		return null; // TODO
	}
	
	public boolean askHaveArrow(int t) {
		return ask(newSymbol(HAVE_ARROW, t));
	}
	
	public Set<Room> askPossibleWumpusRooms(int t) {
		return null; // TODO
	}
	
	public Set<Room> askNotUnsafeRooms(int t) {
		return null; // TODO
	}
	
	public boolean askOK(int t, int x, int y) {
		return ask(newSymbol(OK_TO_MOVE_INTO, t, x, y));
	}
	
	public boolean ask(Sentence sentence) {
		throw new UnsupportedOperationException("TODO");
	}

	public int getCaveXDimension() {
		return caveXDimension;
	}

	public void setCaveXDimension(int caveXDimension) {
		this.caveXDimension = caveXDimension;
	}

	public int getCaveYDimension() {
		return caveYDimension;
	}

	public void setCaveYDimension(int caveYDimension) {
		this.caveYDimension = caveYDimension;
	}

	/**
	 * Add to KB sentences that describe the action a
	 * 
	 * @param a
	 *            action that must be added to KB
	 * @param time
	 *            current time
	 */
	public void makeActionSentence(Action a, int time) {
		String actionName = ((DynamicAction) a).getName();
		tell(newSymbol(actionName, time));
	}

	/**
	 * Add to KB sentences that describe the perception p
	 * (only about the current time).
	 * 
	 * @param p
	 *            perception that must be added to KB
	 * @param time
	 *            current time
	 */
	public void makePerceptSentence(AgentPercept p, int time) {
		if (p.isStench()) {
			tell(newSymbol(PERCEPT_STENCH, time));
		} else {
			tell(new ComplexSentence(Connective.NOT, newSymbol(PERCEPT_STENCH, time)));
		}

		if (p.isBreeze()) {
			tell(newSymbol(PERCEPT_BREEZE, time));
		} else {
			tell(new ComplexSentence(Connective.NOT, newSymbol(PERCEPT_BREEZE, time)));
		}

		if (p.isGlitter()) {
			tell(newSymbol(PERCEPT_GLITTER, time));
		} else {
			tell(new ComplexSentence(Connective.NOT, newSymbol(PERCEPT_GLITTER, time)));
		}

		if (p.isBump()) {
			tell(newSymbol(PERCEPT_BUMP, time));
		} else {
			tell(new ComplexSentence(Connective.NOT, newSymbol(PERCEPT_BUMP, time)));
		}

		if (p.isScream()) {
			tell(newSymbol(PERCEPT_SCREAM, time));
		} else {
			tell(new ComplexSentence(Connective.NOT, newSymbol(PERCEPT_SCREAM, time)));
		}
	}

	/**
	 * TELL the KB the temporal "physics" sentences for time t
	 * 
	 * @param t
	 *            current time step.
	 */
	public void tellTemporalPhysicsSentences(int t) {		
		if (t == 0) {
			// temporal rules at time zero
			tell(newSymbol(LOCATION, 0, 1, 1));
			tell(newSymbol(FACING_EAST, 0));
			tell(newSymbol(HAVE_ARROW, 0));
			tell(newSymbol(WUMPUS_ALIVE, 0));
		}
		
		// We can connect stench and breeze percepts directly
		// to the properties of the squares where they are experienced
		// through the location fluent as follows. For any time step t
		// and any square [x,y], we assert
		for (int x = 1; x <= caveXDimension; x++) {
			for (int y = 1; y <= caveYDimension; y++) {
				tell(new ComplexSentence( 
						newSymbol(LOCATION, t, x, y), 
						Connective.IMPLICATION,
						new ComplexSentence(newSymbol(PERCEPT_BREEZE, t), Connective.BICONDITIONAL, newSymbol(BREEZE, x, y))));
				
				tell(new ComplexSentence( 
						newSymbol(LOCATION, t, x, y), 
						Connective.IMPLICATION,
						new ComplexSentence(newSymbol(PERCEPT_STENCH, t), Connective.BICONDITIONAL, newSymbol(STENCH, x, y))));
			}
		}
		
		//
		// Successor state axioms (dependent on location)	
		for (int x = 1; x <= caveXDimension; x++) {
			for (int y = 1; y <= caveYDimension; y++) {

				// Location
				List<Sentence> locDisjuncts = new ArrayList<Sentence>();
				locDisjuncts.add(new ComplexSentence(
										newSymbol(LOCATION, t, x, y),
										Connective.AND,
										new ComplexSentence(
												new ComplexSentence(Connective.NOT, newSymbol(ACTION_FORWARD, t)),
												Connective.OR,
												newSymbol(PERCEPT_BUMP, t+1))));
				if (x > 1) { // West room is possible
					locDisjuncts.add(new ComplexSentence(
											newSymbol(LOCATION, t, x-1, y),
											Connective.AND,
											new ComplexSentence(
													newSymbol(FACING_EAST, t),
													Connective.AND,
													newSymbol(ACTION_FORWARD, t))));
				}
				if (y < caveYDimension) { // North room is possible
					locDisjuncts.add(new ComplexSentence(
											newSymbol(LOCATION, t, x, y+1),
											Connective.AND,
											new ComplexSentence(
													newSymbol(FACING_SOUTH, t),
													Connective.AND,
													newSymbol(ACTION_FORWARD, t))));
				}
				if (x < caveXDimension) { // East room is possible	
					locDisjuncts.add(new ComplexSentence(
											newSymbol(LOCATION, t, x+1, y),
											Connective.AND,
											new ComplexSentence(
													newSymbol(FACING_WEST, t),
													Connective.AND,
													newSymbol(ACTION_FORWARD, t))));
				}
				if (y > 1) { // South room is possible
					locDisjuncts.add(new ComplexSentence(
											newSymbol(LOCATION, t, x, y-1),
											Connective.AND,
											new ComplexSentence(
													newSymbol(FACING_NORTH, t),
													Connective.AND,
													newSymbol(ACTION_FORWARD, t))));
				}
				
				tell(new ComplexSentence(
							newSymbol(LOCATION, t+1, x, y),
							Connective.BICONDITIONAL,
							Sentence.newDisjunction(locDisjuncts)));

				// The most important question for the agent is whether
				// a square is OK to move into, that is, the square contains
				// no pit nor live wumpus.
				tell(new ComplexSentence(
							newSymbol(OK_TO_MOVE_INTO, t, x, y),
							Connective.BICONDITIONAL,
							new ComplexSentence(
									new ComplexSentence(Connective.NOT, newSymbol(PIT, x, y)),
									Connective.AND,
									new ComplexSentence(Connective.NOT, 
											new ComplexSentence(
													newSymbol(WUMPUS, x, y),
													Connective.AND,
													newSymbol(WUMPUS_ALIVE, t))))));
			}
		} 
		
		//
		// Successor state axioms (independent of location)

		// Rules about current orientation
		// Facing North
		tell(new ComplexSentence(
					newSymbol(FACING_NORTH, t+1),
					Connective.BICONDITIONAL,
					Sentence.newDisjunction(
							new ComplexSentence(newSymbol(FACING_WEST, t), Connective.AND, newSymbol(ACTION_TURN_RIGHT, t)),
							new ComplexSentence(newSymbol(FACING_EAST, t), Connective.AND, newSymbol(ACTION_TURN_LEFT, t)),
							new ComplexSentence(newSymbol(FACING_NORTH, t),
									Connective.AND,
									new ComplexSentence(Connective.NOT,
											new ComplexSentence(
													newSymbol(ACTION_TURN_LEFT, t),
													Connective.OR,
													newSymbol(ACTION_TURN_RIGHT, t))))
							)));
		// Facing South
		tell(new ComplexSentence(
				newSymbol(FACING_SOUTH, t+1),
				Connective.BICONDITIONAL,
				Sentence.newDisjunction(
						new ComplexSentence(newSymbol(FACING_WEST, t), Connective.AND, newSymbol(ACTION_TURN_LEFT, t)),
						new ComplexSentence(newSymbol(FACING_EAST, t), Connective.AND, newSymbol(ACTION_TURN_RIGHT, t)),
						new ComplexSentence(newSymbol(FACING_SOUTH, t),
								Connective.AND,
								new ComplexSentence(Connective.NOT,
										new ComplexSentence(
												newSymbol(ACTION_TURN_LEFT, t),
												Connective.OR,
												newSymbol(ACTION_TURN_RIGHT, t))))
						)));		
		// Facing East
		tell(new ComplexSentence(
				newSymbol(FACING_EAST, t+1),
				Connective.BICONDITIONAL,
				Sentence.newDisjunction(
						new ComplexSentence(newSymbol(FACING_NORTH, t), Connective.AND, newSymbol(ACTION_TURN_RIGHT, t)),
						new ComplexSentence(newSymbol(FACING_SOUTH, t), Connective.AND, newSymbol(ACTION_TURN_LEFT, t)),
						new ComplexSentence(newSymbol(FACING_EAST, t),
								Connective.AND,
								new ComplexSentence(Connective.NOT,
										new ComplexSentence(
												newSymbol(ACTION_TURN_LEFT, t),
												Connective.OR,
												newSymbol(ACTION_TURN_RIGHT, t))))
						)));			
		// Facing West
		tell(new ComplexSentence(
				newSymbol(FACING_WEST, t+1),
				Connective.BICONDITIONAL,
				Sentence.newDisjunction(
						new ComplexSentence(newSymbol(FACING_NORTH, t), Connective.AND, newSymbol(ACTION_TURN_LEFT, t)),
						new ComplexSentence(newSymbol(FACING_SOUTH, t), Connective.AND, newSymbol(ACTION_TURN_RIGHT, t)),
						new ComplexSentence(newSymbol(FACING_WEST, t),
								Connective.AND,
								new ComplexSentence(Connective.NOT,
										new ComplexSentence(
												newSymbol(ACTION_TURN_LEFT, t),
												Connective.OR,
												newSymbol(ACTION_TURN_RIGHT, t))))
						)));
		
		// Rule about the arrow
		tell(new ComplexSentence(
					newSymbol(HAVE_ARROW, t+1),
					Connective.BICONDITIONAL,
					new ComplexSentence(
							newSymbol(HAVE_ARROW, t), 
							Connective.AND, 
							new ComplexSentence(Connective.NOT, newSymbol(ACTION_SHOOT, t)))));
		
		// Rule about wumpus (dead or alive)
		tell(new ComplexSentence(
				newSymbol(WUMPUS_ALIVE, t+1),
				Connective.BICONDITIONAL,
				new ComplexSentence(
						newSymbol(WUMPUS_ALIVE, t),
						Connective.AND,
						new ComplexSentence(Connective.NOT, newSymbol(PERCEPT_SCREAM, t+1)))));
	}
	
	@Override
	public String toString() {
		List<Sentence> sentences = getSentences(); 
		if (sentences.size() == 0) {
			return "";
		} else {
			boolean first = true;
			StringBuilder sb = new StringBuilder();
			for (Sentence s : sentences) {
				if (!first) {
					sb.append("\n");
				}
				sb.append(s.toString());
				first = false;
			}
			return sb.toString();
		}
	}
	
	//
	// PRIVATE
	//
	private PropositionSymbol newSymbol(String prefix, int timeStep) {
		return new PropositionSymbol(prefix+"_"+timeStep);
	}
	
	private PropositionSymbol newSymbol(String prefix, int x, int y) {
		return new PropositionSymbol(prefix+"_"+x+"_"+y);
	}
	
	private PropositionSymbol newSymbol(String prefix, int timeStep, int x, int y) {
		return newSymbol(newSymbol(prefix, timeStep).toString(), x, y);
	}
}
