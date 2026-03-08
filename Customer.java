public class Customer extends User {
    private String customerID;
    private Reservation reservation;

    public Customer(String fullName){
       super(fullName);
    }

    public void addReservation(Reservation res) {
        reservation = res;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public Reservation getRes() {
        return reservation;
    }
}
