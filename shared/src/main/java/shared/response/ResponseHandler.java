package shared.response;

import shared.request.IdentificationRequest;

public interface ResponseHandler {
    void handleHiResponse(HiResponse hiResponse);
    void handleGetSquadsListResponse(GetSquadsListResponse getSquadsListResponse);
    void handleCreateSquadResponse(CreateSquadResponse createSquadResponse);
    void handleIdentificationResponse(IdentificationResponse identificationResponse);
}
