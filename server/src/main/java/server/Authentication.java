package server;

import shared.Model.Player;

import java.util.UUID;

public class Authentication {
    DataBase dataBase;

    public Authentication(DataBase dataBase) {
        this.dataBase = dataBase;
    }

    public Player getUserById(UUID uuid) {
        for (Player player : dataBase.getUsers()) {
            if (player.getId().equals(uuid)) {
                return player;
            }
        }
        return null;
    }
}
