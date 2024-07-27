package server;

import shared.Model.Player;
import shared.Model.Squad;

import java.util.ArrayList;
import java.util.List;

public class DataBase {

    // TODO implement a real DB ...

    private List<Squad> squads = new ArrayList();
    private List<Player> players = new ArrayList();

    public List<Player> getUsers() {
        return players;
    }

    public void setUsers(List<Player> players) {
        this.players = players;
    }

    public List<Squad> getSquads() {
        return squads;
    }
}
