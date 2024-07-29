package shared.response;


import com.fasterxml.jackson.annotation.JsonTypeName;
import shared.Model.Player;
import shared.Model.Squad;

@JsonTypeName("IdentificationResponse")
public class IdentificationResponse implements Response{
    private Player player;
    private Squad squad;
    private Squad opponent;
//    private String username;


    public IdentificationResponse(Player player, Squad squad, Squad opponent) {
        this.player = player;
        this.squad = squad;
        this.opponent = opponent;
    }

    public IdentificationResponse(Player player, Squad squad) {
        this.player = player;
        this.squad = squad;
        this.opponent = null;
    }

    public IdentificationResponse(Player player) {
        this.player = player;
    }

//    public IdentificationResponse(String username) {
//        this.username = username;
//    }

    public IdentificationResponse() {
    }

//    public String getUsername() {
//        return username;
//    }

//    public void setUsername(String username) {
//        this.username = username;
//    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Squad getSquad() {
        return squad;
    }

    public void setSquad(Squad squad) {
        this.squad = squad;
    }

    public Squad getOpponent() {
        return opponent;
    }

    public void setOpponent(Squad opponent) {
        this.opponent = opponent;
    }

    @Override
    public void run(ResponseHandler responseHandler) {
        responseHandler.handleIdentificationResponse(this);
    }
}
