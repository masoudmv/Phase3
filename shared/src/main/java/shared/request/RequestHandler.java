package shared.request;

import shared.response.Response;

public interface RequestHandler {
    Response handleHiRequest(HiRequest hiRequest);
    Response handleLoginRequest(LoginRequest loginRequest);
    Response handleGetSquadsRequest(GetSquadsListRequest getSquadsListRequest);
    Response handleCreateSquadRequest(CreateSquadRequest createSquadRequest);
    Response handleIdentificationRequest(IdentificationRequest identificationRequest);
    Response handleDonateRequest(DonateRequest donateRequest);
    Response handlePurchaseSkillRequest(PurchaseSkillRequest purchaseSkillRequest);
    Response handleLeaveSquadReq(LeaveSquadReq leaveSquadReq);
    Response handleJoinSquadReq(JoinSquadReq joinSquadReq);
    Response handleJoinDemandStatusReq(JoinDemandStatusReq joinDemandStatusReq);
    Response handleKickPlayerReq(KickPlayerReq kickPlayerReq);
}
