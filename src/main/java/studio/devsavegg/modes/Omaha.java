package studio.devsavegg.modes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import studio.devsavegg.GUI;
import studio.devsavegg.core.Card;
import studio.devsavegg.core.Deck;
import studio.devsavegg.core.GameContext;
import studio.devsavegg.core.Player;
import studio.devsavegg.core.PlayerAction;
import studio.devsavegg.game.GamePhaseConfig;
import studio.devsavegg.game.IGameMode;
import studio.devsavegg.game.TableManager;
import studio.devsavegg.services.IBettingStructure;
import studio.devsavegg.services.IHandEvaluator;
import studio.devsavegg.services.IPotManager;
import studio.devsavegg.services.NoLimitStructure;
import studio.devsavegg.services.StandardHandEvaluator;

public class Omaha implements IGameMode{
    private final GUI gui;
    private final List<Player> player;
    private final int smallBlindAmount;
    private final int bigBlindAmount;
    private final StandardHandEvaluator evaluator;
    private final IBettingStructure bettingStructure;
    private final List<Card> community_card;
    private final Map<Player,PlayerAction> actionlog;

    public Omaha(GUI gui,List<Player> players,Deck deck) {
      this(5, 10,gui,players,deck);
    }

    public Omaha(int smallBlindAmount, int bigBlindAmount,GUI gui,List<Player> players,Deck deck) {
        this.actionlog = new HashMap<>() ;
        this.community_card = new ArrayList<>();
        this.player = players;
        this.gui = gui;
        this.smallBlindAmount = smallBlindAmount;
        this.bigBlindAmount = bigBlindAmount;
        this.evaluator = new StandardHandEvaluator();
        this.bettingStructure = new NoLimitStructure();
        for (int i = 0; i < 5; i++) {
            community_card.add(deck.deal());
        }
    }

    public void run(){
        List<GamePhaseConfig> round = getStructure();
        Player player1 = player.get(0);
        Player player2 = player.get(1);
        gui.setActionlog(actionlog);
        gui.setCommunitycard(community_card);
        gui.setMinBet(bettingStructure.calculateMinRaise(new GameContext()));
        
        try {
            gui.Cover(player1.getName()).get();
            gui.setMode(String.format("%s : %s",getName(),round.get(0).phaseName));
            gui.setGUI(player1, false).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    @Override
    public String getName() {
        return "No-Limit Omaha";
    }

    @Override
    public IHandEvaluator getEvaluator() {
        return evaluator;
    }

    @Override
    public IBettingStructure getBettingStructure() {
        return bettingStructure;
    }

    @Override
    public List<GamePhaseConfig> getStructure() {
        List<GamePhaseConfig> phases = new ArrayList<>();

        // Pre-Flop: 2 Hole Cards, Betting
        phases.add(new GamePhaseConfig("Pre-Flop", 0, 2, true, false, false));

        // Flop: 3 Community Cards, Betting
        phases.add(new GamePhaseConfig("Flop", 3, 0, true, false, false));

        // Turn: 1 Community Card, Betting
        phases.add(new GamePhaseConfig("Turn", 1, 0, true, false, false));

        // River: 1 Community Card, Betting, Showdown
        phases.add(new GamePhaseConfig("River", 1, 0, true, true, false));

        return phases;
    }

    @Override
    public void executeForcedBets(TableManager table, IPotManager potManager, GameContext context) {
        Player sbPlayer = table.getPlayerAt(table.getSmallBlindPos());
        Player bbPlayer = table.getPlayerAt(table.getBigBlindPos());

        // Post Small Blind
        int actualSb = Math.min(sbPlayer.getChipStack(), smallBlindAmount);
        if (actualSb > 0) {
            sbPlayer.bet(actualSb);
            potManager.processBet(sbPlayer, actualSb);
        }

        // Post Big Blind
        int actualBb = Math.min(bbPlayer.getChipStack(), bigBlindAmount);
        if (actualBb > 0) {
            bbPlayer.bet(actualBb);
            potManager.processBet(bbPlayer, actualBb);
        }

        // Setup context for Pre-Flop
        context.setCurrentBet(actualBb);
        context.setMinRaise(bigBlindAmount);
    }
}
