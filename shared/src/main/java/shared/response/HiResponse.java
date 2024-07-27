package shared.response;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("hiResponse")
public class HiResponse implements Response{

    public void run(ResponseHandler responseHandler) {
        responseHandler.handleHiResponse(this);
    }
}
