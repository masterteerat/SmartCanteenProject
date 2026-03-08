public class Tester {
    public static void main(String[] args) {
        Canteen kmitl = new Canteen("01");
        Customer c = new Customer("Bonus");
        kmitl.makeReservation(c, "01");
    }
}
