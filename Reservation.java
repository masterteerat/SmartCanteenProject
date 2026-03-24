import java.util.ArrayList;
import java.util.List;

import javax.xml.crypto.Data;

public class Reservation {
    private String reservationID;
    private Status status;
    private List<Customer> occupant;
    private Customer owner;
    private Table table;
    private Feedback feedback;

    public Reservation(String resID, Customer owner, Table tab) {
        this.reservationID = resID;
        this.owner = owner;
        this.table = tab;
        this.status = Status.RESERVED;
    }

    public boolean activateReservation() {
        status = Status.OCCUPIED;
        return true;
    }

    public boolean addFeedback(Customer owner, int score, String comment) {
        String feedbackID = "FB" + System.currentTimeMillis();
        feedback = new Feedback(feedbackID, owner, score, comment, this);
        DatabaseManager.insertFeedback(reservationID, owner.getCustomerID(), score, comment);
        return true;
    }

    public boolean addOccupant(Customer c) {
        if (occupant == null) occupant = new ArrayList<>();
        if (occupant.contains(c)) return false;
        if (occupant.size() + 1 >= table.getCapacity()) {
            return false;
        }
        occupant.add(c);
        DatabaseManager.insertOccupant(reservationID, c.getCustomerID());
        return true;
    }

    public String getReservationID() { return reservationID; }
    public void setReservationID(String reservationID) { this.reservationID = reservationID; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public List<Customer> getOccupant() { return occupant; }
    public void setOccupant(List<Customer> occupant) { this.occupant = occupant; }
    public Customer getOwner() { return owner; }
    public void setOwner(Customer owner) { this.owner = owner; }
    public Table getTable() { return table; }
    public void setTable(Table table) { this.table = table; }
    public Feedback getFeedback() { return feedback; }
}