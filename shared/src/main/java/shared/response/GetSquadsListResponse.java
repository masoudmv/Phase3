package shared.response;

import shared.model.Squad;

import java.util.List;


import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("GetSquadsListResponse")

public class GetSquadsListResponse implements Response{
    private List<Squad> list;
    String message = null;

    public GetSquadsListResponse(String message) {
        this.message = message;
    }

    public GetSquadsListResponse(List<Squad> list) {
        this.list = list;
    }

    public GetSquadsListResponse() {
    }

    public List<Squad> getList() {
        return list;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public void run(ResponseHandler responseHandler) {
        responseHandler.handleGetSquadsListResponse(this);
    }
}
