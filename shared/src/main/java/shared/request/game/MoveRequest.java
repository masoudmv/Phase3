package shared.request.game;

import shared.model.Input;
import shared.request.Request;
import shared.request.RequestHandler;
import shared.response.Response;
import com.fasterxml.jackson.annotation.JsonTypeName;


@JsonTypeName("MoveRequest")

public class MoveRequest implements Request {
    private String macAddress;
    private Input input;


    public MoveRequest(String macAddress, Input input) {
        this.macAddress = macAddress;
        this.input = input;
    }

    public MoveRequest() {
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public Input getInput() {
        return input;
    }

    public void setInput(Input input) {
        this.input = input;
    }

    @Override
    public Response run(RequestHandler requestHandler) {
        return requestHandler.handleMoveReq(this);
    }
}
