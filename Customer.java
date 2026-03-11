public class Customer extends User {
    private String customerID;
    private Canteen canteen;
    private Reservation reservation;

    public Customer(String uid, String fullName, String email, String pwd, String customerID, Canteen canteen, Reservation res) {
        super(uid, fullName, email, pwd);
        this.customerID = customerID;
        this.canteen = canteen;
        this.reservation = res;
    }

    public boolean makeReservation(String tableID) {
        return canteen.makeReservation(this, tableID);
    }

    public boolean activateReservation() {
        if (reservation == null) {
            System.out.println("[Error] " + getFullName() + " has no reservation to activate.");
            return false;
        }
        return reservation.activateReservation();
    }

    public boolean checkOut() {
        if (reservation == null) {
            System.out.println("[Error] " + getFullName() + " has no active reservation.");
            return false;
        }
        Table table = reservation.getTable();
        if (table.getStatus() != Status.OCCUPIED) {
            System.out.println("[Error] Table " + table.getTableID() + " is not Occupied.");
            return false;
        }
        System.out.println("[CheckOut] " + getFullName() + " checking out from Table " + table.getTableID() + "...");
        DatabaseManager.updateReservationStatus(reservation.getReservationID(), "COMPLETED");
        boolean released = canteen.releaseTable(table.getTableID());
        reservation = null;
        return released;
    }

    public boolean addFeedback(Customer owner, int score, String comment) {
        if (reservation.addFeedback(owner, score, comment)) return true;
        return false;
    }

    public String getCustomerID() { return customerID; }
    public void setCustomerID(String customerID) { this.customerID = customerID; }
    public Canteen getCanteen() { return canteen; }
    public void setCanteen(Canteen canteen) { this.canteen = canteen; }
    public Reservation getReservation() { return reservation; }
    public void setReservation(Reservation reservation) { this.reservation = reservation; }
}