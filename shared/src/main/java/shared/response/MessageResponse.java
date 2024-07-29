package shared.response;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("MessageResponse")
public class MessageResponse implements Response {
    private String message;

    public MessageResponse(String message) {
        this.message = message;
    }

    public MessageResponse() {
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
