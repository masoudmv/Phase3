package shared.response;

import shared.Model.Squad;

import java.util.ArrayList;
import java.util.List;


import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("GetSquadsListResponse")

public class GetSquadsListResponse implements Response{
    private List<Squad> list;

    public GetSquadsListResponse(List<Squad> list) {
        this.list = list;
    }

    public GetSquadsListResponse() {
    }

    public List<Squad> getList() {
        return list;
    }

    @Override
    public void run(ResponseHandler responseHandler) {
        responseHandler.handleGetSquadsListResponse(this);
    }
}
