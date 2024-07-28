package shared.request;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
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
})
public interface Request {
    Response run(RequestHandler requestHandler);
}
