import java.util.List;

public class Reservation {
    private String reservationID;
    private Status status;
    private List<Customer> occupant;
    private Customer owner;
    private Table table;
    private Feedback feedback;

    public Reservation(String resID, Customer owner, Table tab) {
        this.reservationID = resID;
        this.status = Status.RESERVED;
        this.table = tab;
        this.owner = owner;
    }

    public boolean activateReservation() {
        status = Status.OCCUPIED;
        return true;
    }

    public boolean addFeedbaack(Customer owner, int score, String comment) {
        feedback = new Feedback(owner, score, comment, this);
        return true;
    }

    public String getReservationID() {
        return reservationID;
    }

    public void setReservationID(String reservationID) {
        this.reservationID = reservationID;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<Customer> getOccupant() {
        return occupant;
    }

    public void setOccupant(List<Customer> occupant) {
        this.occupant = occupant;
    }

    public Customer getOwner() {
        return owner;
    }

    public void setOwner(Customer owner) {
        this.owner = owner;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public Feedback getFeedback() {
        return feedback;
    }
}