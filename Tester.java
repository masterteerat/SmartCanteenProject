public class Tester {
    public static void main(String[] args) {
        System.out.println("Initial Data.....\n");
        Canteen kmitl = new Canteen("C01", "KMITL Smart Canteen");

        Admin admin = new Admin("U999", "Boss", "admin@mail.com", "pass123", "A001", kmitl);
        DatabaseManager.insertAdmin(admin);

        Customer cus1 = new Customer("U001", "Bonus", "bonus@mail.com", "1234", "C001", kmitl);
        DatabaseManager.insertCustomer(cus1);        

        System.out.println("\nAdmin");
        Table t01 = new Table("01", 4, kmitl);
        admin.addTable(t01);
        admin.updateTable("01", 10); 

        System.out.println("\nCustomer reserve the seat");
        kmitl.makeReservation(cus1, "01");
        cus1.activateReservation();

        System.out.println("\nFeedback and Checkout");
        if (cus1.getReservation() != null && cus1.getReservation().getStatus() == Status.OCCUPIED) {
            cus1.getReservation().addFeedback(cus1, 5, "This table is so dirty");
            cus1.checkout();
        }

        admin.viewAllFeedback();
        
        Report report = new Report(admin, kmitl);
        report.printReport();
    }
}