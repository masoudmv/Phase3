package shared.response;

import com.fasterxml.jackson.annotation.JsonTypeName;
import shared.model.NotificationType;

@JsonTypeName("TransferReqToClientResponse")
public class TransferReqToClientResponse implements Response{
    private NotificationType notificationType;
    private String macAddress;
    private String username;
    private String message;

    public TransferReqToClientResponse(NotificationType notificationType, String message) {
        this.notificationType = notificationType;
        this.message = message;
    }

    public TransferReqToClientResponse(NotificationType notificationType, String macAddress, String username) {
        this.notificationType = notificationType;
        this.macAddress = macAddress;
        this.username = username;
    }

    public TransferReqToClientResponse() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public void run(ResponseHandler responseHandler) {
        responseHandler.handleTransferReqToClientResponse(this);
    }
}
