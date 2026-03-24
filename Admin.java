import java.time.LocalDate;

public class Admin extends User {
    private String adminID;
    private Canteen canteen;

    public Admin(String uid, String fullName, String email, String pwd, String adminID, Canteen canteen) {
        super(uid, fullName, email, pwd);
        this.adminID = adminID;
        this.canteen = canteen;
    }

    public boolean addTable(Table tab) {
        if (canteen.getTableList().stream().anyMatch(t -> t.getTableID().equals(tab.getTableID()))) {
            System.out.println("Error: Table " + tab.getTableID() + " already exists!");
            return false;
        }
        
        canteen.addTable(tab);
        boolean success = DatabaseManager.insertTable(tab);
        if (success) System.out.println("Success: Table " + tab.getTableID() + " added.");
        return success;
    }

    public boolean removeTable(Table tab) {
        if (!canteen.removeTable(tab)) {
            System.out.println("Error: Table " + tab.getTableID() + " not found.");
            return false;
        }
        System.out.println("Success: Table " + tab.getTableID() + " removed.");
        return DatabaseManager.deleteTable(tab.getTableID());
    }

    public boolean updateTable(String tableID, int newCapacity) {
        return canteen.getTableList().stream()
            .filter(t -> t.getTableID().equals(tableID))
            .findFirst()
            .map(t -> {
                t.setCapacity(newCapacity);
                boolean success = DatabaseManager.updateTable(t);
                if (success) System.out.println("Success: Table " + tableID + " updated to " + newCapacity + " seats.");
                return success;
            })
            .orElseGet(() -> {
                System.out.println("Error: Table " + tableID + " not found.");
                return false;
            });
    }

    public void viewAllFeedback() {
        DatabaseManager.printAllFeedbacks();
    }

    public Report makeReport() {
        return canteen.makeReport(this);
    }

    public Report makeReport(LocalDate start, LocalDate end) {
        return canteen.makeReport(this, start, end);
    }

    public String getAdminID() { return adminID; }
    public void setAdminID(String adminID) { this.adminID = adminID; }
    public Canteen getCanteen() { return canteen; }
    public void setCanteen(Canteen canteen) { this.canteen = canteen; }
}