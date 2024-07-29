package shared.request.member;

import shared.request.Request;
import shared.request.RequestHandler;
import shared.response.Response;
import com.fasterxml.jackson.annotation.JsonTypeName;


@JsonTypeName("InitMonomachiaReq")
public class InitMonomachiaReq implements Request {
    private String requesterMacAddress;
    private String opponentMacAddress;

    public InitMonomachiaReq(String requesterMacAddress, String opponentMacAddress) {
        this.requesterMacAddress = requesterMacAddress;
        this.opponentMacAddress = opponentMacAddress;
    }

    public InitMonomachiaReq() {
    }

    public String getRequesterMacAddress() {
        return requesterMacAddress;
    }

    public void setRequesterMacAddress(String requesterMacAddress) {
        this.requesterMacAddress = requesterMacAddress;
    }

    public String getOpponentMacAddress() {
        return opponentMacAddress;
    }

    public void setOpponentMacAddress(String opponentMacAddress) {
        this.opponentMacAddress = opponentMacAddress;
    }

    @Override
    public Response run(RequestHandler requestHandler) {
        return requestHandler.handleInitMonomachiaReq(this);
    }
}
