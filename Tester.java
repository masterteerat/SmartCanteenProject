public class Tester {
    public static void main(String[] args) {
        System.out.println("=== Smart Canteen System Initialization ===");
        
        // 1. Create a Canteen
        Canteen kmitl = new Canteen("C01", "KMITL Smart Canteen");
        System.out.println("Loading initial tables... Total tables in system: " + kmitl.getTableList().size() + "\n");

        // 2. Create Mock Customers
        Customer cus1 = new Customer("U001", "Bonus", "bonus@mail.com", "1234", "C001", kmitl, null);
        Customer cus2 = new Customer("U002", "Ploy", "ploy@mail.com", "5678", "C002", kmitl, null);

        // 3. Test Reservation Process
        System.out.println("--- Testing Reservation Process ---");
        
        // Bonus reserves Table 01 (ลูกค้าจองเอง)
        cus1.makeReservation("01");

        // Ploy tries to reserve Table 01 (ลูกค้าจองเอง แต่ต้องจองไม่ได้เพราะ Bonus จองไปแล้ว)
        cus2.makeReservation("01");

        // Ploy changes to reserve Table 02 (ลูกค้าจองเอง คราวนี้ต้องสำเร็จ)
        cus2.makeReservation("02");

        // 4. Print Summary
        System.out.println("\n--- Reservation Summary ---");
        System.out.println("Table 01 Status: " + kmitl.getTableList().get(0).getStatus());
        System.out.println("Table 02 Status: " + kmitl.getTableList().get(1).getStatus());
    }
}