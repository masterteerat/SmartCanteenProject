public class Reservation {
    private String reservationID;
    private String status;
    private Table table;
    private Customer owner;

    public Reservation(String resID, String status, Table tab, Customer own) {
        this.reservationID = resID;
        this.status = status;
        this.table = tab;
        this.owner = own;
        this.owner.addReservation(this);
    }

    public String getReservationID() {
        return reservationID;
    }

    public void setReservationID(String reservationID) {
        this.reservationID = reservationID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public Customer getOwner() {
        return owner;
    }

    public void setOwner(Customer owner) {
        this.owner = owner;
    }

    

}
