import java.util.ArrayList;
import java.util.List;

public class Canteen {
    private String canteenID;
    private String canteenName;
    private List<Customer> customers;
    private List<Admin> admins;
    private List<Table> tableList;

    public Canteen(String canteenID, String name) {
        this.canteenID = canteenID;
        this.canteenName = name;
        this.tableList = new ArrayList<>();
        DatabaseManager.initializeDB();
        this.tableList = DatabaseManager.loadTablesFromDB(this);
    }

    private Table findTable(String tableID) {
        return tableList.stream()
            .filter(t -> t.getTableID().equals(tableID))
            .findFirst().orElse(null);
    }

    public boolean makeReservation(Customer c, String tableID) {
        Table target = findTable(tableID);
        if (target != null && target.checkAvailability()) {
            String resID = "RES" + System.currentTimeMillis();
            target.addReservation(c, resID);
            DatabaseManager.insertReservation(resID, c.getCustomerID(), tableID);
            DatabaseManager.updateTableStatus(tableID, "OCCUPIED");
            return true;
        }
        System.out.println("[Canteen] Table " + tableID + " is not available.");
        return false;
    }

    public boolean releaseTable(String tableID) {
        Table target = findTable(tableID);
        if (target != null && target.getStatus() != Status.AVAILABLE) {
            target.setStatus(Status.AVAILABLE);
            target.setReservation(null);
            DatabaseManager.updateTableStatus(tableID, "AVAILABLE");
            System.out.println("[Canteen] Table " + tableID + " released.");
            return true;
        }
        System.out.println("[Canteen Error] Cannot release Table " + tableID + ".");
        return false;
    }

    public boolean addTable(Table tab) { return tableList.add(tab); }
    public boolean removeTable(Table tab) { return tableList.remove(tab); }

    public String getCanteenID() { return canteenID; }
    public void setCanteenID(String canteenID) { this.canteenID = canteenID; }
    public String getCanteenName() { return canteenName; }
    public void setCanteenName(String canteenName) { this.canteenName = canteenName; }
    public List<Customer> getCustomers() { return customers; }
    public void setCustomers(List<Customer> customers) { this.customers = customers; }
    public List<Admin> getAdmins() { return admins; }
    public void setAdmins(List<Admin> admins) { this.admins = admins; }
    public List<Table> getTableList() { return tableList; }
    public void setTableList(List<Table> tableList) { this.tableList = tableList; }
}