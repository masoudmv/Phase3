package shared.request.member;

import shared.model.NotificationType;
import shared.request.Request;
import shared.request.RequestHandler;
import shared.response.Response;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("ReportAskedPleaRequest")
public class ReportAskedPleaRequest implements Request {
    private NotificationType notificationType;
    private String requesterMacAddress;
    private String acceptorMacAddress;
    private boolean accepted;

    public ReportAskedPleaRequest(NotificationType notificationType, String requesterMacAddress, String acceptorMacAddress, boolean accepted) {
        this.notificationType = notificationType;
        this.requesterMacAddress = requesterMacAddress;
        this.acceptorMacAddress = acceptorMacAddress;
        this.accepted = accepted;
    }

    public ReportAskedPleaRequest(String requesterMacAddress, String acceptorMacAddress, boolean accepted) {
        this.requesterMacAddress = requesterMacAddress;
        this.acceptorMacAddress = acceptorMacAddress;
        this.accepted = accepted;
    }


    public ReportAskedPleaRequest() {
    }

    public String getRequesterMacAddress() {
        return requesterMacAddress;
    }

    public void setRequesterMacAddress(String requesterMacAddress) {
        this.requesterMacAddress = requesterMacAddress;
    }

    public String getAcceptorMacAddress() {
        return acceptorMacAddress;
    }

    public void setAcceptorMacAddress(String acceptorMacAddress) {
        this.acceptorMacAddress = acceptorMacAddress;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    @Override
    public Response run(RequestHandler requestHandler) {
        return requestHandler.handleReportAskedPleaReq(this);
    }
}
