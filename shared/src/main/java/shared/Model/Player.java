package shared.Model;

public class Player extends Model {
    private String macAddress;
    private String username;
    private Status status;
    private Squad squad; // null squad means the player is squadless
    private int XP = 0;

    public Player(String macAddress) {
        this.macAddress = macAddress;
    }

//    public Player() {
//    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Squad getSquad() {
        return squad;
    }

    public void setSquad(Squad squad) {
        this.squad = squad;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public int getXP() {
        return XP;
    }

    public void setXP(int XP) {
        this.XP = XP;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}

enum Status{
    offline, online, busy;
}
