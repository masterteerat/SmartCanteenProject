public class Admin extends User {
    private String adminID;
    private Canteen canteen;

    public Admin(String uid, String fullName, String email, String pwd, String adminID, Canteen canteen) {
        super(uid, fullName, email, pwd);
        this.canteen = canteen;
    }

    public boolean AddTable(Table tab) {
        boolean exists = canteen.getTableList().stream()
                .anyMatch(t -> t.getTableID().equals(tab.getTableID()));
        
        if (exists) {
            System.out.println("[Admin] Error: Table " + tab.getTableID() + " already exists!");
            return false;
        }

        canteen.addTable(tab);
        boolean dbSuccess = DatabaseManager.insertTable(tab);
        
        if(dbSuccess) {
            System.out.println("[Admin] Success: Table " + tab.getTableID() + " added.");
        }
        return dbSuccess;
    }

    public boolean RemoveTable(Table tab) {
        boolean removedFromList = canteen.removeTable(tab);
        if (removedFromList) {
            boolean dbSuccess = DatabaseManager.deleteTable(tab.getTableID());
            System.out.println("[Admin] Success: Table " + tab.getTableID() + " removed.");
            return dbSuccess;
        } else {
            System.out.println("[Admin] Error: Table " + tab.getTableID() + " not found in Canteen.");
            return false;
        }
    }

    public boolean UpdateTable(String tableID, int newCapacity) {
        for (Table t : canteen.getTableList()) {
            if (t.getTableID().equals(tableID)) {
                t.setCapacity(newCapacity);
                boolean dbSuccess = DatabaseManager.updateTable(t);
                
                if (dbSuccess) {
                    System.out.println("[Admin] Success: Table " + tableID + " has been updated to " + newCapacity + " seats.");
                }
                return dbSuccess;
            }
        }
        System.out.println("[Admin] Error: Table " + tableID + " not found.");
        return false;
    }

    public void ViewAllFeedback() {
        System.out.println("[Admin] " + this.getFullName() + " is requesting all feedback records...");
        DatabaseManager.printAllFeedbacks();
    }

    public String getAdminID() {
        return adminID;
    }

    public void setAdminID(String adminID) {
        this.adminID = adminID;
    }

    public Canteen getCanteen() {
        return canteen;
    }

    public void setCanteen(Canteen canteen) {
        this.canteen = canteen;
    }
}