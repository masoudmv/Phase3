package client.network.toolBox;

public enum SquadConstants {
    SQUAD_CREATION_XP;
    public int getValue() {
        return switch (this) {
            case SQUAD_CREATION_XP -> 100;
        };
    }


}
