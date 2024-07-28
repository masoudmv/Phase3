package shared.request;

import com.fasterxml.jackson.annotation.JsonTypeName;
import shared.response.Response;

@JsonTypeName("IdentificationRequest")

public class IdentificationRequest implements Request{

    private String MACAddress;
    private String username = null;


    public IdentificationRequest(String MACAddress, String username) {
        this.MACAddress = MACAddress;
        this.username = username;

    }

    public IdentificationRequest(String MACAddress) {
        this.MACAddress = MACAddress;
    }


    public IdentificationRequest() {
    }

    public String getMACAddress() {
        return MACAddress;
    }

    public void setMACAddress(String MACAddress) {
        this.MACAddress = MACAddress;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public Response run(RequestHandler requestHandler) {
        return requestHandler.handleIdentificationRequest(this);
    }
}
