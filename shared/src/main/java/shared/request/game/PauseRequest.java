package shared.request.game;

import shared.request.Request;
import shared.request.RequestHandler;
import shared.response.Response;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("PauseRequest")
public class PauseRequest implements Request {
    private String macAddress;

    public PauseRequest(String macAddress) {
        this.macAddress = macAddress;
    }

    public PauseRequest() {
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    @Override
    public Response run(RequestHandler requestHandler) {
        return requestHandler.handlePauseRequest(this);
    }
}
