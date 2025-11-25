package studio.devsavegg.services;

import studio.devsavegg.core.Player;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a specific pot that only certain players are eligible to win.
 */
public class SidePot {
    private int amount;
    private final Set<Player> eligiblePlayers;

    public SidePot() {
        this.amount = 0;
        this.eligiblePlayers = new HashSet<>();
    }

    public void addChips(int chips) {
        this.amount += chips;
    }

    public void addEligiblePlayer(Player p) {
        this.eligiblePlayers.add(p);
    }

    public int getAmount() {
        return amount;
    }

    public Set<Player> getEligiblePlayers() {
        return eligiblePlayers;
    }

    public boolean isEligible(Player p) {
        return eligiblePlayers.contains(p);
    }
}