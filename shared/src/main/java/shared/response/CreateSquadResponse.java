package shared.response;


import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("CreateSquadResponse")

public class CreateSquadResponse implements Response{

    public CreateSquadResponse() {
    }

    @Override
    public void run(ResponseHandler responseHandler) {
        responseHandler.handleCreateSquadResponse(this);
    }
}
