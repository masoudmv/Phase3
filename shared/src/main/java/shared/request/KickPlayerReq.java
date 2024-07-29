package shared.request;

import shared.response.Response;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("KickPlayerReq")
public class KickPlayerReq implements Request{
    private String myMacAddress;
    private String playerMacAddress;

    public KickPlayerReq(String myMacAddress, String playerMacAddress) {
        this.myMacAddress = myMacAddress;
        this.playerMacAddress = playerMacAddress;
    }


    public KickPlayerReq() {
    }

    public String getMyMacAddress() {
        return myMacAddress;
    }

    public void setMyMacAddress(String myMacAddress) {
        this.myMacAddress = myMacAddress;
    }

    public String getPlayerMacAddress() {
        return playerMacAddress;
    }

    public void setPlayerMacAddress(String playerMacAddress) {
        this.playerMacAddress = playerMacAddress;
    }

    @Override
    public Response run(RequestHandler requestHandler) {
        return null;
    }
}
