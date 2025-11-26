package studio.devsavegg;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import studio.devsavegg.core.Deck;
import studio.devsavegg.core.Player;
import studio.devsavegg.modes.Omaha;

public class Main {
    public static void main(String[] args) {
        //System.out.println("Awaiting for GUI implementation...");
        GUI gui = new GUI();
        Deck deck = new Deck();
        List<Player> playerList = new ArrayList<>();
        try {
            Map<String,Integer> nameandmoney = gui.setPlayerInfo().get();
            String mode = gui.setMainmenu().get();
            for (Map.Entry<String, Integer> name : nameandmoney.entrySet()) {
                playerList.add(new Player("0", name.getKey(),name.getValue()));
            }
            playerList = playerList.reversed();
            switch (mode) {
                    case "1":
                        gui.setMode("texas Hold em!");
                        break;
                    case "2":
                        Omaha omaha = new Omaha(gui, playerList,deck);
                        omaha.run();
                        break;
                    case "3":
                        gui.setMode("Five card Drawn!");
                        break;
                    default:
                        throw new AssertionError();
                };
                
        } catch (Exception e) {
        }
        
    }
}