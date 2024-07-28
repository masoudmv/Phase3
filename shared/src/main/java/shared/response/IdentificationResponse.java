package shared.response;


import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("IdentificationResponse")
public class IdentificationResponse implements Response{
    private String username;

    public IdentificationResponse(String username) {
        this.username = username;
    }

    public IdentificationResponse() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public void run(ResponseHandler responseHandler) {
        responseHandler.handleIdentificationResponse(this);
    }
}
