package shared.response;

import shared.response.game.EndOfGameResponse;
import shared.response.game.NullResponse;
import shared.response.game.PauseResponse;
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

    void handleNullResponse(NullResponse nullResponse);
    void handleStateResponse(StateResponse stateResponse);
    void handlePauseResponse(PauseResponse pauseResponse);
    void handleEndOfGameResponse(EndOfGameResponse endOfGameResponse);
}
