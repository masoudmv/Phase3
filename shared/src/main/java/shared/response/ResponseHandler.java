package shared.response;

import shared.request.DonateRequest;
import shared.request.IdentificationRequest;

public interface ResponseHandler {
    void handleHiResponse(HiResponse hiResponse);
    void handleGetSquadsListResponse(GetSquadsListResponse getSquadsListResponse);
    void handleCreateSquadResponse(CreateSquadResponse createSquadResponse);
    void handleIdentificationResponse(IdentificationResponse identificationResponse);
    void handleDonateResponse(DonateResponse donateResponse);
    void handlePurchaseSkillResponse(PurchaseSkillResponse purchaseSkillResponse);
}
