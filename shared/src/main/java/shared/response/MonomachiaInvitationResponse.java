package shared.response;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("MonomachiaInvitationResponse")
public class MonomachiaInvitationResponse implements Response{
    private String macAddress;
    private String username;

    public MonomachiaInvitationResponse(String macAddress, String username) {
        this.macAddress = macAddress;
        this.username = username;
    }

    public MonomachiaInvitationResponse() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    @Override
    public void run(ResponseHandler responseHandler) {
        responseHandler.handleMonomachiaInvitationResponse(this);
    }
}
