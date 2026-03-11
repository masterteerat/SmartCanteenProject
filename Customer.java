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