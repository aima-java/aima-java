package aima.core.logic.propositional.algorithms;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import aima.core.logic.propositional.parsing.PLParser;
import aima.core.logic.propositional.parsing.ast.Sentence;
import aima.core.logic.propositional.parsing.ast.PropositionSymbol;
import aima.core.logic.propositional.visitors.CNFClauseGatherer;
import aima.core.logic.propositional.visitors.CNFTransformer;
import aima.core.logic.propositional.visitors.SymbolCollector;
import aima.core.util.Converter;
import aima.core.util.Util;

/**
 * @author Ravi Mohan
 * @author Mike Stampone
 */
public class WalkSAT {
	private Model myModel;

	private Random random = new Random();

	/**
	 * Returns a satisfying model or failure (null).
	 * 
	 * @param logicalSentence
	 *            a set of clauses in propositional logic
	 * @param numberOfFlips
	 *            number of flips allowed before giving up
	 * @param probabilityOfRandomWalk
	 *            the probability of choosing to do a "random walk" move,
	 *            typically around 0.5
	 * 
	 * @return a satisfying model or failure (null).
	 */
	public Model findModelFor(String logicalSentence, int numberOfFlips,
			double probabilityOfRandomWalk) {
		myModel = new Model();
		Sentence s = (Sentence) new PLParser().parse(logicalSentence);
		CNFTransformer transformer = new CNFTransformer();
		CNFClauseGatherer clauseGatherer = new CNFClauseGatherer();
		SymbolCollector sc = new SymbolCollector();

		List<PropositionSymbol> symbols = new Converter<PropositionSymbol>().setToList(sc
				.getSymbolsIn(s));
		for (int i = 0; i < symbols.size(); i++) {
			PropositionSymbol sym = (PropositionSymbol) symbols.get(i);
			myModel = myModel.extend(sym, Util.randomBoolean());
		}
		List<Sentence> clauses = new Converter<Sentence>()
				.setToList(clauseGatherer.getClausesFrom(transformer
						.transform(s)));

		for (int i = 0; i < numberOfFlips; i++) {
			if (getNumberOfClausesSatisfiedIn(
					new Converter<Sentence>().listToSet(clauses), myModel) == clauses
					.size()) {
				return myModel;
			}
			Sentence clause = clauses.get(random.nextInt(clauses.size()));

			List<PropositionSymbol> symbolsInClause = new Converter<PropositionSymbol>().setToList(sc
					.getSymbolsIn(clause));
			if (random.nextDouble() >= probabilityOfRandomWalk) {
				PropositionSymbol randomSymbol = symbolsInClause.get(random
						.nextInt(symbolsInClause.size()));
				myModel = myModel.flip(randomSymbol);
			} else {
				PropositionSymbol symbolToFlip = getSymbolWhoseFlipMaximisesSatisfiedClauses(
						new Converter<Sentence>().listToSet(clauses),
						symbolsInClause, myModel);
				myModel = myModel.flip(symbolToFlip);
			}

		}
		return null;
	}

	private PropositionSymbol getSymbolWhoseFlipMaximisesSatisfiedClauses(
			Set<Sentence> clauses, List<PropositionSymbol> symbols, Model model) {
		if (symbols.size() > 0) {
			PropositionSymbol retVal = symbols.get(0);
			int maxClausesSatisfied = 0;
			for (int i = 0; i < symbols.size(); i++) {
				PropositionSymbol sym = symbols.get(i);
				if (getNumberOfClausesSatisfiedIn(clauses, model.flip(sym)) > maxClausesSatisfied) {
					retVal = sym;
					maxClausesSatisfied = getNumberOfClausesSatisfiedIn(
							clauses, model.flip(sym));
				}
			}
			return retVal;
		} else {
			return null;
		}

	}

	private int getNumberOfClausesSatisfiedIn(Set<Sentence> clauses, Model model) {
		int retVal = 0;
		Iterator<Sentence> i = clauses.iterator();
		while (i.hasNext()) {
			Sentence s = i.next();
			if (model.isTrue(s)) {
				retVal += 1;
			}
		}
		return retVal;
	}
}
