package shared.response;

import shared.response.game.MoveResponse;
import shared.response.game.StateResponse;

public interface ResponseHandler {
    void handleHiResponse(HiResponse hiResponse);
    void handleGetSquadsListResponse(GetSquadsListResponse getSquadsListResponse);
    void handleCreateSquadResponse(CreateSquadResponse createSquadResponse);
    void handleIdentificationResponse(IdentificationResponse identificationResponse);
    void handleDonateResponse(DonateResponse donateResponse);
    void handlePurchaseSkillResponse(MessageResponse messageResponse);
    void handleLeaveSquadResponse(LeaveSquadResponse leaveSquadResponse);
    void handleJoinDemandResponse(JoinDemandResponse joinDemandResponse);
    void handleTransferReqToClientResponse(TransferReqToClientResponse monomachiaInvitationResponse);
    void handleMoveResponse(MoveResponse moveResponse);
    void handleStateResponse(StateResponse stateResponse);
}
