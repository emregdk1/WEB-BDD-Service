package services.pojo.requests;

public class GetBookingRequest {
    private String id;

    public GetBookingRequest() {
    }

    public GetBookingRequest(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
