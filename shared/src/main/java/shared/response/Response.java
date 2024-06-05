package shared.response;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "subclassType"
)

@JsonSubTypes({
        @JsonSubTypes.Type(value = HiResponse.class, name = "hiResponse"),

})
public interface Response {
    void run(ResponseHandler responseHandler);
}
