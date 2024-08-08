package shared.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "squads")
public class Squad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private Player owner = null;

    @OneToMany(mappedBy = "squad", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Player> members = new ArrayList<>();


    @Column(name = "vault")
    private int vault;

    @Column(name = "in_battle")
    private boolean inBattle = false;

    @Column(name = "battle_xp")
    private int battleXP = 0;

    @Column(name = "monomachia_victories")
    private int monomachiaVictories = 0;

    @Column(name = "palioxis")
    private int palioxis = 0;

    @Column(name = "adonis")
    private int adonis = 0;

    @Column(name = "gefjon")
    private int gefjon = 0;




//    private List<String> macAddresses = new ArrayList<>();
//    private String ownerMacAddress;


    public Squad(Player owner) {
        this.owner = owner;
        this.members.add(owner);
        owner.setSquad(this);
    }

    public Squad() {
    }

    public void addMember(Player player){
        members.add(player);
//        macAddresses.add(player.getMacAddress());
    }

    @JsonManagedReference
    public List<Player> getMembers() {
        return members;
    }


//    public List<String> getMacAddresses() {
//        return macAddresses;
//    }
//
//    public void setMacAddresses(List<String> macAddresses) {
//        this.macAddresses = macAddresses;
//    }
//
//    public String getOwnerMacAddress() {
//        return ownerMacAddress;
//    }
//
//    public void setOwnerMacAddress(String ownerMacAddress) {
//        this.ownerMacAddress = ownerMacAddress;
//    }




    public void setMembers(List<Player> members) {
        this.members = members;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public int getVault() {
        return vault;
    }

    public void addToVault(int vault) {
        this.vault += vault;
    }

//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }

    public int getPalioxis() {
        return palioxis;
    }

    public int getAdonis() {
        return adonis;
    }

    public int getGefjon() {
        return gefjon;
    }


    public boolean hasGefjon(){
        if (gefjon > 0) {
            gefjon --;
            return true;
        } return false;
    }

    public boolean hasPalioxis(){
        if (palioxis > 0) {
            palioxis --;
            return true;
        } return false;
    }

    public void reduceVaultBy(int amount) {
        this.vault -= amount;
    }

    public boolean isInBattle() {
        return inBattle;
    }

    public void setInBattle(boolean inBattle) {
        this.inBattle = inBattle;
    }

    public int getBattleXP() {
        return battleXP;
    }

    public void setBattleXP(int battleXP) {
        this.battleXP = battleXP;
    }

    public void addBattleXP(int battleXP) {
        this.battleXP += battleXP;
    }

    public int getMonomachiaVictories() {
        return monomachiaVictories;
    }

    public void setMonomachiaVictories(int monomachiaVictories) {
        this.monomachiaVictories = monomachiaVictories;
    }

    public void addMonomachiaVictories() {
        this.monomachiaVictories++;
    }

    public boolean gefjonIsActivated(){
        return gefjon > 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    //    public Squad getOpponent() {
//        return opponent;
//    }
//
//    public void setOpponent(Squad opponent) {
//        this.opponent = opponent;
//    }

    public String buySkill(Skill skill) {
        switch (skill) {
            case palioxis -> {
                if (vault < members.size() * 100) return "Squad vault does not have the required XP!";
                reduceVaultBy(members.size() * 100);
                palioxis++;
                return "Palioxis was Purchased successfully!";
            }
            case adonis -> {
                if (vault < 400) return "Squad vault does not have the required XP!";
                reduceVaultBy(400);
                adonis++;
                return "Adonis was Purchased successfully!";
            }
            case gefjon -> {
                if (vault < 300) return "Squad vault does not have the required XP!";
                reduceVaultBy(300);
                gefjon++;
                return "Gefjon was Purchased successfully!";
            }
            default -> throw new IllegalArgumentException("Unknown skill: " + skill);
        }
    }
}
