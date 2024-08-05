package client.network.game.view.junks;


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
}
