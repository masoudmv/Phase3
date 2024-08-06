package shared.request.game;
import com.fasterxml.jackson.annotation.JsonTypeName;
import shared.request.Request;
import shared.request.RequestHandler;
import shared.response.Response;


@JsonTypeName("ActivateSkillRequest")

public class ActivateSkillRequest implements Request {
    private String macAddress;

    public ActivateSkillRequest(String macAddress) {
        this.macAddress = macAddress;
    }

    public ActivateSkillRequest() {
    }

    @Override
    public Response run(RequestHandler requestHandler) {
        return requestHandler.handleActivateSkillRequest(this);
    }
}
