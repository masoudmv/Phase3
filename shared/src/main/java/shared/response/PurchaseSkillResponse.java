package shared.response;

import shared.request.Request;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("PurchaseSkillResponse")
public class PurchaseSkillResponse implements Response {
    private String message;

    public PurchaseSkillResponse(String message) {
        this.message = message;
    }

    public PurchaseSkillResponse() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    @Override
    public void run(ResponseHandler responseHandler) {
        responseHandler.handlePurchaseSkillResponse(this);
    }
}
