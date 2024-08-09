package model.entities;

import controller.Game;
import model.charactersModel.EpsilonModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public enum Ability {
    BANISH, EMPOWER, HEAL,
    DISMAY, SLUMBER, SLAUGHTER;


    public static Ability activeAbility;



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


    public ActionListener getAction() {
        return switch (this) {
            case BANISH -> null;
            case EMPOWER -> e -> Profile.getCurrent().empowerInitiationTime = Game.elapsedTime;
            case HEAL -> e -> EpsilonModel.getINSTANCE().health += 10;
            case DISMAY -> e -> Profile.getCurrent().dismayInitiationTime = Game.elapsedTime;
            case SLUMBER -> e -> Profile.getCurrent().slumberInitiationTime = Game.elapsedTime;
            case SLAUGHTER -> e ->Profile.getCurrent().BULLET_DAMAGE = 50;
        };
    }

    public void fire() {
        ActionListener action = getAction();
        if (action != null) {
            action.actionPerformed(new ActionEvent(new Object(), ActionEvent.ACTION_PERFORMED, null));
        } else {
            System.out.println("No action defined for this skill");
        }
    }
}
