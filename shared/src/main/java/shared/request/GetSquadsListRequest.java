package shared.request;

import com.fasterxml.jackson.annotation.JsonTypeName;
import shared.response.Response;

@JsonTypeName("GetSquadsListRequest")

public class GetSquadsListRequest implements Request{





    @Override
    public Response run(RequestHandler requestHandler) {
        return requestHandler.handleGetSquadsRequest(this);
    }
}
