package shared.request;

import com.fasterxml.jackson.annotation.JsonTypeName;
import shared.response.Response;

@JsonTypeName("LeaveSquadReq")
public class LeaveSquadReq implements Request{
    private String macAddress;

    public LeaveSquadReq(String macAddress) {
        this.macAddress = macAddress;
    }

    public LeaveSquadReq() {
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    @Override
    public Response run(RequestHandler requestHandler) {
        return requestHandler.handleLeaveSquadReq(this);
    }
}
