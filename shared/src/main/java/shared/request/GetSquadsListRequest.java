package shared.request;

import com.fasterxml.jackson.annotation.JsonTypeName;
import shared.response.Response;

@JsonTypeName("GetSquadsListRequest")

public class GetSquadsListRequest implements Request{
    private String macAddress;

    public GetSquadsListRequest(String macAddress) {
        this.macAddress = macAddress;
    }

    public GetSquadsListRequest() {
    }


    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    @Override
    public Response run(RequestHandler requestHandler) {
        return requestHandler.handleGetSquadsRequest(this);
    }
}
