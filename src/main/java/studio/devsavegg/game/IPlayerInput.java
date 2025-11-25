package studio.devsavegg.game;

import studio.devsavegg.core.*;

import java.util.List;

/**
 * Interface for abstracting how we get input from players (Console, GUI, Network).
 */
public interface IPlayerInput {
    /**
     * Request an action from a player.
     * @param player The player who needs to act.
     * @param context The current game state (snapshot).
     * @param legalActions A list of valid actions the player can take.
     * @return The action the player chose.
     */
    PlayerAction requestAction(Player player, GameContext context, List<ActionType> legalActions);

    List<Card> requestDiscard(Player player, GameContext context);
}