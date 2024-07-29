package server;

import shared.Model.Player;
import shared.Model.Skill;
import shared.Model.Squad;
import shared.constants.Message;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class DataBase {
    private volatile List<Squad> squads = new CopyOnWriteArrayList<>();
    private volatile List<Player> players = new CopyOnWriteArrayList<>();
    private final List<Pair<Squad, Squad>> squadPairs = new CopyOnWriteArrayList<>();

    public DataBase() {
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
        if (squadPairs.isEmpty() || !squad.isInBattle()) return null;
        for (Pair<Squad, Squad> pair : squadPairs) {
            if (squad.equals(pair.getFirst())){
                return pair.getSecond();
            } else if (squad.equals(pair.getSecond())){
                return pair.getFirst();
            }
        } return null;
    }


    public synchronized void initiateSquadBattle() {
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
}
