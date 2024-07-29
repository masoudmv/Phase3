package shared.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.ArrayList;
import java.util.List;

public class Squad {
    private String name;
    private List<Player> members = new ArrayList<>();
    private Player owner = null;
    private int vault;
    private boolean inBattle = false;

//    @JsonIgnore
//    private Squad opponent = null;

    private int palioxis = 0;
    private int adonis = 0;
    private int gefjon = 0;

    public Squad(Player owner) {
        this.owner = owner;
        this.members.add(owner);
        owner.setSquad(this);
    }

    public Squad() {
    }

    public void addMember(Player player){
        members.add(player);
    }

    @JsonManagedReference
    public List<Player> getMembers() {
        return members;
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPalioxis() {
        return palioxis;
    }

    public int getAdonis() {
        return adonis;
    }

    public int getGefjon() {
        return gefjon;
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
