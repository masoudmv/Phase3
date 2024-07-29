package shared.response;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("LeaveSquadResponse")
public class LeaveSquadResponse implements Response{
    private String message;

    public LeaveSquadResponse(String message) {
        this.message = message;
    }

    public LeaveSquadResponse() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public void run(ResponseHandler responseHandler) {
        responseHandler.handleLeaveSquadResponse(this);
    }
}
