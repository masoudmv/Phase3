package shared.request.nonmember;

import com.fasterxml.jackson.annotation.JsonTypeName;
import shared.request.Request;
import shared.request.RequestHandler;
import shared.response.Response;

@JsonTypeName("JoinSquadReq")

public class JoinSquadReq implements Request {
    private String myMacAddress;
    private String ownerMacAddress;

    public JoinSquadReq(String myMacAddress, String ownerMacAddress) {
        this.myMacAddress = myMacAddress;
        this.ownerMacAddress = ownerMacAddress;
    }

    public JoinSquadReq() {
    }

    public String getMyMacAddress() {
        return myMacAddress;
    }

    public void setMyMacAddress(String myMacAddress) {
        this.myMacAddress = myMacAddress;
    }

    public String getOwnerMacAddress() {
        return ownerMacAddress;
    }

    public void setOwnerMacAddress(String ownerMacAddress) {
        this.ownerMacAddress = ownerMacAddress;
    }

    @Override
    public Response run(RequestHandler requestHandler) {
        return requestHandler.handleJoinSquadReq(this);
    }
}
