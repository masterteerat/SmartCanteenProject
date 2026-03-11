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
        tableList = new ArrayList<>();

        DatabaseManager.initializeDB();
        initializeData();
    }

    public void initializeData() {
        this.tableList = DatabaseManager.loadTablesFromDB(this);
    }

    public void makeReservation(Customer c, String tableID){
        Table targetTable = tableList.stream()
            .filter(t -> t.getTableID().equals(tableID))
            .findFirst()
            .orElse(null);

        if (targetTable != null && targetTable.checkAvailability()) {
            String resID = "RES" + System.currentTimeMillis();
            targetTable.addReservation(c, resID);

            DatabaseManager.updateTableStatus(tableID, "OCCUPIED");
        }
    }

    public void releaseTable(String tableID) {
        Table targetTable = tableList.stream()
            .filter(t -> t.getTableID().equals(tableID))
            .findFirst()
            .orElse(null);
        if (targetTable != null && targetTable.getStatus() != Status.AVAILABLE) {
            targetTable.setStatus(Status.AVAILABLE);
            targetTable.setReservation(null); 
            DatabaseManager.updateTableStatus(tableID, "AVAILABLE");
            System.out.println("[Canteen] Table " + tableID + " has been successfully released.");
        } else {
            System.out.println("[Canteen Error] Cannot release Table " + tableID + ". It might already be available or not found.");
        }
    }
    
    public boolean addTable(Table tab) {
        if (tableList.add(tab)) {
            return true;
        }   
        return false;
    }

    public boolean removeTable(Table tab) {
        if (tableList.remove(tab)) {
            return true;
        }
        return false;
    }

    public String getCanteenID() {
        return canteenID;
    }

    public void setCanteenID(String canteenID) {
        this.canteenID = canteenID;
    }

    public String getCanteenName() {
        return canteenName;
    }

    public void setCanteenName(String canteenName) {
        this.canteenName = canteenName;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }

    public List<Admin> getAdmins() {
        return admins;
    }

    public void setAdmins(List<Admin> admins) {
        this.admins = admins;
    }

    public List<Table> getTableList() {
        return tableList;
    }

    public void setTableList(List<Table> tableList) {
        this.tableList = tableList;
    }
}
