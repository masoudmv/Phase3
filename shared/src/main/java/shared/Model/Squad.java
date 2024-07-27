package shared.Model;

import java.util.concurrent.CopyOnWriteArraySet;

public class Squad {
    private CopyOnWriteArraySet<Player> members = new CopyOnWriteArraySet<>();
    private Player owner = null;
    private int vault;

    public Squad(Player owner) {
        this.owner = owner;
        this.members.add(owner);
    }

    public CopyOnWriteArraySet<Player> getMembers() {
        return members;
    }

    public void setMembers(CopyOnWriteArraySet<Player> members) {
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

    public void setVault(int vault) {
        this.vault = vault;
    }
}
