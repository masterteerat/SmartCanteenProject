public abstract class User {
    private String userID;
    private String fullName;
    private String email;
    private String password;
    private Class<? extends User> role;
    
    public User(String uid, String fullName, String email, String pwd) {
        this.userID = uid;
        this.fullName = fullName;
        this.email = email;
        this.password = pwd;
        this.role = this.getClass();
    }

    public boolean login(String email, String password) { return true; }
    public boolean logout() { return true; }
    public String getUserID() { return userID; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Class<? extends User> getRole() { return role; }
}
