package shared.constants;

public enum Message {
    KICKED, SQUAD_BATTLE_START, SQUAD_BATTLE_END, JOIN_REQUEST, JOIN_REQUEST_SUCCESSFUL, JOIN_REQUEST_UNSUCCESSFUL;

    public String getValue() {
        return switch (this) {
            case KICKED -> "You have been kicked out from you squad!";
            // Add cases for other enum values as needed

            case SQUAD_BATTLE_START -> "Squad battle has been initiated!";
            case SQUAD_BATTLE_END -> "Squad battle has terminated!";
            case JOIN_REQUEST -> "SomeOne wants to join you squad ..."; // todo add username to message somehow ...
            case JOIN_REQUEST_SUCCESSFUL -> "The leader of squad has accepted your join request!";
            case JOIN_REQUEST_UNSUCCESSFUL -> "The leader of squad has rejected your join request!";
        };
    }
}
