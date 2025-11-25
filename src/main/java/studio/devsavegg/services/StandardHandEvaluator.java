package studio.devsavegg.services;

import studio.devsavegg.core.Card;
import studio.devsavegg.core.HandRank;
import studio.devsavegg.core.Rank;
import studio.devsavegg.core.Suit;
import studio.devsavegg.core.HandRank.RankType;

import java.util.*;
import java.util.stream.Collectors;

public class StandardHandEvaluator implements IHandEvaluator {

    @Override
    public HandRank evaluate(List<Card> hole, List<Card> board) {
        List<Card> allCards = new ArrayList<>(hole);
        allCards.addAll(board);

        // Sort descending (Ace to 2)
        allCards.sort(Collections.reverseOrder());

        // Analyze Flush & Straight Flush
        List<Card> flushCards = getFlushCards(allCards);
        if (flushCards != null) {
            // Check if the flush cards specifically form a straight (Straight Flush)
            List<Rank> sfRanks = getStraightRanks(flushCards);
            if (sfRanks != null) {
                if (sfRanks.get(0) == Rank.ACE) {
                    return new HandRank(RankType.ROYAL_FLUSH, Collections.emptyList());
                }
                return new HandRank(RankType.STRAIGHT_FLUSH, sfRanks);
            }
            // Just a regular Flush
            List<Rank> flushKickers = flushCards.stream()
                    .limit(5)
                    .map(Card::getRank)
                    .collect(Collectors.toList());
            return new HandRank(RankType.FLUSH, flushKickers);
        }

        // Group cards by Rank for Quads, Full House, Trips, Pairs
        // Map: Rank -> Count. Ordered by Count (desc), then Rank (desc)
        Map<Rank, Integer> rankCounts = getRankCounts(allCards);

        // Four of a Kind
        if (hasNOfAKind(rankCounts, 4)) {
            Rank quadRank = getRankByCount(rankCounts, 4);
            List<Rank> kickers = getKickers(allCards, 1, Collections.singletonList(quadRank));
            return new HandRank(RankType.FOUR_OF_A_KIND, combine(quadRank, kickers));
        }

        // Full House
        // Possible scenarios: 3-2, 3-3 (take higher trip), 3-2-2
        if (hasNOfAKind(rankCounts, 3) && (rankCounts.size() >= 2)) {
            Iterator<Rank> it = rankCounts.keySet().iterator();
            Rank tripsRank = it.next();
            Rank pairRank = it.next();

            // Ensure the second group actually has at least 2 cards (could be 3-1-1-1-1)
            if (rankCounts.get(pairRank) >= 2) {
                return new HandRank(RankType.FULL_HOUSE, Arrays.asList(tripsRank, pairRank));
            }
        }

        // Straight (Regular)
        List<Rank> straightRanks = getStraightRanks(allCards);
        if (straightRanks != null) {
            return new HandRank(RankType.STRAIGHT, straightRanks);
        }

        // Three of a Kind
        if (hasNOfAKind(rankCounts, 3)) {
            Rank tripRank = getRankByCount(rankCounts, 3);
            List<Rank> kickers = getKickers(allCards, 2, Collections.singletonList(tripRank));
            return new HandRank(RankType.THREE_OF_A_KIND, combine(tripRank, kickers));
        }

        // 8. Two Pair
        if (getCountOfCount(rankCounts, 2) >= 2) {
            Iterator<Rank> it = rankCounts.keySet().iterator();
            Rank pair1 = it.next();
            Rank pair2 = it.next();

            List<Rank> kickers = getKickers(allCards, 1, Arrays.asList(pair1, pair2));

            List<Rank> resultRanks = new ArrayList<>();
            resultRanks.add(pair1);
            resultRanks.add(pair2);
            resultRanks.addAll(kickers);

            return new HandRank(RankType.TWO_PAIR, resultRanks);
        }

        // One Pair
        if (hasNOfAKind(rankCounts, 2)) {
            Rank pairRank = getRankByCount(rankCounts, 2);
            List<Rank> kickers = getKickers(allCards, 3, Collections.singletonList(pairRank));
            return new HandRank(RankType.PAIR, combine(pairRank, kickers));
        }

        // High Card
        return new HandRank(RankType.HIGH_CARD, getKickers(allCards, 5, Collections.emptyList()));
    }

    // --- Helper Methods ---

    /**
     * returns all cards of the suit if a flush exists (>=5 cards), otherwise null.
     */
    private List<Card> getFlushCards(List<Card> allCards) {
        Map<Suit, List<Card>> suitMap = new HashMap<>();
        for (Card c : allCards) {
            suitMap.computeIfAbsent(c.getSuit(), k -> new ArrayList<>()).add(c);
        }

        for (List<Card> suitedCards : suitMap.values()) {
            if (suitedCards.size() >= 5) {
                suitedCards.sort(Collections.reverseOrder());
                return suitedCards;
            }
        }
        return null;
    }

    /**
     * Checks for 5 sequential ranks. Handles Ace-low (Wheel).
     * Input list assumed to be sorted descending.
     * @return List of 5 Ranks in the straight (highest first), or null.
     */
    private List<Rank> getStraightRanks(List<Card> cards) {
        // distinct ranks only
        List<Rank> distinctRanks = cards.stream()
                .map(Card::getRank)
                .distinct()
                .collect(Collectors.toList());

        if (distinctRanks.size() < 5) return null;

        // Check normal straights
        for (int i = 0; i <= distinctRanks.size() - 5; i++) {
            if (isSequence(distinctRanks, i)) {
                return distinctRanks.subList(i, i + 5);
            }
        }

        // Check Wheel (A-5-4-3-2)
        // Must have Ace at index 0 (since sorted desc) and 5-4-3-2 existing
        if (distinctRanks.contains(Rank.ACE) &&
                distinctRanks.contains(Rank.FIVE) &&
                distinctRanks.contains(Rank.FOUR) &&
                distinctRanks.contains(Rank.THREE) &&
                distinctRanks.contains(Rank.TWO)) {
            return Arrays.asList(Rank.FIVE, Rank.FOUR, Rank.THREE, Rank.TWO, Rank.ACE);
        }

        return null;
    }

    private boolean isSequence(List<Rank> ranks, int startIndex) {
        int startVal = ranks.get(startIndex).getValue();
        for (int i = 1; i < 5; i++) {
            if (ranks.get(startIndex + i).getValue() != startVal - i) {
                return false;
            }
        }
        return true;
    }

    /**
     * Groups cards by Rank, sorts groups by Count (Desc) then Rank Value (Desc).
     */
    private Map<Rank, Integer> getRankCounts(List<Card> cards) {
        Map<Rank, Integer> rawCounts = new HashMap<>();
        for (Card c : cards) {
            rawCounts.put(c.getRank(), rawCounts.getOrDefault(c.getRank(), 0) + 1);
        }

        return rawCounts.entrySet().stream()
                .sorted((e1, e2) -> {
                    int compareCount = e2.getValue().compareTo(e1.getValue());
                    if (compareCount != 0) return compareCount;
                    return Integer.compare(e2.getKey().getValue(), e1.getKey().getValue());
                })
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    private boolean hasNOfAKind(Map<Rank, Integer> counts, int n) {
        return counts.values().stream().anyMatch(count -> count == n);
    }

    // Checks how many groups of size N exist
    private long getCountOfCount(Map<Rank, Integer> counts, int n) {
        return counts.values().stream().filter(count -> count == n).count();
    }

    private Rank getRankByCount(Map<Rank, Integer> counts, int n) {
        return counts.entrySet().stream()
                .filter(e -> e.getValue() == n)
                .findFirst()
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    /**
     * Gets 'count' highest cards from 'cards' excluding any ranks in 'exclude'.
     */
    private List<Rank> getKickers(List<Card> cards, int count, List<Rank> exclude) {
        return cards.stream()
                .map(Card::getRank)
                .filter(r -> !exclude.contains(r))
                .distinct()
                .limit(count)
                .collect(Collectors.toList());
    }

    private List<Rank> combine(Rank main, List<Rank> others) {
        List<Rank> combined = new ArrayList<>();
        combined.add(main);
        combined.addAll(others);
        return combined;
    }
}