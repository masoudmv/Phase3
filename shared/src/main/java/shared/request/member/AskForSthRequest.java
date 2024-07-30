package shared.request.member;

import shared.Model.NotificationType;
import shared.request.Request;
import shared.request.RequestHandler;
import shared.response.Response;
import com.fasterxml.jackson.annotation.JsonTypeName;


@JsonTypeName("AskForSthRequest")
public class AskForSthRequest implements Request {
    private NotificationType notificationType;
    private String requesterMacAddress;
    private String receiverMacAddress;

    public AskForSthRequest(NotificationType notificationType, String requesterMacAddress, String receiverMacAddress) {
        this.notificationType = notificationType;
        this.requesterMacAddress = requesterMacAddress;
        this.receiverMacAddress = receiverMacAddress;
    }

    public AskForSthRequest() {
    }

    public String getRequesterMacAddress() {
        return requesterMacAddress;
    }

    public void setRequesterMacAddress(String requesterMacAddress) {
        this.requesterMacAddress = requesterMacAddress;
    }

    public String getReceiverMacAddress() {
        return receiverMacAddress;
    }

    public void setReceiverMacAddress(String receiverMacAddress) {
        this.receiverMacAddress = receiverMacAddress;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    @Override
    public Response run(RequestHandler requestHandler) {
        return requestHandler.handleAskForSthReq(this);
    }
}
