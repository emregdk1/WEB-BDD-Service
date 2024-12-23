package services.pojo.requests;

public class DeleteBookingRequest {
    private int id;

    public DeleteBookingRequest() {
    }

    public DeleteBookingRequest(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
