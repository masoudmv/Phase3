package shared.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

public class Player extends Model {
    private String macAddress;
    private String username;
    private Status status;
    private int donatedAmount; // make zero after each squad battle or leaving squad.

    @JsonBackReference
    private Squad squad; // null squad means the player is squadless

    private int XP = 0;

    public Player(String macAddress) {
        this.macAddress = macAddress;
        this.XP = 1000;
    }

    public Player() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JsonBackReference
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

    public int getDonatedAmount() {
        return donatedAmount;
    }

    public void addDonatedAmount(int donatedAmount) {
        this.donatedAmount += donatedAmount;
    }

    public void reduceXpBy(int amount) {
        if (amount > this.XP) return;
        this.XP -= amount;
    }
}

enum Status {
    offline, online, busy;
}
