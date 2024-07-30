package shared.response;

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
}
