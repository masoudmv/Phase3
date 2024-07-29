package shared.request;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import shared.request.leader.JoinDemandStatusReq;
import shared.request.leader.KickPlayerReq;
import shared.request.leader.PurchaseSkillRequest;
import shared.request.member.DonateRequest;
import shared.request.member.InitMonomachiaReq;
import shared.request.member.LeaveSquadReq;
import shared.request.member.MonomachiaInvitationStatusReq;
import shared.request.nonmember.CreateSquadRequest;
import shared.request.nonmember.GetSquadsListRequest;
import shared.request.nonmember.JoinSquadReq;
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
        @JsonSubTypes.Type(value = InitMonomachiaReq.class, name = "InitMonomachiaReq"),
        @JsonSubTypes.Type(value = MonomachiaInvitationStatusReq.class, name = "MonomachiaInvitationStatusReq"),
})
public interface Request {
    Response run(RequestHandler requestHandler);
}
