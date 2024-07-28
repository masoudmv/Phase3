package server;

import shared.Model.Player;
import shared.Model.Squad;

import java.util.ArrayList;
import java.util.List;

public class DataBase {

    // TODO implement a real DB ...

    private List<Squad> squads = new ArrayList();

    private List<Player> players = new ArrayList();

    private List<String> getAllMacAddresses(){
        List<String> macAddresses = new ArrayList();
        for (Player player : players){
            macAddresses.add(player.getMacAddress());
        } return macAddresses;
    }

    public Player findPlayer(String macAddress){
        for (Player player : players){
            if (player.getMacAddress().equals(macAddress)){
                return player;
            }
        } return null;
    }

    public void identificate(String macAddress){
        Player player = findPlayer(macAddress);
        if (player == null){
            // create a new player with this macAddress
            players.add(new Player(macAddress));
//            return null;
        }

    }

    public void setUsername(String macAddress, String username){
        Player player = findPlayer(macAddress);
        if (player == null){
            System.out.println(" this type of identificationRequest should be used only if the macAddress is set before");
        } else {
            player.setUsername(username);
        }
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<Squad> getAllSquads() {
        return squads;
    }

    public String createSquad(String macAddress){
        Player player = findPlayer(macAddress);
        if (player == null){
            System.out.println("The player does not exit :/");
            return  "The player does not exit :/";
        } else if (player.getSquad() != null){
            return "You are Already in a squad!";
        } else if (player.getXP() < 100){
            return "You don't have enough XP!";
        } else {
            this.squads.add(new Squad(player));
            player.reduceXpBy(200);
            return "Squad was created successfully!";
        }
    }

    public String donateToSquad(Player player, int amount){
        if (player.getSquad() == null) System.out.println("The player isn't in a squad!");
        int newAmount = player.getDonatedAmount() + amount;
        if (newAmount > 200) return "You can not donate more than 200 XP!";
        player.addDonatedAmount(amount);
        Squad squad = player.getSquad();
        squad.addToVault(amount);
        player.reduceXpBy(amount);
        return "You have successfully donated " + amount + " XP to Squad Vault!";
    }
}
