package studio.devsavegg.game;

import studio.devsavegg.core.Player;
import java.util.ArrayList;
import java.util.List;

public class TableManager {
    private final List<Player> seats;
    private int buttonPosition;

    public TableManager(List<Player> players) {
        this.seats = new ArrayList<>(players);
        this.buttonPosition = 0;
    }

    public void moveButton() {
        if (seats.isEmpty()) return;
        buttonPosition = (buttonPosition + 1) % seats.size();
    }

    public Player getButtonPlayer() {
        return seats.get(buttonPosition);
    }

    public int getButtonPosition() {
        return buttonPosition;
    }

    public int getSmallBlindPos() {
        // Heads up (2 players) is a special case: Button is SB.
        if (seats.size() == 2) return buttonPosition;
        return (buttonPosition + 1) % seats.size();
    }

    public int getBigBlindPos() {
        if (seats.size() == 2) return (buttonPosition + 1) % seats.size();
        return (buttonPosition + 2) % seats.size();
    }

    public Player getPlayerAt(int pos) {
        return seats.get(pos % seats.size());
    }

    /**
     * Finds the next active player starting AFTER the given position.
     */
    public Player getNextActivePlayer(int currentPos) {
        for (int i = 1; i < seats.size(); i++) {
            Player p = seats.get((currentPos + i) % seats.size());
            if (!p.isFolded() && !p.isAllIn() && !p.isSittingOut()) {
                return p;
            }
        }
        return null; // Should not happen if game is running
    }

    public List<Player> getAllPlayers() {
        return seats;
    }

    public int getPlayerPosition(Player p) {
        return seats.indexOf(p);
    }
}