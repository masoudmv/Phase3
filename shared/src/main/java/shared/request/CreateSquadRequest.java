package shared.request;

import com.fasterxml.jackson.annotation.JsonTypeName;
import shared.response.Response;

@JsonTypeName("CreateSquadRequest")

public class CreateSquadRequest implements Request{
    private String MACAddress;

    public CreateSquadRequest(String MACAddress) {
        this.MACAddress = MACAddress;
    }

    public CreateSquadRequest() {
    }

    public String getMACAddress() {
        return MACAddress;
    }

    public void setMACAddress(String MACAddress) {
        this.MACAddress = MACAddress;
    }

    @Override
    public Response run(RequestHandler requestHandler) {
        return requestHandler.handleCreateSquadRequest(this);
    }
}
