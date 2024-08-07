package shared.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import static shared.Model.Status.online;

public class Player extends Model {
    private String macAddress;
    private String username;
    private Status status;
    private int donatedAmount; // make zero after each squad battle or leaving squad.
    private double lastOnlineTime;

    private boolean hasJoinDemandMessage = false;
    private String joinDemandMacAddress;
    private String joinDemandUsername;

    private boolean hasMessage = false;
    private String message;

    private boolean hasNotification = false;
    private Notification notification = null;

    private boolean attendedMonomachia = false;
    private boolean attendedColosseum = false;

    private boolean isInBattle = false;

    // todo change
    private String battleID = "1";

    private int inMenuTime = 0;

    // todo a thread in server side to check connection status
    // todo remove set message, has message ...



    @JsonBackReference
    private Squad squad; // null squad means the player is squadless

    private int XP = 0;

    public Player(String macAddress) {
        this.macAddress = macAddress;
        this.XP = 1000;
        this.status = online;
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

    public double getLastOnlineTime() {
        return lastOnlineTime;
    }

    public void setLastOnlineTime(double lastOnlineTime) {
        this.lastOnlineTime = lastOnlineTime;
    }

    public boolean hasMessage() {
        return hasMessage;
    }

    public void setHasMessage(boolean hasMessage) {
        this.hasMessage = hasMessage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean hasJoinDemandMessage() {
        return hasJoinDemandMessage;
    }

    public void setHasJoinDemandMessage(boolean hasJoinDemandMessage) {
        this.hasJoinDemandMessage = hasJoinDemandMessage;
    }

    public String getJoinDemandMacAddress() {
        return joinDemandMacAddress;
    }

    public void setJoinDemandMacAddress(String joinDemandMacAddress) {
        this.joinDemandMacAddress = joinDemandMacAddress;
    }

    public String getJoinDemandUsername() {
        return joinDemandUsername;
    }

    public void setJoinDemandUsername(String joinDemandUsername) {
        this.joinDemandUsername = joinDemandUsername;
    }


    public void setHasNotification(boolean hasNotification) {
        this.hasNotification = hasNotification;
        this.notification = null;
    }

    public boolean hasNotification() {
        return hasNotification;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.hasNotification = true;
        this.notification = notification;
    }

    public boolean isAttendedMonomachia() {
        return attendedMonomachia;
    }

    public void setAttendedMonomachia(boolean attendedMonomachia) {
        this.attendedMonomachia = attendedMonomachia;
    }

    public boolean isAttendedColosseum() {
        return attendedColosseum;
    }

    public void setAttendedColosseum(boolean attendedColosseum) {
        this.attendedColosseum = attendedColosseum;
    }

    public boolean isInBattle() {
        return isInBattle;
    }

    public void setInBattle(boolean inBattle) {
        isInBattle = inBattle;
    }

    public String getBattleID() {
        return battleID;
    }

    public void setBattleID(String battleID) {
        this.battleID = battleID;
    }

    public int incrementInMenuTime(){
        inMenuTime += 1;
        return inMenuTime;
    }



    public int getInMenuTime() {
        return inMenuTime;
    }

    public void setInMenuTime(int inMenuTime) {
        this.inMenuTime = inMenuTime;
    }

    public void addXP(int XP){
        this.XP += XP;
        squad.addBattleXP(XP);
    }

    public void reduceXP(int XP){
        this.XP -= XP;
        if (XP < 0) XP = 0;
    }
}

