package shared.response.game;

import shared.response.Response;
import shared.response.ResponseHandler;
import com.fasterxml.jackson.annotation.JsonTypeName;


@JsonTypeName("PauseResponse")
public class PauseResponse implements Response {
    private boolean successful;


    public PauseResponse(boolean successful) {
        this.successful = successful;
    }

    public PauseResponse() {
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    @Override
    public void run(ResponseHandler responseHandler) {
        responseHandler.handlePauseResponse(this);
    }
}
