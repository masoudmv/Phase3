package shared.request;

import shared.request.game.*;
import shared.request.leader.JoinDemandStatusReq;
import shared.request.leader.KickPlayerReq;
import shared.request.leader.PurchaseSkillRequest;
import shared.request.member.DonateRequest;
import shared.request.member.AskForSthRequest;
import shared.request.member.LeaveSquadReq;
import shared.request.member.ReportAskedPleaRequest;
import shared.request.nonmember.CreateSquadRequest;
import shared.request.nonmember.GetSquadsListRequest;
import shared.request.nonmember.JoinSquadReq;
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
    Response handleAskForSthReq(AskForSthRequest askForSthRequest);
    Response handleReportAskedPleaReq(ReportAskedPleaRequest reportAskedPleaRequest);

    Response handleMoveReq(MoveRequest moveRequest);
    Response handleStateRequest(StateRequest stateRequest);
    Response handleClickedRequest(ClickedRequest clickedRequest);
    Response handlePauseRequest(PauseRequest pauseRequest);
    Response handleBuyAbilityRequest(BuyAbilityRequest buyAbilityRequest);
    Response handleActivateAbilityRequest(ActivateAbilityRequest activateAbilityRequest);
    Response handleActivateSkillRequest(ActivateSkillRequest activateSkillRequest);
}
