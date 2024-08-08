package server;

import game.controller.Game;
import game.controller.GameType;
import game.model.charactersModel.EpsilonModel;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import shared.hibernate.HibernateUtil;
import shared.hibernate.PlayerDAO;
import shared.hibernate.SquadDAO;
import shared.model.*;
import shared.response.MessageResponse;
import shared.response.Response;
import shared.response.TransferReqToClientResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiConsumer;

public class DataBase {
    private static DataBase dataBase;




    private volatile List<Squad> squads = new CopyOnWriteArrayList<>();
    private volatile List<Player> players = new CopyOnWriteArrayList<>();
    private final List<Pair<Squad, Squad>> squadPairs = new CopyOnWriteArrayList<>();
    private final List<Pair<Player, Player>> monomachiaPairs = new CopyOnWriteArrayList<>();
    private final List<Pair<Player, Player>> colosseumPairs = new CopyOnWriteArrayList<>();
    private final List<Match> matches = new CopyOnWriteArrayList<>();
    // todo update playerPairs based on summons ...
    private boolean squadBattleInitiated = false;


    private List<GameData> gameData = new CopyOnWriteArrayList<>();
    private List<Game> games = new CopyOnWriteArrayList<>();

    public DataBase() {
        dataBase = this;
//        Squad squad = new Squad(new Player("Mahmood"));
//        squad.addMember(new Player("Mahmut"));
//        squad.addMember(new Player("Mah"));
//        squad.addMember(new Player("Mahfffafmut"));
//        squad.addMember(new Player("Mahmrheaaaaaaaut"));
//        squads.add(squad);
    }

    public synchronized static Player findPlayer(String macAddress) {
        for (Player player : dataBase.getAllPlayers()) {
            if (player.getMacAddress().equals(macAddress)) {
                return player;
            }
        }
        return null;
    }

    public synchronized void identificate(String macAddress) {

        Player player = findPlayer(macAddress);
        if (player == null) {
            System.out.println("The player was successfully added to the database");

            Player player1 = new Player(macAddress);
            players.add(player1);

            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            Session session = sessionFactory.openSession();
            Transaction t = session.beginTransaction();

            session.save(player1);
            t.commit();


            System.out.println("The player was successfully added to the database");
        }
    }

    public synchronized void setUsername(String macAddress, String username) {
        Player player = findPlayer(macAddress);

        if (player != null) {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            Session session = null;
            Transaction transaction = null;

            try {
                session = sessionFactory.openSession();
                transaction = session.beginTransaction();

                Player p = session.get(Player.class, macAddress);
                if (p != null) {
                    p.setUsername(username);
                    transaction.commit(); // Changes are automatically detected and saved
                    System.out.println("Username updated successfully for player with MAC address " + macAddress);
                } else {
                    System.out.println("Player with MAC address " + macAddress + " not found in the database.");
                }
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                e.printStackTrace();
                System.err.println("Failed to update username for player with MAC address " + macAddress + ": " + e.getMessage());
            } finally {
                if (session != null) {
                    session.close();
                }
            }

            // Update the in-memory player object as well
            player.setUsername(username);
        } else {
            System.out.println("Player with MAC address " + macAddress + " not found in the in-memory database.");
        }
    }



    public List<Player> getAllPlayers() {
        return players;
    }

    public List<Squad> getAllSquads() {
        return squads;
    }


    public List<Pair<Squad, Squad>> getSquadPairs() {
        return squadPairs;
    }

    public boolean isSquadBattleInitiated() {
        return squadBattleInitiated;
    }

    public void setSquadBattleInitiated(boolean squadBattleInitiated) {
        this.squadBattleInitiated = squadBattleInitiated;
    }

    public synchronized String createSquad(String macAddress) {
        Player player = findPlayer(macAddress);

        if (player == null) {
            return "The player does not exist :/";
        } else if (player.getSquad() != null) {
            return "You are already in a squad!";
        } else if (player.getXP() < 100) {
            return "You don't have enough XP!";
        } else {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            Session session = null;
            Transaction transaction = null;

            try {
                session = sessionFactory.openSession();
                transaction = session.beginTransaction();

                // Reload the player from the session to ensure we are working with the latest data
                Player p = session.get(Player.class, macAddress);
                if (p == null) {
                    return "The player does not exist :/";
                } else if (p.getSquad() != null) {
                    return "You are already in a squad!";
                } else if (p.getXP() < 100) {
                    return "You don't have enough XP!";
                }

                // Create the new squad and associate it with the player
                Squad squad = new Squad(p);
                p.setSquad(squad);
                p.reduceXpBy(200);

                // Save the squad and update the player in the database
                session.update(p);
                session.save(squad);
                transaction.commit();

                // Update the in-memory player object as well
                player.setSquad(squad);
                player.reduceXpBy(200);

                return "Squad was created successfully!";
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                e.printStackTrace();
                System.err.println("Failed to create squad for player with MAC address " + macAddress + ": " + e.getMessage());
                return "Failed to create squad due to an internal error.";
            } finally {
                if (session != null) {
                    session.close();
                }
            }
        }
    }


    public synchronized String donateToSquad(Player player, int amount) {
        if (player.getSquad() == null) {
            return "The player isn't in a squad!";
        }
        int newAmount = player.getDonatedAmount() + amount;
        if (newAmount > 200) return "You cannot donate more than 200 XP!";
        player.addDonatedAmount(amount);
        Squad squad = player.getSquad();
        squad.addToVault(amount);
        player.reduceXpBy(amount);
        return "You have successfully donated " + amount + " XP to the Squad Vault!";
    }

    public synchronized String purchaseSkill(Player player, Skill skill) {
        Squad squad = player.getSquad();
        if (squad == null) return "You are not in a squad!";
        String playerMacAddress = player.getMacAddress();
        String ownerMacAddress = squad.getOwner().getMacAddress();
        if (!ownerMacAddress.equals(playerMacAddress)) return "You are not the creator of the squad!";
        return squad.buySkill(skill);
    }

    public synchronized String sendOutFromSquad(Player player) {
        Squad squad = player.getSquad();
        if (squad == null) {
            return "The player isn't in any squad!";
        }

        String playerMacAddress = player.getMacAddress();
        String ownerMacAddress = squad.getOwner().getMacAddress();

        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = null;
        Transaction transaction = null;

        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            if (playerMacAddress.equals(ownerMacAddress)) {
                // The player is the owner, terminate the squad
                for (Player p : squad.getMembers()) {
                    p.setSquad(null);
                    session.update(p);
                }
                squad.setMembers(null);
                session.delete(squad);

                transaction.commit();
                return "The squad was successfully terminated!";
            } else {
                // The player is a member, leave the squad
                player.setSquad(null);
                squad.getMembers().remove(player);

                session.update(player);
                session.update(squad);

                transaction.commit();
                return "You have successfully left the squad!";
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            System.err.println("Failed to update squad for player with MAC address " + playerMacAddress + ": " + e.getMessage());
            return "Failed to update squad due to an internal error.";
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }



    public String joinPlayerToSquad(Player demander, Squad squad, boolean accepted) {
        if (squad == null) return "The squad does not exist anymore!";
        if (demander.getSquad() != null) return "The player has already joined another squad!";
        if (!accepted) {
            demander.setHasMessage(true);
            demander.setMessage(Message.JOIN_REQUEST_UNSUCCESSFUL.getValue());

            // Update the database to reflect the change
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            Session session = null;
            Transaction transaction = null;

            try {
                session = sessionFactory.openSession();
                transaction = session.beginTransaction();

                // Update the player's message status in the database
                session.update(demander);

                transaction.commit();
                return "You have declined the request!";
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                e.printStackTrace();
                System.err.println("Failed to update player message for demander: " + e.getMessage());
                return "Failed to decline request due to an internal error.";
            } finally {
                if (session != null) {
                    session.close();
                }
            }
        }

        // If the request is accepted
        demander.setSquad(squad);
        squad.addMember(demander);

        demander.setHasMessage(true);
        demander.setMessage(Message.JOIN_REQUEST_SUCCESSFUL.getValue());

        // Update the database to reflect the changes
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = null;
        Transaction transaction = null;

        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            // Update the player's squad and message status in the database
            session.update(demander);
            session.update(squad);

            transaction.commit();
            return "The player has successfully joined the squad!";
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            System.err.println("Failed to update squad for demander: " + e.getMessage());
            return "Failed to join squad due to an internal error.";
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }


    public String kickPlayer(Player player) {
        Squad squad = player.getSquad();
        if (squad == null) return "The player is not in any squad!";

        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = null;
        Transaction transaction = null;

        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            // Remove the player from the squad and update the database
            squad.getMembers().remove(player);
            player.setSquad(null);
            player.setHasMessage(true);
            player.setMessage("You have been kicked out of the squad by the leader!");

            session.update(player);
            session.update(squad);

            transaction.commit();
            return "The player was successfully kicked!";
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            System.err.println("Failed to kick player from squad: " + e.getMessage());
            return "Failed to kick player due to an internal error.";
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }


    public Squad findOpponent(Squad squad){
        if (squad == null) return null;
        if (squadPairs.isEmpty() || !squad.isInBattle()) return null;
        for (Pair<Squad, Squad> pair : squadPairs) {
            if (squad.equals(pair.getFirst())){
                return pair.getSecond();
            } else if (squad.equals(pair.getSecond())){
                return pair.getFirst();
            }
        } return null;
    }


    public boolean isAcceptableBattleReq(Player receiver){
        Status status = receiver.getStatus();
        boolean hasAttended = receiver.isAttendedMonomachia();
        boolean isOffline = (status == Status.offline);
        boolean isBusy = (status == Status.busy);
        if (hasAttended || isOffline || isBusy) return false;
        return true;
    }

    public String getMonomachiaMessageForRequester(Player requester, Player receiver){
        System.out.println("handling monomachia ...");
        Status status = receiver.getStatus();
        String receiverUsername = receiver.getUsername();
        if (requester.equals(receiver)) return "You cannot request yourself!";
        else if (!requester.getSquad().isInBattle()) return "Your squad is not in a battle yet!";
        else if (receiver.isAttendedMonomachia()) return receiverUsername + " has already been in a monomachia battle";
        else if (status == Status.offline) return receiverUsername + " is offline right now!";
        else if (status == Status.busy) return receiverUsername + " is Busy right now!";

        else {
            Notification notification = new Notification(NotificationType.MONOMACHIA);
            String requesterMacAddress = requester.getMacAddress();
            String requesterUsername = requester.getUsername();
            notification.setMacAddress(requesterMacAddress);
            notification.setUsername(requesterUsername);
            receiver.setNotification(notification);
            return "Your monomachia challenge has been sent to opponent successfully!";
        }
    }


    public String getColosseumMessageForRequester(Player requester, Player receiver){
        Status status = receiver.getStatus();
        String receiverUsername = receiver.getUsername();
        if (requester.equals(receiver)) return receiverUsername + " you cannot request yourself!";
        else if (!requester.getSquad().isInBattle()) return "Your squad is not in a battle yet!";
        else if (receiver.isAttendedColosseum()) return receiverUsername + " has already been in a colosseum battle";
        else if (status == Status.offline) return receiverUsername + " is offline right now!";
        else if (status == Status.busy) return receiverUsername + " is Busy right now!";
        else {
            Notification notification = new Notification(NotificationType.COLOSSEUM);
            String requesterMacAddress = requester.getMacAddress();
            String requesterUsername = requester.getUsername();
            notification.setMacAddress(requesterMacAddress);
            notification.setUsername(requesterUsername);
            receiver.setNotification(notification);
            return "Your colosseum challenge has been sent to opponent successfully!";
        }
    }

    private String getSummonMessageForRequester(Player requester, Player receiver) {
        System.out.println("got summon request");
        Status status = receiver.getStatus();
        String receiverUsername = receiver.getUsername();
        if (requester.equals(receiver)) return receiverUsername + " you cannot request yourself!";
        else if (!requester.getSquad().isInBattle()) return "Your squad is not in a battle yet!";
        else if (status == Status.offline) return receiverUsername + " is offline right now!";
        else if (status == Status.busy) return receiverUsername + " is Busy right now!";
        else {
            Notification notification = new Notification(NotificationType.SUMMON);
            String requesterMacAddress = requester.getMacAddress();
            String requesterUsername = requester.getUsername();
            notification.setMacAddress(requesterMacAddress);
            notification.setUsername(requesterUsername);
            receiver.setNotification(notification);
            return "Your Summon request has been sent to" + receiverUsername + "successfully!";
        }
    }


    public String sth(Player requester, Player receiver, NotificationType type){
        switch (type) {
            case MONOMACHIA -> {
                return getMonomachiaMessageForRequester(requester, receiver);
            }

            case COLOSSEUM -> {
                return getColosseumMessageForRequester(requester, receiver);
            }

            case SUMMON -> {
                return getSummonMessageForRequester(requester, receiver);
            }

            case JOIN -> System.out.println();
            case SIMPLE_MESSAGE -> System.out.println();
        }
        return "";
    }

    public Response sendNotificationToReceiver(NotificationType type, Player receiver){
        switch (type) {
            case MONOMACHIA, COLOSSEUM, SUMMON -> {
                String macAddress1 = receiver.getNotification().getMacAddress();
                String username1 = receiver.getNotification().getUsername();
                receiver.setHasNotification(false); // should be after the two previous lines!
                System.out.println("Sending message to Requester ...");
                return new TransferReqToClientResponse(type, macAddress1, username1);
            }
//            case SUMMON -> System.out.println();

            case JOIN -> System.out.println();
            case SIMPLE_MESSAGE -> {
                String message1 = receiver.getNotification().getMessage();
                System.out.println("messageeeeeee:   " + message1);
                receiver.setHasNotification(false);
                System.out.println("Setting simple message ...");
                return new MessageResponse(message1);
            }
        }
        return null;
    }

    public String getRequestStatus(NotificationType type,Player requester,  Player receiver, boolean accepted){
        switch (type) {
            case MONOMACHIA -> {
                if (accepted){
                    Notification notification = new Notification(NotificationType.SIMPLE_MESSAGE,
                            "Your Monomachia challenge request was accepted. It will start in 15 seconds.");
                    requester.setNotification(notification);

                    requester.setInBattle(true);
                    receiver.setInBattle(true);

                    return "Monomachia challenge will start in 15 seconds!";
                } else {
                    Notification notification = new Notification(NotificationType.SIMPLE_MESSAGE,
                            "Your Monomachia challenge request was not accepted");
                    requester.setNotification(notification);
                    return "You did not accept the monomachia battle challenge";
                }
            }
            case COLOSSEUM -> {
                if (accepted){
                    Notification notification = new Notification(NotificationType.SIMPLE_MESSAGE,
                            "Your Colosseum challenge request was accepted. It will start in 15 seconds.");
                    requester.setNotification(notification);

                    requester.setInBattle(true);
                    receiver.setInBattle(true);

                    // todo update status

                    colosseumPairs.add(new Pair<>(receiver, requester));

                    return "Colosseum challenge will start in 15 seconds!";
                } else {
                    Notification notification = new Notification(NotificationType.SIMPLE_MESSAGE,
                            "Your Colosseum challenge request was not accepted");
                    requester.setNotification(notification);
                    return "You did not accept the Colosseum battle challenge";
                }
            }
            case SUMMON -> {
                if (accepted){
                    Notification notification = new Notification(NotificationType.SIMPLE_MESSAGE,
                            "Your Summon challenge request was accepted.");
                    requester.setNotification(notification);

                    requester.setInBattle(true);
                    receiver.setInBattle(true);

                    return "You accepted to be someones summon. prepare yourself for monomachia battle!";
                } else {
                    Notification notification = new Notification(NotificationType.SIMPLE_MESSAGE,
                            "Your Summon request was not accepted");
                    requester.setNotification(notification);
                    return "You did not accept the Summon request";
                }
            }

            case JOIN -> System.out.println();
            case SIMPLE_MESSAGE -> {

            }
        }
        return "null";

    }


    public synchronized void initiateSquadBattle() {
        squadBattleInitiated = true;
        // todo dont allow subsequent -i calls ...
        Collections.shuffle(squads);

        for (int i = 0; i < squads.size() - 1; i += 2) {
            Squad squad1 = squads.get(i);
            Squad squad2 = squads.get(i + 1);
            squad1.setInBattle(true);
            squad2.setInBattle(true);
//            squad1.setOpponent(squad2);
//            squad2.setOpponent(squad1);
            squadPairs.add(new Pair<>(squad1, squad2));
        }


        // Handle the case where there is an odd number of squads
        if (squads.size() % 2 != 0) {
            Squad lastSquad = squads.get(squads.size() - 1);
//            lastSquad.setOpponent(null); // or handle appropriately
            lastSquad.setInBattle(false);
        }



        for (Squad squad : squads){
            if (squad.isInBattle()){
                for (Player player : squad.getMembers()){
                    player.setHasMessage(true);
                    player.setMessage(Message.SQUAD_BATTLE_START.getValue());
                }
            }
        }
    }

    public synchronized void terminateSquadBattle() {
        for (Pair<Squad, Squad> pair : squadPairs) {
            Squad first = pair.getFirst();
            Squad second = pair.getSecond();

            boolean firstIsWinner = determineWinner(first, second);

            updatePlayers(first, second, firstIsWinner);
        }
    }

    private boolean determineWinner(Squad first, Squad second) {
        int firstSquadXP = first.getBattleXP();
        int secondSquadXP = second.getBattleXP();

        if (firstSquadXP != secondSquadXP) {
            return firstSquadXP > secondSquadXP;
        }

        int firstVic = first.getMonomachiaVictories();
        int secondVic = second.getMonomachiaVictories();

        if (firstVic != secondVic) {
            return firstVic > secondVic;
        }

        boolean firstG = first.gefjonIsActivated();
        boolean secondG = second.gefjonIsActivated();

        if (firstG != secondG) {
            return firstG;
        }

        return new Random().nextInt(2) == 0;
    }

    private void updatePlayers(Squad first, Squad second, boolean firstIsWinner) {
        Squad winner = firstIsWinner ? first : second;
        Squad loser = firstIsWinner ? second : first;

        int reward = 500;
        if (winner.hasGefjon()) reward *= 2;

        for (Player player : winner.getMembers()) {
            player.addXP(reward);
            player.setNotification(createNotification("You have won the battle!"));
        }

        int punishment = 300;
        if (loser.hasPalioxis()) punishment = 100;
        if (loser.hasGefjon()) punishment *= 2;

        for (Player player : loser.getMembers()) {
            player.reduceXP(punishment);
            player.setNotification(createNotification("You have lost the battle!"));
        }
    }

    private Notification createNotification(String message) {
        Notification notification = new Notification();
        notification.setNotificationType(NotificationType.SIMPLE_MESSAGE);
        notification.setMessage(message);
        return notification;
    }



    public GameData findGameData(String id){
        for (GameData gameData : gameData){
            if (gameData.id.equals(id)) return gameData;
        }
        return null;
    }

    public synchronized static DataBase getDataBase(){
        if (dataBase == null) System.out.println("initialize database first ...");
        return dataBase;
    }

    public List<GameData> getGameData() {
        return gameData;
    }

    public void setGameData(List<GameData> gameData) {
        this.gameData = gameData;
    }

    public void addGame(Game game){
        games.add(game);
    }

    public Game findGame(String id){
        for (Game game : games){
            if (game.getGameID().equals(id)) return game;
        }
        return null;
    }

    public void addMatch(String username, double survivalTime, int gainedXP){
        Match match = new Match(username, survivalTime, gainedXP);
        matches.add(match);
    }


    public static void handleEndGame(GameType gameType, Game game){
        switch (gameType){
            case monomachia -> handleMonomachiaEndGame(game);
            case colosseum -> handleColosseumEndGame(game);
        }
    }

    public static void handleMonomachiaEndGame(Game game) {
        List<EpsilonModel> epsilons = game.epsilons;
        List<EpsilonModel> deadEpsilons = game.deadEpsilons;
        int total = epsilons.size() + deadEpsilons.size();
        int reward = 80 / (total - 1);


        int black = 0;
        int green = 0;

        for (EpsilonModel epsilon : epsilons) {
            if (epsilon.isBlackTeam()) black++;
            else green++;
        }

        boolean blackWins = black > green;

        // Function to add XP to players based on team
        BiConsumer<List<EpsilonModel>, Boolean> addXPToTeam = (epsilonList, isBlackTeam) -> {
            for (EpsilonModel e : epsilonList) {
                if (e.isBlackTeam() == isBlackTeam) {
                    Player p = findPlayer(e.getMacAddress());
                    p.addXP(reward);
                }
            }
        };

        addXPToTeam.accept(epsilons, blackWins);
        addXPToTeam.accept(deadEpsilons, blackWins);
    }


    public static void handleColosseumEndGame(Game game){
        List<EpsilonModel> epsilons = game.epsilons;
        List<EpsilonModel> deadEpsilons = game.deadEpsilons;

        if (epsilons.size() == 2){
            String m1 = epsilons.get(0).getMacAddress();
            Player p1 = findPlayer(m1);
            p1.addXP(30);

            String m2 = epsilons.get(1).getMacAddress();
            Player p2 = findPlayer(m2);
            p2.addXP(30);

        }
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public List<Squad> getSquads() {
        return squads;
    }

    public void setSquads(List<Squad> squads) {
        this.squads = squads;
    }

    //    public static List<Player> getMembers(Squad squad){
//        List<String> macs = squad.getMacAddresses();
//        List<Player> members = new ArrayList<>();
//
//        for (Player player : dataBase.players){
//            String macAddress = player.getMacAddress();
//            if (macs.contains(macAddress)) members.add(player);
//        }
//
//        return members;
//    }
//
//
//    public static Player getOwner(Squad squad){
//        String macAddress = squad.getOwnerMacAddress();
//
//        for (Player player : dataBase.players){
//            String mac = player.getMacAddress();
//            if (macAddress.equals(mac)) return player;
//        }
//
//        return null;
//    }
//
//    public Squad getSquad(Player player){
//        for (Squad squad : dataBase.squads){
//            for (Player p : squad.getMembers()){
//                if (p.equals(player)) return squad;
//            }
//        } return null;
//    }
}
