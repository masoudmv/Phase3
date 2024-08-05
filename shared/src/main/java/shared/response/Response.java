package shared.response;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import shared.response.game.NullResponse;
import shared.response.game.PauseResponse;
import shared.response.game.StateResponse;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "subclassType"
)

@JsonSubTypes({
        @JsonSubTypes.Type(value = HiResponse.class, name = "hiResponse"),
        @JsonSubTypes.Type(value = GetSquadsListResponse.class, name = "GetSquadsListResponse"),
        @JsonSubTypes.Type(value = CreateSquadResponse.class, name = "CreateSquadResponse"),
        @JsonSubTypes.Type(value = IdentificationResponse.class, name = "IdentificationResponse"),
        @JsonSubTypes.Type(value = DonateResponse.class, name = "DonateResponse"),
        @JsonSubTypes.Type(value = MessageResponse.class, name = "MessageResponse"),
        @JsonSubTypes.Type(value = LeaveSquadResponse.class, name = "LeaveSquadResponse"),
        @JsonSubTypes.Type(value = JoinDemandResponse.class, name = "JoinDemandResponse"),
        @JsonSubTypes.Type(value = TransferReqToClientResponse.class, name = "TransferReqToClientResponse"),

        @JsonSubTypes.Type(value = NullResponse.class, name = "NullResponse"),
        @JsonSubTypes.Type(value = StateResponse.class, name = "StateResponse"),
        @JsonSubTypes.Type(value = PauseResponse.class, name = "PauseResponse"),

})
public interface Response {
    void run(ResponseHandler responseHandler);
}
