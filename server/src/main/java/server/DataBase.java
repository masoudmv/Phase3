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

    private Player findPlayer(String macAddress){
        for (Player player : players){
            if (player.getMacAddress().equals(macAddress)){
                return player;
            }
        } return null;
    }

    public String identificate(String macAddress){
        Player player = findPlayer(macAddress);
        if (player == null){
            // create a new player with this macAddress
            players.add(new Player(macAddress));
            return null;
        } else {
            return player.getUsername();
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
}
