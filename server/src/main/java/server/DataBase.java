package server;

import shared.Model.*;
import shared.Model.dummies.DummyModel;
import shared.Model.dummies.DummyPanel;
import shared.response.MessageResponse;
import shared.response.Response;
import shared.response.TransferReqToClientResponse;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class DataBase {
    private static DataBase dataBase;

    private volatile List<Squad> squads = new CopyOnWriteArrayList<>();
    private volatile List<Player> players = new CopyOnWriteArrayList<>();
    private final List<Pair<Squad, Squad>> squadPairs = new CopyOnWriteArrayList<>();
    private final List<Pair<Player, Player>> monomachiaPairs = new CopyOnWriteArrayList<>();
    private final List<Pair<Player, Player>> colosseumPairs = new CopyOnWriteArrayList<>();
    // todo update playerPairs based on summons ...
    private boolean squadBattleInitiated = false;
    private List<String> eliminatedEntities = new CopyOnWriteArrayList<>();


    private Map<String, EntityType> createdEntities = new ConcurrentHashMap<>();
    private Map<String, DummyPanel> createdPanels = new ConcurrentHashMap<>();

    private Map<String, DummyModel> updatedModels = new ConcurrentHashMap<>();
    private Map<String, DummyPanel> updatedPanels = new ConcurrentHashMap<>();

    public DataBase() {
        dataBase = this;
//        Squad squad = new Squad(new Player("Mahmood"));
//        squad.addMember(new Player("Mahmut"));
//        squad.addMember(new Player("Mah"));
//        squad.addMember(new Player("Mahfffafmut"));
//        squad.addMember(new Player("Mahmrheaaaaaaaut"));
//        squads.add(squad);
    }

    public synchronized Player findPlayer(String macAddress) {
        for (Player player : players) {
            if (player.getMacAddress().equals(macAddress)) {
                return player;
            }
        }
        return null;
    }

    public synchronized void identificate(String macAddress) {

        Player player = findPlayer(macAddress);
        if (player == null) {
            players.add(new Player(macAddress));
            System.out.println("Number of players after adding: " + players.size());
        }
    }

    public synchronized void setUsername(String macAddress, String username) {
        Player player = findPlayer(macAddress);
        if (player != null) {
            player.setUsername(username);
        } else {
            System.out.println("Player with MAC address " + macAddress + " not found.");
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
            Squad squad = new Squad(player);
            squads.add(squad);
            player.reduceXpBy(200);
            return "Squad was created successfully!";
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

    public synchronized String sendOutFromSquad(Player player) { // todo use a better name
        Squad squad = player.getSquad();
        if (squad == null) return "The player isn't in any squad!";
        String playerMacAddress = player.getMacAddress();
        String ownerMacAddress = squad.getOwner().getMacAddress();
        if (playerMacAddress.equals(ownerMacAddress)) {
            for (Player p : squad.getMembers()) {
                p.setSquad(null);
            }
            squad.setMembers(null);
            squads.remove(squad);
            return "The squad was successfully terminated!";
        } else {
            player.setSquad(null);
            squad.getMembers().remove(player);
            return "You have successfully left the squad!";
        }
    }


    public String joinPlayerToSquad(Player demander, Squad squad, boolean accepted) {
        if (squad == null) return "the Squad does not exist anymore!";
        if (demander.getSquad() != null) return "the Player has already joined another squad!";
        if (!accepted) {
            demander.setHasMessage(true);
            demander.setMessage(Message.JOIN_REQUEST_UNSUCCESSFUL.getValue());
            return "You have declined the request!";
        }
        demander.setSquad(squad);
        squad.addMember(demander);

        demander.setHasMessage(true);
        demander.setMessage(Message.JOIN_REQUEST_SUCCESSFUL.getValue());
        return "The Player has successfully joined the squad!";
    }

    public String kickPlayer(Player player){
        Squad squad = player.getSquad();
        if (squad == null) return "the player is not in any squad!";
        squad.getMembers().remove(player);
        player.setSquad(null);
        player.setHasMessage(true);
        player.setMessage("You have been kicked out of the squad by leader!");
        return "the player was successfully kicked!";
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
            System.out.println(squad1.getName() + " vs " + squad2.getName());
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

    public synchronized void createEntity(String id, EntityType entityType){
        createdEntities.put(id, entityType);
    }

    public synchronized void createPanel(String id, Point2D location, Dimension dimension){
        int x = (int) location.getX();
        int y = (int) location.getY();
        DummyPanel panel = new DummyPanel(id, new Point(x, y), dimension);
        createdPanels.put(id, panel);
    }

    public synchronized void eliminateEntity(String id){
        eliminatedEntities.add(id);
    }


    public synchronized void clearModels(){
        updatedModels.clear();
    }

    public synchronized void addUpdatedModels(DummyModel model){
        updatedModels.put(model.getId(), model);
    }

    public synchronized void clearPanels(){
        updatedPanels.clear();
    }

    public synchronized void addUpdatedPanels(DummyPanel panel){
        updatedPanels.put(panel.getId(), panel);
    }

    public synchronized Map<String, DummyPanel> getCreatedPanels() {
        return createdPanels;
    }

    public synchronized Map<String, EntityType> getCreatedEntities() {
        return createdEntities;
    }

    public synchronized Map<String, DummyPanel> getUpdatedPanels() {
        return updatedPanels;
    }

    public synchronized Map<String, DummyModel> getUpdatedModels() {
        return updatedModels;
    }

    public synchronized List<String> getEliminatedEntities() {
        return eliminatedEntities;
    }



    public synchronized static DataBase getDataBase(){
        if (dataBase == null) System.out.println("initialize database first ...");
        return dataBase;
    }
}
