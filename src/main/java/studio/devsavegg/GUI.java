package studio.devsavegg;

import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import javax.swing.*;
import javax.swing.border.Border;

import studio.devsavegg.core.ActionType;
import studio.devsavegg.core.Card;
import studio.devsavegg.core.Player;
import studio.devsavegg.core.PlayerAction;

public class GUI extends JFrame implements ActionListener {
    //infra requirement : player info getter(name,holecard,chipstack),card info getter,action type constructor,action type getter(type,amount),
    //and game mode maker to be able to access coummunity card,list of current pot (amount in int),min bet,Map of player and thier chip gain at the end of the game;
    // you may refrence to placeholder class to see what i mean lol
    //inorder to provide gui the amount it needed 

    //info
    Map<Player, PlayerAction> actionLog;
    List<Card> community_card;
    List<Integer> pot;
    int minbet;
    int flipped;
    //ui component
    JFrame UI;
    Color pokerGreen;
    ImageIcon icon;
    URL imageUrl;
    JLabel title;
    JButton bet, raise, call, check, fold,Gamemode1,Gamemode2,Gamemode3,done;
    Font display, titleFont;
    public GUI() {
        flipped = 0;
        imageUrl = GUI.class.getResource("/IMG/icon.png");
        icon = new ImageIcon(imageUrl);
        pokerGreen = new Color(0x35654d);
        UI = new JFrame();
        UI.setSize(1366, 768);
        UI.setResizable(false);
        UI.setDefaultCloseOperation(EXIT_ON_CLOSE);
        UI.setTitle("Poker Night!");
        UI.getContentPane().setBackground(pokerGreen);
        UI.setIconImage(icon.getImage());
        UI.setVisible(true);

        bet = buttonmaker("bet");
        raise = buttonmaker("raise");
        call = buttonmaker("call");
        check = buttonmaker("check");
        fold = buttonmaker("fold");
        Gamemode1 = buttonmaker("Texas hold em");
        Gamemode2 = buttonmaker("Omaha");
        Gamemode3 = buttonmaker("Five card drawn");
        display = new Font("Helvetica", Font.PLAIN, 25);
        titleFont = new Font("Helvetica", Font.PLAIN, 50);
    }

    //=================================GUI SHOW METHOD===============================//

    public CompletableFuture<String> setMainmenu() {
        CompletableFuture<String> result = new CompletableFuture<>();
        UI.getContentPane().removeAll();
        flipped = 0;
        minbet = 0;
        JPanel Center = new JPanel();
        Center.setBackground(pokerGreen);
        Center.setPreferredSize(new Dimension(50, 50));
        Center.setLayout((new BoxLayout(Center, FlowLayout.CENTER)));
        Gamemode1.setPreferredSize(new Dimension(500, 50));
        Gamemode2.setPreferredSize(new Dimension(500, 50));
        Gamemode3.setPreferredSize(new Dimension(500, 50));
        Gamemode1.setAlignmentX(Component.CENTER_ALIGNMENT);
        Gamemode2.setAlignmentX(Component.CENTER_ALIGNMENT);
        Gamemode3.setAlignmentX(Component.CENTER_ALIGNMENT);
        UI.add(Center,BorderLayout.CENTER);
        
        JLabel Logolabel = new JLabel(Iconmaker("/IMG/Logo.png", 3));
        Logolabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        Center.add(Logolabel);
        Center.add(Box.createVerticalStrut(50));
        Center.add(Gamemode1);
        Center.add(Box.createVerticalStrut(15));
        Center.add(Gamemode2);
        Center.add(Box.createVerticalStrut(15));
        Center.add(Gamemode3);
        UI.revalidate();
        UI.repaint();

        Gamemode1.addActionListener(e -> result.complete("1"));
        Gamemode2.addActionListener(e -> result.complete("2"));
        Gamemode3.addActionListener(e -> result.complete("3"));
        
        return result;
    }

    public CompletableFuture<Boolean> setResult(Map<Player,Integer> Distributed){

        JPanel Aside = new JPanel();
        Aside.setBackground(Color.lightGray);
        Aside.setBorder(BorderFactory.createEtchedBorder());
        Aside.setPreferredSize(new Dimension(200, 5));
       

        JPanel Aside2 = new JPanel();
        Aside2.setBackground(Color.lightGray);
        Aside2.setBorder(BorderFactory.createEtchedBorder());
        Aside2.setPreferredSize(new Dimension(200, 5));
       
        

        JPanel Center = new JPanel();
        Center.setBackground(pokerGreen);
        Center.setPreferredSize(new Dimension(500, 500));
        Center.setLayout((new BoxLayout(Center, FlowLayout.CENTER)));


        UI.getContentPane().removeAll();
        done = buttonmaker("done");
        JLabel Logo = new JLabel(Iconmaker("IMG/Result.png",3));
        done.setAlignmentX(Component.CENTER_ALIGNMENT);
        Logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        Center.add(Logo);

        for (Map.Entry<Player,Integer> Player: Distributed.entrySet()){
              JLabel log = new JLabel();
              Font font = new Font("Helvetica", Font.PLAIN, 30);
              log.setFont(font);
              log.setForeground(Color.WHITE);
              log.setText(String.format("%s : %d $", Player.getKey().getName(), Player.getValue()));
              log.setAlignmentX(Component.CENTER_ALIGNMENT);
              Center.add(log);
              Center.add(Box.createVerticalStrut(15));
        }

        Center.add(Box.createVerticalStrut(50));
        Center.add(done);
        CompletableFuture<Boolean> result = new CompletableFuture<>();
        done.addActionListener(e -> result.complete(true));
        UI.add(Center);
        UI.add(Aside,BorderLayout.EAST);
        UI.add(Aside2,BorderLayout.WEST);
        // UI.add(Top,BorderLayout.NORTH);
        // UI.add(Bottom,BorderLayout.SOUTH);
        UI.revalidate();
        UI.repaint();

        return result;
        
    }

    public CompletableFuture<PlayerAction> setGUI(Player player, boolean betted) {
        
        
        CompletableFuture<PlayerAction> result = new CompletableFuture<>();

        // title
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setVerticalAlignment(JLabel.TOP);
        title.setForeground(new Color(0xF9F6EE));
        title.setFont(titleFont);

        // panels
        JPanel Top = new JPanel();
        Top.setBackground(Color.gray);
        Top.setBorder(BorderFactory.createEtchedBorder());
        Top.setPreferredSize(new Dimension(5, 100));

        JPanel Aside = new JPanel();
        Aside.setBackground(Color.lightGray);
        Aside.setBorder(BorderFactory.createEtchedBorder());
        Aside.setPreferredSize(new Dimension(200, 5));
        Aside.setLayout(new BoxLayout(Aside, BoxLayout.Y_AXIS));

        JPanel Aside2 = new JPanel();
        Aside2.setBackground(Color.lightGray);
        Aside2.setBorder(BorderFactory.createEtchedBorder());
        Aside2.setPreferredSize(new Dimension(200, 5));
        Aside2.setLayout(new BoxLayout(Aside2, BoxLayout.Y_AXIS));

        JPanel Center = new JPanel();
        Center.setBackground(pokerGreen);
        Center.setPreferredSize(new Dimension(50, 50));
        Center.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 75));

        JPanel Bottom = new JPanel();
        Bottom.setBackground(Color.gray);
        Bottom.setPreferredSize(new Dimension(50, 300));
        Bottom.setLayout(new BorderLayout());

        JPanel PlayerCardPanel = new JPanel();
        PlayerCardPanel.setBorder(BorderFactory.createEtchedBorder());
        PlayerCardPanel.setBackground(pokerGreen);
        PlayerCardPanel.setPreferredSize(new Dimension(50, 150));
        PlayerCardPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 15));

        JPanel ChooseArea = new JPanel();
        ChooseArea.setBackground(Color.DARK_GRAY);
        ChooseArea.setBorder(BorderFactory.createEtchedBorder());
        ChooseArea.setPreferredSize(new Dimension(50, 150));
        ChooseArea.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 15));

        //add action log
        Font AsideFont = new Font("Helvetica", Font.PLAIN, 20);
        if (!actionLog.isEmpty()) {
        for (Map.Entry<Player,PlayerAction> playerLog: actionLog.entrySet()) {
              JLabel log = new JLabel();
            log.setFont(AsideFont);
            switch (playerLog.getValue().getType()) {
                case BET, RAISE :
                    log.setText(String.format("%s : %s %d $", playerLog.getKey().getName(),playerLog.getValue().getType().name(),playerLog.getValue().getAmount()));
                    break;
                default :
                    log.setText(String.format("%s : %s ",  playerLog.getKey().getName(),playerLog.getValue().getType().name()));
        }
        Aside.add(log); 
    }
            
        }
        


        //add pot info
        if (pot != null){
            for (int i = 0; i < pot.size(); i++) {
            JLabel log = new JLabel();
            log.setFont(AsideFont);
            if(i==0){
                log.setText(String.format("Mainpot : %d $",pot.get(i)));
            }else{
                log.setText(String.format("Sidepot %d : %d $",i,pot.get(i)));
            }
            Aside2.add(log);
        }
        }
        

        // add community card
        if (community_card != null){
            for (int i = 0; i < flipped; i++) {
            Card card = community_card.get(i);
            JLabel cardimg = new JLabel(getcardIcon(card, 6));
            Center.add(cardimg);
        }
        for (int i = 0; i < community_card.size()-flipped; i++) {
            JLabel cardimg = new JLabel(getBackIcon(6));
            Center.add(cardimg);
        }
        }
        



        // player cards
        JLabel yourcard = new JLabel("Your card");
        yourcard.setForeground(new Color(0xF9F6EE));
        yourcard.setFont(titleFont);
        PlayerCardPanel.add(yourcard);
        for (Card card : player.getHoleCards()) {
            JLabel cardimg = new JLabel(getcardIcon(card, 10));
            PlayerCardPanel.add(cardimg, BorderLayout.CENTER);
        }
        JLabel yourchip = new JLabel(String.format("Your Chip : %d $", player.getChipStack()));
        yourchip.setForeground(new Color(0xF9F6EE));
        yourchip.setFont(display);
        JLabel minbet = new JLabel(String.format("Min bet : %d $", this.minbet));
        minbet.setForeground(new Color(0xF9F6EE));
        minbet.setFont(display);
        

        Bottom.add(ChooseArea, BorderLayout.SOUTH);
        Bottom.add(PlayerCardPanel, BorderLayout.NORTH);
        Top.add(title);

        // Add buttons
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(220, 50));
        field.setFont(new Font("Helvetica", Font.PLAIN, 30));
        ChooseArea.add(bet);
        ChooseArea.add(raise);
        ChooseArea.add(call);
        ChooseArea.add(check);
        ChooseArea.add(fold);
        ChooseArea.add(yourchip);
        ChooseArea.add(minbet);
        ChooseArea.add(field);
        raise.setEnabled(true);
        call.setEnabled(true);
        check.setEnabled(true);
        bet.setEnabled(true);
        if (betted) {
            check.setEnabled(false);
            bet.setEnabled(false);
        }else{
            raise.setEnabled(false);
            call.setEnabled(false);
        }

        // Repaint
        UI.getContentPane().removeAll();
        UI.add(Aside2,BorderLayout.EAST);
        UI.add(Top, BorderLayout.NORTH);
        UI.add(Center, BorderLayout.CENTER);
        UI.add(Bottom, BorderLayout.SOUTH);
        UI.add(Aside, BorderLayout.WEST);
        UI.revalidate();
        UI.repaint();

        bet.addActionListener(e -> {
            try {
                int amount = Integer.parseInt(field.getText());
                result.complete(new PlayerAction(player,ActionType.BET, amount));
            } catch (NumberFormatException ex) {
                field.setText("Not a Number!");
            }
        });

        raise.addActionListener(e -> {
            try {
                int amount = Integer.parseInt(field.getText());
                result.complete(new PlayerAction(player,ActionType.RAISE, amount));
            } catch (NumberFormatException ex) {
                field.setText("Not a Number!");
            }
        });
        call.addActionListener(e -> result.complete(new PlayerAction(player,ActionType.CALL,0)));
        check.addActionListener(e -> result.complete(new PlayerAction(player,ActionType.CHECK,0)));
        fold.addActionListener(e -> result.complete(new PlayerAction(player,ActionType.FOLD,0)));

        return result;
    }

    public CompletableFuture<Boolean> Cover(String string){
        UI.getContentPane().removeAll();
        CompletableFuture<Boolean> result = new CompletableFuture<>();
        done = buttonmaker(string);
        done.addActionListener(e -> result.complete(true));
        done.setPreferredSize(new Dimension(500, 50));
        done.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel Center = new JPanel();
        Center.setBackground(pokerGreen);
        Center.setLayout((new BoxLayout(Center, FlowLayout.CENTER)));
        UI.add(Center);
        Center.add(Box.createVerticalGlue());  
        Center.add(done);
        Center.add(Box.createVerticalGlue());
        UI.revalidate();
        UI.repaint();
        return result;
    }

    public CompletableFuture<List<Card>> setOmaha1(Player player){
        UI.getContentPane().removeAll();
        CompletableFuture<List<Card>> result = new CompletableFuture<>();
        List<Card> chosen = new ArrayList<>();
        JPanel Top = new JPanel();
        Top.setBackground(Color.gray);
        Top.setBorder(BorderFactory.createEtchedBorder());
        Top.setPreferredSize(new Dimension(5, 100));

        JPanel Center = new JPanel();
        Center.setBackground(pokerGreen);
        Center.setPreferredSize(new Dimension(50, 50));
        Center.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 75));

        JPanel Bottom = new JPanel();
        Bottom.setBackground(Color.gray);
        Bottom.setPreferredSize(new Dimension(50, 300));
        Bottom.setLayout(new BorderLayout());

        JPanel PlayerCardPanel = new JPanel();
        PlayerCardPanel.setBorder(BorderFactory.createEtchedBorder());
        PlayerCardPanel.setBackground(pokerGreen);
        PlayerCardPanel.setPreferredSize(new Dimension(50, 150));
        PlayerCardPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 15));

        JPanel ChooseArea = new JPanel();
        ChooseArea.setBackground(Color.DARK_GRAY);
        ChooseArea.setBorder(BorderFactory.createEtchedBorder());
        ChooseArea.setPreferredSize(new Dimension(50, 150));
        ChooseArea.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 15));

        //add jlabel
        JLabel title = new JLabel(String.format("%s : Choose community card", player.getName()));
        title.setFont(titleFont);
        title.setForeground(Color.WHITE);
        Top.add(title);

        //add coumunity card
         for (int i = 0; i < 5; i++) {
            Card card = community_card.get(i);
            JLabel cardimg = new JLabel(getcardIcon(card, 6));
            Center.add(cardimg);
        }

        //player card
        List<Card> playerHand = player.getHoleCards();
        for (int i = 0; i < playerHand.size(); i++) {
            Card card = playerHand.get(i);
            JLabel cardimg = new JLabel(getcardIcon(card, 10));
            PlayerCardPanel.add(cardimg);
        }
        //Choose area

        
        
        JButton C1,C2,C3,C4,C5;
        C1 = buttonmaker("1");
        C2 = buttonmaker("2");
        C3 = buttonmaker("3");
        C4 = buttonmaker("4");
        C5 = buttonmaker("5");
        C1.addActionListener(e->{chosen.add(community_card.get(0));
            C1.setEnabled(false);
            if(chosen.size() == 3)
                {result.complete(chosen);};});
        C2.addActionListener(e->{chosen.add(community_card.get(1));
            C2.setEnabled(false);
            if(chosen.size() == 3)
                {result.complete(chosen);};});
        C3.addActionListener(e->{chosen.add(community_card.get(2));
            C3.setEnabled(false);
            if(chosen.size() == 3)
                {result.complete(chosen);};});
        C4.addActionListener(e->{chosen.add(community_card.get(3));
            C4.setEnabled(false);
            if(chosen.size() == 3)
                {result.complete(chosen);};});
        C5.addActionListener(e->{chosen.add(community_card.get(4));
            C5.setEnabled(false);
            if(chosen.size() == 3)
                {result.complete(chosen);};});
        
        ChooseArea.add(C1);
        ChooseArea.add(C2);
        ChooseArea.add(C3);
        ChooseArea.add(C4);
        ChooseArea.add(C5);


        UI.add(Top,BorderLayout.NORTH);
        UI.add(Center,BorderLayout.CENTER);
        UI.add(Bottom,BorderLayout.SOUTH);
        Bottom.add(PlayerCardPanel,BorderLayout.NORTH);
        Bottom.add(ChooseArea,BorderLayout.SOUTH);
        UI.revalidate();
        UI.repaint();
      
            return result;
          
        
    
    }
    
    public CompletableFuture<List<Card>> setOmaha2(Player player){
        UI.getContentPane().removeAll();
        CompletableFuture<List<Card>> result = new CompletableFuture<>();
        List<Card> chosen = new ArrayList<>();
        JPanel Top = new JPanel();
        Top.setBackground(Color.gray);
        Top.setBorder(BorderFactory.createEtchedBorder());
        Top.setPreferredSize(new Dimension(5, 100));

        JPanel Center = new JPanel();
        Center.setBackground(pokerGreen);
        Center.setPreferredSize(new Dimension(50, 50));
        Center.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 75));

        JPanel Bottom = new JPanel();
        Bottom.setBackground(Color.gray);
        Bottom.setPreferredSize(new Dimension(50, 300));
        Bottom.setLayout(new BorderLayout());

        JPanel PlayerCardPanel = new JPanel();
        PlayerCardPanel.setBorder(BorderFactory.createEtchedBorder());
        PlayerCardPanel.setBackground(pokerGreen);
        PlayerCardPanel.setPreferredSize(new Dimension(50, 150));
        PlayerCardPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 15));

        JPanel ChooseArea = new JPanel();
        ChooseArea.setBackground(Color.DARK_GRAY);
        ChooseArea.setBorder(BorderFactory.createEtchedBorder());
        ChooseArea.setPreferredSize(new Dimension(50, 150));
        ChooseArea.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 15));

        //add jlabel
        JLabel title = new JLabel(String.format("%s : Choose Player card", player.getName()));
        title.setFont(titleFont);
        title.setForeground(Color.WHITE);
        Top.add(title);

        //add coumunity card
         for (int i = 0; i < 5; i++) {
            Card card = community_card.get(i);
            JLabel cardimg = new JLabel(getcardIcon(card, 6));
            Center.add(cardimg);
        }

        //player card
        List<Card> playerHand = player.getHoleCards();
        for (int i = 0; i < playerHand.size(); i++) {
            Card card = playerHand.get(i);
            JLabel cardimg = new JLabel(getcardIcon(card, 10));
            PlayerCardPanel.add(cardimg);
        }
        //Choose area

        
        
        JButton C1,C2,C3,C4,C5;
        C1 = buttonmaker("1");
        C2 = buttonmaker("2");
        C3 = buttonmaker("3");
        C4 = buttonmaker("4");
        C5 = buttonmaker("5");
        C1.addActionListener(e->{chosen.add(community_card.get(0));
            C1.setEnabled(false);
            if(chosen.size() == 2)
                {result.complete(chosen);};});
        C2.addActionListener(e->{chosen.add(community_card.get(1));
            C2.setEnabled(false);
            if(chosen.size() == 2)
                {result.complete(chosen);};});
        C3.addActionListener(e->{chosen.add(community_card.get(2));
            C3.setEnabled(false);
            if(chosen.size() == 2)
                {result.complete(chosen);};});
        C4.addActionListener(e->{chosen.add(community_card.get(3));
            C4.setEnabled(false);
            if(chosen.size() == 2)
                {result.complete(chosen);};});
        
        
        ChooseArea.add(C1);
        ChooseArea.add(C2);
        ChooseArea.add(C3);
        ChooseArea.add(C4);
       


        UI.add(Top,BorderLayout.NORTH);
        UI.add(Center,BorderLayout.CENTER);
        UI.add(Bottom,BorderLayout.SOUTH);
        Bottom.add(PlayerCardPanel,BorderLayout.NORTH);
        Bottom.add(ChooseArea,BorderLayout.SOUTH);
        UI.revalidate();
        UI.repaint();
      
            return result;
          
        
    
    }

    public CompletableFuture<Map<String,Integer>> setPlayerInfo(){
        UI.getContentPane().removeAll();
        CompletableFuture<Map<String,Integer>> result = new CompletableFuture<>();
        JLabel Logolabel = new JLabel(Iconmaker("/IMG/Logo.png", 6));
        Logolabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel Top = new JPanel();
        Top.setBackground(pokerGreen);

        JPanel Aside1 = new JPanel();
        Aside1.setBackground(pokerGreen);
        Aside1.setPreferredSize(new Dimension(1366/2, 100));
        Aside1.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 100));
        Aside1.setBorder(BorderFactory.createEtchedBorder());

        JPanel Aside2 = new JPanel();
        Aside2.setBackground(pokerGreen);
        Aside2.setPreferredSize(new Dimension(1366/2, 100));
        Aside2.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 100));
        Aside2.setBorder(BorderFactory.createEtchedBorder());

        JPanel Bottom = new JPanel();
        Bottom.setBackground(Color.lightGray);
        Bottom.setPreferredSize(new Dimension(1366/2, 100));

        JLabel player1 = new JLabel("Enter Player 1 Name :");
        player1.setFont(display);
        player1.setAlignmentX(Component.CENTER_ALIGNMENT);
        player1.setForeground(Color.WHITE);
        JLabel money1 = new JLabel("Enter Player 1 Money :");
        money1.setFont(display);
        money1.setAlignmentX(Component.CENTER_ALIGNMENT);
        money1.setForeground(Color.WHITE);
        JTextField nameField = new JTextField();
        nameField.setPreferredSize(new Dimension(220, 50));
        nameField.setFont(new Font("Helvetica", Font.PLAIN, 30));
        nameField.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField moneyField = new JTextField();
        moneyField.setPreferredSize(new Dimension(220, 50));
        moneyField.setFont(new Font("Helvetica", Font.PLAIN, 30));

        JLabel player2 = new JLabel("Enter Player 2 Name :");
        player2.setFont(display);
        player2.setAlignmentX(Component.CENTER_ALIGNMENT);
        player2.setForeground(Color.WHITE);
        JLabel money2 = new JLabel("Enter Player 2 Money :");
        money2.setFont(display);
        money2.setAlignmentX(Component.CENTER_ALIGNMENT);
        money2.setForeground(Color.WHITE);

        JTextField nameField2 = new JTextField();
        nameField2.setPreferredSize(new Dimension(220, 50));
        nameField2.setFont(new Font("Helvetica", Font.PLAIN, 30));
        nameField2.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField moneyField2 = new JTextField();
        moneyField2.setPreferredSize(new Dimension(220, 50));
        moneyField2.setFont(new Font("Helvetica", Font.PLAIN, 30));
        
        JButton submit = buttonmaker("Submit");
        submit.addActionListener(e -> {
            try {

                Map<String,Integer> answer = new HashMap<>();
                int amount1 = Integer.parseInt(moneyField.getText());
                int amount2 = Integer.parseInt(moneyField2.getText());
                String name1 = nameField.getText();
                String name2 = nameField2.getText();
                answer.put(name1, amount1);
                answer.put(name2, amount2);
                result.complete(answer);
            } catch (NumberFormatException ex) {
                
            }
        });
       

        UI.add(Top,BorderLayout.NORTH);
        UI.add(Aside1,BorderLayout.WEST);
        UI.add(Aside2,BorderLayout.EAST);
        UI.add(Bottom,BorderLayout.SOUTH);

        Aside1.add(player1);
        Aside1.add(nameField);
        Aside1.add(money1);
        Aside1.add(moneyField);

    
        Aside2.add(player2);
        Aside2.add(nameField2);
        Aside2.add(money2);
        Aside2.add(moneyField2);
    

        Bottom.add(submit);
        Top.add(Logolabel);
        UI.revalidate();
        UI.repaint();
        return result;
    }

    

    //===============================================================================//

    //=================================INFO SET/UTILITY===============================//


    public void setMode(String title){
            this.title = new JLabel(title);
    }

    public void setMinBet(int minbet){this.minbet = minbet;}

    public void revealCommunitycard(){
        flipped += 1;
    }   
    
    public void revealCommunitycard(int amount){
        flipped += amount;
    } 
    
    public void setCommunitycard(List<Card> community_card){
        this.community_card = community_card;
    }
    
    public void setPot(List<Integer> Pot){this.pot = Pot;}

    public void resetinfo(){
        minbet = 0;
        flipped = 0;
    }

    public void setActionlog(Map<Player,PlayerAction> actionLog){this.actionLog = actionLog;}

    @Override
    public void actionPerformed(ActionEvent e) {
        //////////////////////////////////////////////
    }
    
    //====================================================================================//

    //HELPER----------------------------------------------------------------------------------------------------
    
    private ImageIcon getcardIcon(Card card, int scale) {
        ImageIcon cardUrl = new ImageIcon(getCardURL(card));
        Image rawimg = cardUrl.getImage();
        Image scaledImage = rawimg.getScaledInstance(rawimg.getWidth(rootPane) / scale,
                rawimg.getHeight(rootPane) / scale, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    private URL getCardURL(Card card) {
        String url = String.format("/IMG/Card/%s/%s.png", card.getSuit().name(), card.getRank().name());
        return GUI.class.getResource(url);
    }
    private URL getBackURL() {
        String url = String.format("/IMG/Card/back.png");
        return GUI.class.getResource(url);
    }
    private ImageIcon getBackIcon(int scale) {
        ImageIcon cardUrl = new ImageIcon(getBackURL());
        Image rawimg = cardUrl.getImage();
        Image scaledImage = rawimg.getScaledInstance(rawimg.getWidth(rootPane) / scale,
                rawimg.getHeight(rootPane) / scale, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    private JButton buttonmaker(String text) {
        JButton button = new JButton(text);
        button.addActionListener(this);
        button.setFont(new Font("Helvetica", Font.PLAIN, 30));
        button.setPreferredSize(new Dimension(220, 50));
        return button;
    }

    private ImageIcon Iconmaker(String URL,int scale){
        URL url = getClass().getResource(URL);
        ImageIcon Logoicon = new ImageIcon(url);
        Image rawLogo = Logoicon.getImage();
        Image scaledImage = rawLogo.getScaledInstance(rawLogo.getWidth(rootPane) / scale,rawLogo.getHeight(rootPane) / scale, Image.SCALE_SMOOTH);
        ImageIcon LogoURL = new ImageIcon(scaledImage);
        return LogoURL;
    }
    

}
