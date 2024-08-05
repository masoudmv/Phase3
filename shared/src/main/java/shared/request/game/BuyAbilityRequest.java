package shared.request.game;

import shared.request.Request;
import shared.request.RequestHandler;
import shared.response.Response;
import com.fasterxml.jackson.annotation.JsonTypeName;


@JsonTypeName("BuyAbilityRequest")
public class BuyAbilityRequest implements Request {
    private String macAddress;
    private String abilityName;

    public BuyAbilityRequest(String macAddress, String abilityName) {
        this.macAddress = macAddress;
        this.abilityName = abilityName;
    }

    public BuyAbilityRequest() {
    }


    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getAbilityName() {
        return abilityName;
    }

    public void setAbilityName(String abilityName) {
        this.abilityName = abilityName;
    }

    @Override
    public Response run(RequestHandler requestHandler) {
        return requestHandler.handleBuyAbilityRequest(this);
    }
}
