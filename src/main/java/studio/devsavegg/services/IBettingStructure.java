package studio.devsavegg.services;

import studio.devsavegg.core.GameContext;
import studio.devsavegg.core.PlayerAction;

public interface IBettingStructure {
    int getMinRaise(GameContext context);
    int getMaxRaise(GameContext context);
    boolean validateAction(PlayerAction action, GameContext context);
}