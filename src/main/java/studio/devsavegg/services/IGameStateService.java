package studio.devsavegg.services;

import studio.devsavegg.core.HandHistory;
import studio.devsavegg.core.Player;
import java.util.List;
import java.util.Map;

public interface IGameStateService {
    void savePlayerChips(List<Player> players);
    Map<String, Integer> loadPlayerChips();
    void logHand(HandHistory history);
}