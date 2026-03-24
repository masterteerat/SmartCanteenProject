import java.time.LocalDate;
import java.util.List;

public class Tester {
    public static void main(String[] args) {
        System.out.println("========== 1. INITIALIZING SYSTEM ==========");
        Canteen kmitl = new Canteen("C01", "KMITL Smart Canteen");

        // 🌟 ใช้ register ผ่าน Canteen (ถูกหลัก OOP และเอาลง DB ให้ด้วย)
        Admin admin = new Admin("U999", "Boss", "admin@mail.com", "pass123", "A001", kmitl);
        kmitl.registerAdmin(admin);

        Customer cus1 = new Customer("U001", "Bonus", "bonus@mail.com", "1234", "C001", kmitl);
        Customer cus2 = new Customer("U002", "Peako", "peako@mail.com", "4321", "C002", kmitl);
        Customer cus3 = new Customer("U003", "Mew",   "mew@mail.com",   "5555", "C003", kmitl);
        kmitl.registerCustomer(cus1);        
        kmitl.registerCustomer(cus2);        
        kmitl.registerCustomer(cus3);        
        
        System.out.println("System initialized with " + kmitl.getAdmins().size() + " Admin and " + kmitl.getCustomers().size() + " Customers.\n");


        System.out.println("========== 2. ADMIN: TABLE MANAGEMENT ==========");
        Table t01 = new Table("01", 4, kmitl);
        Table t02 = new Table("02", 2, kmitl);
        Table t03 = new Table("03", 8, kmitl); // สร้างมาเพื่อทดสอบการลบ
        
        admin.addTable(t01);
        admin.addTable(t02);
        admin.addTable(t03);
        
        // ทดสอบแก้ไขและลบโต๊ะ
        System.out.println("-- Updating & Removing Tables --");
        admin.updateTable("01", 5); 
        admin.removeTable(t03);
        System.out.println();


        System.out.println("========== 3. CUSTOMER: RESERVATION & CHECKOUT (HAPPY PATH) ==========");
        // Bonus จองและใช้งานโต๊ะ 01
        kmitl.makeReservation(cus1, "01");
        cus1.activateReservation();
        
        // Bonus ชวน Peako มาร่วมโต๊ะ
        if (cus1.getReservation() != null) {
            cus1.getReservation().addOccupant(cus2);
            System.out.println("Success: " + cus2.getFullName() + " joined " + cus1.getFullName() + "'s table.");
        }

        // Checkout และให้คะแนน 5 ดาว
        if (cus1.getReservation() != null && cus1.getReservation().getStatus() == Status.OCCUPIED) {
            cus1.getReservation().addFeedback(cus1, 5, "Great food, very clean!");
            cus1.checkout();
        }
        System.out.println();


        System.out.println("========== 4. CUSTOMER: EDGE CASES (SAD PATH) ==========");
        // Mew จองโต๊ะ 02
        kmitl.makeReservation(cus3, "02");
        cus3.activateReservation();
        
        // Peako พยายามจองโต๊ะ 02 ที่ Mew นั่งอยู่ (ต้องจองไม่ได้)
        System.out.println("-- Try to book an occupied table --");
        boolean isSuccess = kmitl.makeReservation(cus2, "02");
        if (!isSuccess) {
            System.out.println("Expected Fail: Table 02 is not available.");
        }
        
        // Mew เช็คเอาต์และให้คะแนน 3 ดาว
        if (cus3.getReservation() != null && cus3.getReservation().getStatus() == Status.OCCUPIED) {
            cus3.getReservation().addFeedback(cus3, 3, "A bit too noisy.");
            cus3.checkout();
        }
        System.out.println();


        System.out.println("========== 5. ADMIN: VIEW ALL FEEDBACK ==========");
        // ทดสอบให้ Admin ดึงข้อมูลรีวิวทั้งหมดมาดู
        admin.viewAllFeedback();
        System.out.println();


        System.out.println("========== 6. SPECIFIC TABLE STATS (OOP TEST) ==========");
        // ทดสอบการให้ Table และ Canteen ดึงข้อมูลของตัวเอง (เฉพาะโต๊ะ 01)
        List<String> usageHistory = kmitl.getUsage(null, null);
        for (String record : usageHistory) {
            System.out.println(" > " + record);
        }
        
        System.out.println("\n--- Table 01 Average Score ---");
        Double avgScore = t01.getTableAvgScore(null, null);
        System.out.printf("Score: %.2f / 5.0\n", avgScore != null ? avgScore : 0.0);
        System.out.println();


        System.out.println("========== 7. ADMIN: ANALYTICS & REPORTS ==========");
        // ทดสอบออกรายงานภาพรวม
        System.out.println("--- Report: All Time ---");
        Report reportAll = admin.makeReport();
        reportAll.printReport();
        
        System.out.println("--- Report: Specific Period ---");
        LocalDate start = LocalDate.now().minusDays(7); // ย้อนหลัง 7 วัน
        LocalDate end = LocalDate.now().plusDays(7);    // ไปข้างหน้า 7 วัน
        Report periodReport = admin.makeReport(start, end);
        periodReport.printReport();
    }
}