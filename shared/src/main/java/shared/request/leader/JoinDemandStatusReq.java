package shared.request.leader;

import com.fasterxml.jackson.annotation.JsonTypeName;
import shared.request.Request;
import shared.request.RequestHandler;
import shared.response.Response;

@JsonTypeName("JoinDemandStatusReq")
public class JoinDemandStatusReq implements Request {
    private String ownerMacAddress;
    private String demanderMacAddress;
    private boolean accepted;


    public JoinDemandStatusReq(String ownerMacAddress, String demanderMacAddress, boolean accepted) {
        this.ownerMacAddress = ownerMacAddress;
        this.demanderMacAddress = demanderMacAddress;
        this.accepted = accepted;
    }

    public JoinDemandStatusReq() {
    }

    public String getDemanderMacAddress() {
        return demanderMacAddress;
    }

    public void setDemanderMacAddress(String demanderMacAddress) {
        this.demanderMacAddress = demanderMacAddress;
    }

    public String getOwnerMacAddress() {
        return ownerMacAddress;
    }

    public void setOwnerMacAddress(String ownerMacAddress) {
        this.ownerMacAddress = ownerMacAddress;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    @Override
    public Response run(RequestHandler requestHandler) {
        return requestHandler.handleJoinDemandStatusReq(this);
    }
}
