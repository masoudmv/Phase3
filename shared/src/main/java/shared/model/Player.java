package shared.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import shared.hibernate.HibernateUtil;

import static shared.model.Status.online;


@Entity
@Table(name = "players")
public class Player {

    @Id
    @Column(name = "mac_address", nullable = false, unique = true)
    private String macAddress;

    @Column(name = "username")
    private String username;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "donated_amount")
    private int donatedAmount;

    @Column(name = "last_online_time")
    private double lastOnlineTime;

    @Column(name = "has_join_demand_message")
    private boolean hasJoinDemandMessage = false;

    @Column(name = "join_demand_mac_address")
    private String joinDemandMacAddress;

    @Column(name = "join_demand_username")
    private String joinDemandUsername;

    @Column(name = "has_message")
    private boolean hasMessage = false;

    @Column(name = "message")
    private String message;

    @Column(name = "has_notification")
    private boolean hasNotification = false;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "notification_id")
    private Notification notification = null;

    @Column(name = "attended_monomachia")
    private boolean attendedMonomachia = false;

    @Column(name = "attended_colosseum")
    private boolean attendedColosseum = false;

    @Column(name = "is_in_battle")
    private boolean isInBattle = false;

    @Column(name = "battle_id")
    private String battleID = "1";

    @Column(name = "in_menu_time")
    private int inMenuTime = 0;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "squad_id")
    private Squad squad;

    @Column(name = "xp")
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

//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }

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

