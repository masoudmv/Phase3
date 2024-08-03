package shared.response.game;

import shared.response.Response;
import shared.response.ResponseHandler;
import com.fasterxml.jackson.annotation.JsonTypeName;


@JsonTypeName("InputResponse")

public class MoveResponse implements Response {


    public MoveResponse() {
    }

    @Override
    public void run(ResponseHandler responseHandler) {
        responseHandler.handleMoveResponse(this);
    }
}
