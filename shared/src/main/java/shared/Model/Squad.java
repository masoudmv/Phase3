package shared.Model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.ArrayList;
import java.util.List;

public class Squad {
    private String name;
    private List<Player> members = new ArrayList<>();
    private Player owner = null;
    private int vault;

    public Squad(Player owner) {
        this.owner = owner;
        this.members.add(owner);
        owner.setSquad(this);
    }

    public Squad() {
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
}
