package shared.response;


import com.fasterxml.jackson.annotation.JsonTypeName;
import shared.model.Player;
import shared.model.Squad;

@JsonTypeName("CreateSquadResponse")

public class CreateSquadResponse implements Response {
    private Player player;
    private Squad squad;
    private String message;
    private boolean successful;

    public CreateSquadResponse(String message, Player player, Squad squad) {
        this.message = message;
        this.player = player;
        this.squad = squad;
    }

    public CreateSquadResponse(String message, Player player) {
        this.message = message;
        this.player = player;
    }

    public CreateSquadResponse(String message) {
        this.message = message;
    }

    public CreateSquadResponse(boolean successful) {
        this.successful = successful;
    }

    public CreateSquadResponse() {
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

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

    @Override
    public void run(ResponseHandler responseHandler) {
        responseHandler.handleCreateSquadResponse(this);
    }
}
