package studio.devsavegg.services;

import studio.devsavegg.core.Card;
import studio.devsavegg.core.HandRank;
import java.util.List;

public interface IHandEvaluator {
    /**
     * Evaluates the strength of a hand given hole cards and community cards.
     * Typically, selects the best 5 cards out of 7.
     */
    HandRank evaluate(List<Card> hole, List<Card> board);
}