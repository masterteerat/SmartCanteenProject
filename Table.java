public class Table {
    private String TableID;
    private int capacity;
    private Status status;
    private Canteen canteen;
    private Reservation reservation;

    public Table(String TableID, int capacity, Canteen canteen) {
        this.TableID = TableID;
        this.capacity = capacity;
        status = Status.AVAILABLE;
    }

    public boolean addReservation(Customer owner, String resID) {
        Reservation res = new Reservation(resID, owner, this);
        reservation = res;
        status = Status.OCCUPIED;
        owner.setReservation(res);
        return true;
    }

    public Boolean checkAvailability() {
        return status == Status.AVAILABLE;
    }

    public String getTableID() {
        return TableID;
    }

    public void setTableID(String tableID) {
        TableID = tableID;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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