public class Admin extends User {
    private String adminID;
    private Canteen canteen;

    public Admin(String uid, String fullName, String email, String pwd, String adminID, Canteen canteen) {
        super(uid, fullName, email, pwd);
        this.canteen = canteen;
    }

    public boolean AddTable(Table tab) {
        return true;
    }

    public boolean RemoveTable(Table tab) {
        return true;
    }

    public boolean UpdateTable(Table tab) {
        return true;
    }

    public void ViewFeedback() {

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