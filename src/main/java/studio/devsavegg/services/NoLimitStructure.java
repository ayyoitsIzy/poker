package studio.devsavegg.services;

import studio.devsavegg.core.ActionType;
import studio.devsavegg.core.GameContext;
import studio.devsavegg.core.PlayerAction;

public class NoLimitStructure implements IBettingStructure {

    @Override
    public int getMinRaise(GameContext context) {
        return Math.max(context.getMinRaise(), context.getCurrentBet());
    }

    @Override
    public int getMaxRaise(GameContext context) {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean validateAction(PlayerAction action, GameContext context) {
        if (action.getType() == ActionType.FOLD) return true;

        int playerChips = action.getPlayer().getChipStack();
        int amount = action.getAmount();

        if (amount > playerChips) return false;

        if (action.getType() == ActionType.RAISE) {
            int min = getMinRaise(context);
            return amount >= min || amount >= playerChips;
        }
        return true;
    }
}