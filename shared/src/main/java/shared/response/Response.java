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

})
public interface Response {
    void run(ResponseHandler responseHandler);
}
