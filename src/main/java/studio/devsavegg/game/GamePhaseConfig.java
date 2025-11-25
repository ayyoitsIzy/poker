package studio.devsavegg.game;

/**
 * Defines the configuration for a specific phase of the game (e.g., "The Flop").
 */
public class GamePhaseConfig {
    public final String phaseName;
    public final int communityCardsToDeal;
    public final int holeCardsToDeal;
    public final boolean isBettingRound;
    public final boolean triggersShowdown;
    public final boolean isDrawRound;

    public GamePhaseConfig(String phaseName, int communityCardsToDeal, int holeCardsToDeal,
                           boolean isBettingRound, boolean triggersShowdown, boolean isDrawRound) {
        this.phaseName = phaseName;
        this.communityCardsToDeal = communityCardsToDeal;
        this.holeCardsToDeal = holeCardsToDeal;
        this.isBettingRound = isBettingRound;
        this.triggersShowdown = triggersShowdown;
        this.isDrawRound = isDrawRound;
    }
}