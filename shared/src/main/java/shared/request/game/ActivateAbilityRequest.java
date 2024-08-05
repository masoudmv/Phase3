package shared.request.game;

import shared.request.Request;
import shared.request.RequestHandler;
import shared.response.Response;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("ActivateAbilityRequest")

public class ActivateAbilityRequest implements Request {
    private String macAddress;

    public ActivateAbilityRequest(String macAddress) {
        this.macAddress = macAddress;
    }

    public ActivateAbilityRequest() {
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    @Override
    public Response run(RequestHandler requestHandler) {
        return requestHandler.handleActivateAbilityRequest(this);
    }
}
