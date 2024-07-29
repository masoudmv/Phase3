package shared.constants;

public enum UIMessageConstants {
    CONNECTED_TO_SERVER, DIDNT_CONNECT_TO_SERVER,;

    public String getValue() {
        return switch (this) {
            case CONNECTED_TO_SERVER -> "Connected to server successfully!";
            case DIDNT_CONNECT_TO_SERVER -> "Could not connect to server. starting the game in Offline mode!";
        };
    }
}
