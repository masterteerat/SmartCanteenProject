public class Tester {
    public static void main(String[] args) {
        System.out.println("=============================================");
        System.out.println("   🚀 SMART CANTEEN - ADMIN CONTROL TEST");
        System.out.println("=============================================\n");

        Canteen kmitl = new Canteen("C01", "KMITL Smart Canteen");
        Admin admin = new Admin("U999", "Admin Boss", "admin@mail.com", "pass123", "A001", kmitl);
        Customer cus1 = new Customer("U001", "Bonus", "bonus@mail.com", "1234", "C001", kmitl, null);

        // --- PHASE 1: ADMIN MANAGEMENT ---
        System.out.println("--- [Admin Action] ---");
        // เพิ่มโต๊ะใหม่ (สมมติเป็นโต๊ะ 05 จุ 4 คน)
        Table t05 = new Table("05", 4, kmitl);
        admin.addTable(t05);

        // แอดมินสั่งอัปเดตความจุโต๊ะ 05 เป็น 10 คน ผ่าน ID โดยตรง
        // ไม่ต้องไปสั่ง t05.setCapacity เองข้างนอกแล้ว
        admin.updateTable("05", 10); 


        // --- PHASE 2: CUSTOMER RESERVATION ---
        System.out.println("\n--- [Customer Action] ---");
        System.out.println("Bonus is reserving Table 05 (Now 10 seats)...");
        cus1.makeReservation("05");


        // --- PHASE 3: CHECKOUT & FEEDBACK ---
        System.out.println("\n--- [Process Checkout] ---");
        if (cus1.getReservation() != null) {
            // ให้ Feedback
            cus1.getReservation().addFeedback(cus1, 5, "โต๊ะใหญ่ขึ้นเยอะเลยครับ ขอบคุณแอดมิน!");
            // คืนโต๊ะ
            cus1.checkOut();
        }

        // --- PHASE 4: VIEW RESULTS ---
        System.out.println("\n--- [Admin Review] ---");
        admin.viewAllFeedback();

        System.out.println("=============================================");
        System.out.println("   🎉 SECURE TEST COMPLETED!");
        System.out.println("=============================================");
    }
}