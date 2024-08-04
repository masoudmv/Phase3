package shared.response.game;

import shared.response.Response;
import shared.response.ResponseHandler;
import com.fasterxml.jackson.annotation.JsonTypeName;


@JsonTypeName("NullResponse")

public class NullResponse implements Response {


    public NullResponse() {
    }

    @Override
    public void run(ResponseHandler responseHandler) {
        responseHandler.handleNullResponse(this);
    }
}
