public class Table {
    private String tableID;
    private int capacity;
    private Status status;
    private Canteen canteen;
    private Reservation reservation;

    public Table(String tableID, int capacity, Canteen canteen) {
        this.tableID = tableID;
        this.capacity = capacity;
        this.canteen = canteen;
        this.status = Status.AVAILABLE;
    }

    public boolean addReservation(Customer owner, String resID) {
        reservation = new Reservation(resID, owner, this);
        status = Status.OCCUPIED;
        owner.setReservation(reservation);
        return true;
    }

    public boolean checkAvailability() {
        return status == Status.AVAILABLE;
    }

    public String getTableID() { return tableID; }
    public void setTableID(String tableID) { this.tableID = tableID; }
    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public Canteen getCanteen() { return canteen; }
    public void setCanteen(Canteen canteen) { this.canteen = canteen; }
    public Reservation getReservation() { return reservation; }
    public void setReservation(Reservation reservation) { this.reservation = reservation; }
}