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

        initializeData();
    }

    public void initializeData() {
        tableList.add(new Table("01", 1, this));
        tableList.add(new Table("02", 2, this));
    }

    public void makeReservation(Customer c, String tableID){
        Table targetTable = tableList.stream()
            .filter(t -> t.getTableID().equals(tableID))
            .findFirst()
            .orElse(null);

        if (targetTable != null && targetTable.checkAvailability()) {
            String resID = "RES" + System.currentTimeMillis();
            targetTable.addReservation(c, resID);
        }
    }

    public boolean addTable(Table tab) {
        if (tableList.add(tab)) {
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
