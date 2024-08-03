package shared.request;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import shared.request.game.StateRequest;
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
import shared.request.game.MoveRequest;
import shared.response.Response;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "subclassType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = HiRequest.class, name = "hiRequest"),
        @JsonSubTypes.Type(value = LoginRequest.class, name = "loginRequest"),
        @JsonSubTypes.Type(value = GetSquadsListRequest.class, name = "GetSquadsListRequest"),
        @JsonSubTypes.Type(value = CreateSquadRequest.class, name = "CreateSquadRequest"),
        @JsonSubTypes.Type(value = IdentificationRequest.class, name = "IdentificationRequest"),
        @JsonSubTypes.Type(value = DonateRequest.class, name = "DonateRequest"),
        @JsonSubTypes.Type(value = PurchaseSkillRequest.class, name = "PurchaseSkillRequest"),
        @JsonSubTypes.Type(value = LeaveSquadReq.class, name = "LeaveSquadReq"),
        @JsonSubTypes.Type(value = JoinSquadReq.class, name = "JoinSquadReq"),
        @JsonSubTypes.Type(value = JoinDemandStatusReq.class, name = "JoinDemandStatusReq"),
        @JsonSubTypes.Type(value = KickPlayerReq.class, name = "KickPlayerReq"),
        @JsonSubTypes.Type(value = AskForSthRequest.class, name = "AskForSthRequest"),
        @JsonSubTypes.Type(value = ReportAskedPleaRequest.class, name = "ReportAskedPleaRequest"),
        @JsonSubTypes.Type(value = MoveRequest.class, name = "MoveRequest"),
        @JsonSubTypes.Type(value = StateRequest.class, name = "StateRequest"),
})
public interface Request {
    Response run(RequestHandler requestHandler);
}
