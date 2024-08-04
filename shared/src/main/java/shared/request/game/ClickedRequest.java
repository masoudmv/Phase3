package shared.request.game;

import shared.request.Request;
import shared.request.RequestHandler;
import shared.response.Response;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.awt.*;


@JsonTypeName("ClickedRequest")

public class ClickedRequest implements Request {
    private String macAddress;
    private Point position;

    public ClickedRequest(String macAddress, Point position) {
        this.macAddress = macAddress;
        this.position = position;
    }

    public ClickedRequest(Point position) {
        this.position = position;
    }

    public ClickedRequest() {
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public Point getPosition() {
        return position;
    }
//
    public void setPosition(Point position) {
        this.position = position;
    }


    @Override
    public Response run(RequestHandler requestHandler) {
        return requestHandler.handleClickedRequest(this);
    }
}
