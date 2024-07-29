package shared.response;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("JoinDemandResponse")
public class JoinDemandResponse implements Response{
    private String macAddress;
    private String username;


    public JoinDemandResponse(String macAddress, String username) {
        this.macAddress = macAddress;
        this.username = username;
    }

    public JoinDemandResponse(String username) {
        this.username = username;
    }

    public JoinDemandResponse() {
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public void run(ResponseHandler responseHandler) {
        responseHandler.handleJoinDemandResponse(this);
    }
}
