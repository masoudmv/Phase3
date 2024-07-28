package shared.request;

import com.fasterxml.jackson.annotation.JsonTypeName;
import shared.response.Response;

@JsonTypeName("DonateRequest")

public class DonateRequest implements Request {
    private String macAddress;
    private int amount;

    public DonateRequest(String macAddress, int amount) {
        this.macAddress = macAddress;
        this.amount = amount;
    }

    public DonateRequest() {
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    @Override
    public Response run(RequestHandler requestHandler) {
        return requestHandler.handleDonateRequest(this);
    }
}
