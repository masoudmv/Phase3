package shared.response;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

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

})
public interface Response {
    void run(ResponseHandler responseHandler);
}
