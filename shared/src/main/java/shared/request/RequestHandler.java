package shared.request;

import shared.response.Response;

public interface RequestHandler {
    Response handleHiRequest(HiRequest hiRequest);
    Response handleLoginRequest(LoginRequest loginRequest);
    Response handleGetSquadsRequest(GetSquadsListRequest getSquadsListRequest);
}
