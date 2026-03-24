import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Canteen {
    private String canteenID;
    private String canteenName;
    private List<Customer> customers;
    private List<Admin> admins;
    private List<Table> tableList;
    private List<Report> reportLog;

    public Canteen(String canteenID, String name) {
        this.canteenID = canteenID;
        this.canteenName = name;
        this.customers = new ArrayList<>();
        this.admins = new ArrayList<>();
        this.tableList = new ArrayList<>();
        DatabaseManager.initializeDB();
        this.tableList = DatabaseManager.loadTablesFromDB(this);
    }

    public void registerCustomer(Customer c) {
        this.customers.add(c);
        DatabaseManager.insertCustomer(c);
    }

    public void registerAdmin(Admin a) {
        this.admins.add(a);
        DatabaseManager.insertAdmin(a);
    }

    private Table findTable(String tableID) {
        return tableList.stream()
            .filter(t -> t.getTableID().equals(tableID))
            .findFirst().orElse(null);
    }

    public boolean makeReservation(Customer c, String tableID) {
        if (c.getReservation() != null) return false;
        Table target = findTable(tableID);
        if (target != null && target.checkAvailability()) {
            String resID = "RES" + System.currentTimeMillis();
            target.addReservation(c, resID);
            DatabaseManager.insertReservation(resID, c.getCustomerID(), tableID);
            DatabaseManager.updateTableStatus(tableID, "OCCUPIED");
            System.out.println("Reserve Table " + tableID + " success");
            return true;
        }
        System.out.println("Table " + tableID + " is not available.");
        return false;
    }

    public boolean releaseTable(String tableID) {
        Table target = findTable(tableID);
        if (target != null && target.getStatus() != Status.AVAILABLE) {
            target.setStatus(Status.AVAILABLE);
            target.setReservation(null);
            DatabaseManager.updateTableStatus(tableID, "AVAILABLE");
            System.out.println("Table " + tableID + " released.");
            return true;
        }
        System.out.println("Cannot release Table " + tableID + ".");
        return false;
    }

    public Report makeReport(Admin admin) {
        return new Report(admin, this);
    }

    public Report makeReport(Admin admin, LocalDate start, LocalDate end) {
        return new Report(admin, this, start, end);
    }

    public List<String> getUsage(LocalDate start, LocalDate end) {
        List<String> allUsage = new ArrayList<>();
        
        for (Table t : tableList) {
            allUsage.add(">>> History of Table " + t.getTableID());
            List<String> tableUsage = t.getTableUsage(start, end);
            
            if (tableUsage.isEmpty()) {
                allUsage.add(" - No records");
            } else {
                for (String record : tableUsage) {
                    allUsage.add(" - " + record);
                }
            }
        }
        return allUsage;
    }

    public int getTotalReservations(LocalDate start, LocalDate end) {
        return DatabaseManager.getTotalReservationsCount(start, end);
    }

    public double getAverageScore(LocalDate start, LocalDate end) {
        return DatabaseManager.getAverageScore(start, end);
    }

    public String getMostPopularTable(LocalDate start, LocalDate end) {
        return DatabaseManager.getMostPopularTable(start, end);
    }

    public String getPeakHour(LocalDate start, LocalDate end) {
        return DatabaseManager.getPeakHour(start, end);
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