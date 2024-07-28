package shared.response;


import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("DonateResponse")

public class DonateResponse implements Response{
    private String message;

    public DonateResponse(String message) {
        this.message = message;
    }

    public DonateResponse() {
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public void run(ResponseHandler responseHandler) {
        responseHandler.handleDonateResponse(this);
    }
}
