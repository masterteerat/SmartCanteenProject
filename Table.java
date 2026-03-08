public class Table {
    private String TableID;
    private String status;
    private int capacity;

    public Table(String TableID, int capacity){
        this.TableID = TableID;
        this.capacity = capacity;
        status = "Available";
    }

    public Reservation addReservation(Customer owner) {
        Reservation res = new Reservation("RES01", "Reserved", this, owner);
        return res;
    }

    public Boolean checkAvailability(){
        if (!status.equals("Available")) return false;
        return true;
    }

    public String getTableID() {
        return TableID;
    }

    public void setTableID(String tableID) {
        TableID = tableID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    
}
