public class Customer extends User {
    private String customerID;
    private Canteen canteen;
    private Reservation reservation;

    public Customer(String uid, String fullName, String email, String pwd, String cusID, Canteen canteen, Reservation res) {
        super(uid, fullName, email, pwd);
        this.customerID = cusID;
        this.canteen = canteen;
        this.reservation = res;
    }

    public void makeReservation(String tableID) {
        this.canteen.makeReservation(this, tableID);
    }

    public void activateReservation() {
        this.reservation.activateReservation();
    }

 public void checkOut() {
        if (this.reservation != null) {
            Table bookedTable = this.reservation.getTable();
            String tableID = bookedTable.getTableID();
            
            if (bookedTable.getStatus() == Status.OCCUPIED) {
                System.out.println("[CheckOut] " + this.getFullName() + " is checking out from Table " + tableID + "...");
                this.canteen.releaseTable(tableID);
                
                this.reservation = null; 
            } else {
                System.out.println("[Error] Table " + tableID + " is not Occupied.");
            }
        } else {
            System.out.println("[Error] " + this.getFullName() + " has no active reservation to check out.");
        }
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public Canteen getCanteen() {
        return canteen;
    }

    public void setCanteen(Canteen canteen) {
        this.canteen = canteen;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }
    
}