package game.model.entities;

import game.controller.Game;
import game.model.charactersModel.EpsilonModel;
import server.DataBase;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public enum Ability {
    BANISH, EMPOWER, HEAL,
    DISMAY, SLUMBER, SLAUGHTER;

    public String getName() {
        return switch (this) {
            case BANISH -> "O' Hephaestus, Banish";
            case EMPOWER -> "O' Athena, Empower";
            case HEAL -> "O' Apollo, Heal";
            case DISMAY -> "O' Deimos, Dismay";
            case SLUMBER -> "O' Hypnos, Slumber";
            case SLAUGHTER -> "O' Phonoi, Slaughter";
        };
    }

    public int getCost() {
        return switch (this) {
            case BANISH -> 100;
            case EMPOWER -> 75;
            case HEAL -> 50;
            case DISMAY -> 120;
            case SLUMBER -> 150;
            case SLAUGHTER -> 200;
        };
    }


    public ActionListener getAction(String gameID, String macAddress) {
        return switch (this) {
            case BANISH -> null;
            case EMPOWER -> e -> {
                findGame(gameID).getProfile().empowerInitiationTime = findGame(gameID).ELAPSED_TIME;
                findGame(gameID).getProfile().activatedAbilities.put(macAddress, EMPOWER);
            };
            case HEAL -> e -> findEpsilonModel(gameID, macAddress).health += 10;
            case DISMAY -> e -> {
                findGame(gameID).getProfile().dismayInitiationTime = findGame(gameID).ELAPSED_TIME;
                findGame(gameID).getProfile().activatedAbilities.put(macAddress, DISMAY);
            };
            case SLUMBER -> e -> findGame(gameID).getProfile().slumberInitiationTime = findGame(gameID).ELAPSED_TIME;
            case SLAUGHTER -> e -> findGame(gameID).getProfile().activatedAbilities.put(macAddress, SLAUGHTER);

        };
    }

    public void fire(String gameID, String macAddress) {
        ActionListener action = getAction(gameID, macAddress);
        if (action != null) {
            action.actionPerformed(new ActionEvent(new Object(), ActionEvent.ACTION_PERFORMED, null));
        } else {
            System.out.println("No action defined for this ability");
        }
    }

    private Game findGame(String gameID){
        return DataBase.getDataBase().findGame(gameID);
    }

    private EpsilonModel findEpsilonModel(String gameID, String macAddress) {
        Game game = findGame(gameID);
        for (EpsilonModel epsilon : game.epsilons){
            if (epsilon.getMacAddress().equals(macAddress)) {
                return epsilon;
            }
        }

        return null;
    }
}
