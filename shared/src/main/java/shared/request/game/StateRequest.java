package shared.request.game;

import shared.request.Request;
import shared.request.RequestHandler;
import shared.response.Response;
import com.fasterxml.jackson.annotation.JsonTypeName;


@JsonTypeName("StateRequest")

public class StateRequest implements Request {
    private String macAddress;
//    private long creationTime;

    public StateRequest(String macAddress) {
        this.macAddress = macAddress;
//        creationTime = System.currentTimeMillis();
    }

//    public StateRequest(long creationTime) {
//        this.creationTime = creationTime;
//    }

    public StateRequest() {
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }


//    public long getCreationTime() {
//        return creationTime;
//    }



    @Override
    public Response run(RequestHandler requestHandler) {
        return requestHandler.handleStateRequest(this);
    }
}
