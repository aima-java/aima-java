package aima.core.logic.propositional.algorithms;

import java.util.List;
import java.util.Set;

import aima.core.logic.propositional.parsing.PLParser;
import aima.core.logic.propositional.parsing.ast.Sentence;
import aima.core.logic.propositional.parsing.ast.PropositionSymbol;
import aima.core.logic.propositional.visitors.SymbolCollector;
import aima.core.util.Converter;
import aima.core.util.SetOps;
import aima.core.util.Util;

/**
 * @author Ravi Mohan
 * @author Mike Stampone
 */
public class TTEntails {

	/**
	 * Returns the answer to the specified question using the TT-Entails
	 * algorithm.
	 * 
	 * @param kb
	 *            a knowledge base to ASK
	 * @param alpha
	 *            a question to ASK the knowledge base
	 * 
	 * @return the answer to the specified question using the TT-Entails
	 *         algorithm.
	 */
	public boolean ttEntails(KnowledgeBase kb, String alpha) {
		Sentence kbSentence = kb.asSentence();
		Sentence querySentence = (Sentence) new PLParser().parse(alpha);
		SymbolCollector collector = new SymbolCollector();
		Set<PropositionSymbol> kbSymbols = collector.getSymbolsIn(kbSentence);
		Set<PropositionSymbol> querySymbols = collector.getSymbolsIn(querySentence);
		Set<PropositionSymbol> symbols = SetOps.union(kbSymbols, querySymbols);
		List<PropositionSymbol> symbolList = new Converter<PropositionSymbol>().setToList(symbols);
		return ttCheckAll(kbSentence, querySentence, symbolList, new Model());
	}

	public boolean ttCheckAll(Sentence kbSentence, Sentence querySentence,
			List<PropositionSymbol> symbols, Model model) {
		if (symbols.isEmpty()) {
			if (model.isTrue(kbSentence)) {
				// System.out.println("#");
				return model.isTrue(querySentence);
			} else {
				// System.out.println("0");
				return true;
			}
		} else {
			PropositionSymbol symbol = Util.first(symbols);
			List<PropositionSymbol> rest = Util.rest(symbols);

			Model trueModel = model.extend(new PropositionSymbol(symbol.getSymbol()), true);
			Model falseModel = model.extend(new PropositionSymbol(symbol.getSymbol()),
					false);
			return (ttCheckAll(kbSentence, querySentence, rest, trueModel) && (ttCheckAll(
					kbSentence, querySentence, rest, falseModel)));
		}
	}
}