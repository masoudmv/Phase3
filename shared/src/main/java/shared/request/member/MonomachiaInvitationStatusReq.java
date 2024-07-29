package shared.request.member;

import shared.request.Request;
import shared.request.RequestHandler;
import shared.response.Response;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("MonomachiaInvitationStatusReq")
public class MonomachiaInvitationStatusReq implements Request {
    private String requesterMacAddress;
    private String acceptorMacAddress;
    private boolean accepted;

    public MonomachiaInvitationStatusReq(String requesterMacAddress, String acceptorMacAddress, boolean accepted) {
        this.requesterMacAddress = requesterMacAddress;
        this.acceptorMacAddress = acceptorMacAddress;
        this.accepted = accepted;
    }

    public MonomachiaInvitationStatusReq() {
    }

    public String getRequesterMacAddress() {
        return requesterMacAddress;
    }

    public void setRequesterMacAddress(String requesterMacAddress) {
        this.requesterMacAddress = requesterMacAddress;
    }

    public String getAcceptorMacAddress() {
        return acceptorMacAddress;
    }

    public void setAcceptorMacAddress(String acceptorMacAddress) {
        this.acceptorMacAddress = acceptorMacAddress;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    @Override
    public Response run(RequestHandler requestHandler) {
        return requestHandler.handleMonomachiaInvitationStatusReq(this);
    }
}
