import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:SmartCanteen.db";

    private static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    // Helper: execute INSERT/UPDATE/DELETE
    private static boolean execute(String sql, Object... params) {
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++)
                ps.setObject(i + 1, params[i]);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("[DB Error] " + e.getMessage());
            return false;
        }
    }

    // INIT
    public static void initializeDB() {
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS customers (customerID TEXT PRIMARY KEY, fullName TEXT NOT NULL, email TEXT UNIQUE, password TEXT)");
            stmt.execute("CREATE TABLE IF NOT EXISTS admins    (adminID TEXT PRIMARY KEY, fullName TEXT NOT NULL, email TEXT UNIQUE, password TEXT)");
            stmt.execute("CREATE TABLE IF NOT EXISTS tables    (tableID TEXT PRIMARY KEY, capacity INTEGER, status TEXT)");
            stmt.execute("CREATE TABLE IF NOT EXISTS reservations (reservationID TEXT PRIMARY KEY, customerID TEXT, tableID TEXT, status TEXT, FOREIGN KEY(customerID) REFERENCES customers(customerID), FOREIGN KEY(tableID) REFERENCES tables(tableID))");
            stmt.execute("CREATE TABLE IF NOT EXISTS feedbacks (feedbackID INTEGER PRIMARY KEY AUTOINCREMENT, reservationID TEXT, customerID TEXT, score INTEGER, comment TEXT, FOREIGN KEY(reservationID) REFERENCES reservations(reservationID), FOREIGN KEY(customerID) REFERENCES customers(customerID))");

            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS count FROM tables");
            if (rs.getInt("count") == 0) {
                stmt.execute("INSERT INTO tables    VALUES ('01', 4, 'AVAILABLE')");
                stmt.execute("INSERT INTO tables    VALUES ('02', 2, 'AVAILABLE')");
                stmt.execute("INSERT INTO customers VALUES ('C001', 'Bonus', 'bonus@mail.com', '1234')");
                stmt.execute("INSERT INTO customers VALUES ('C002', 'Ploy',  'ploy@mail.com',  '5678')");
                stmt.execute("INSERT INTO admins    VALUES ('A001', 'Admin Super', 'admin@mail.com', 'admin123')");
                System.out.println("[DB] Initialized with default data.");
            }
        } catch (SQLException e) {
            System.out.println("[DB Error] " + e.getMessage());
        }
    }

    // TABLE
    public static List<Table> loadTablesFromDB(Canteen canteen) {
        List<Table> list = new ArrayList<>();
        try (Connection conn = connect();
             ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM tables")) {
            while (rs.next()) {
                Table t = new Table(rs.getString("tableID"), rs.getInt("capacity"), canteen);
                t.setStatus(Status.valueOf(rs.getString("status")));
                list.add(t);
            }
        } catch (SQLException e) {
            System.out.println("[DB Error] " + e.getMessage());
        }
        return list;
    }

    public static boolean insertTable(Table tab) {
        return execute("INSERT INTO tables VALUES (?, ?, ?)",
                tab.getTableID(), tab.getCapacity(), tab.getStatus().name());
    }

    public static boolean deleteTable(String tableID) {
        return execute("DELETE FROM tables WHERE tableID = ?", tableID);
    }

    public static boolean updateTable(Table tab) {
        return execute("UPDATE tables SET capacity = ?, status = ? WHERE tableID = ?",
                tab.getCapacity(), tab.getStatus().name(), tab.getTableID());
    }

    public static void updateTableStatus(String tableID, String status) {
        execute("UPDATE tables SET status = ? WHERE tableID = ?", status, tableID);
    }

    // RESERVATION
    public static void insertReservation(String resID, String cusID, String tableID) {
        if (execute("INSERT INTO reservations VALUES (?, ?, ?, ?)", resID, cusID, tableID, "OCCUPIED"))
            System.out.println("[DB] Reservation " + resID + " inserted.");
    }

    public static void updateReservationStatus(String reservationID, String status) {
        execute("UPDATE reservations SET status = ? WHERE reservationID = ?", status, reservationID);
    }

    // FEEDBACK
    public static void insertFeedback(String resID, String cusID, int score, String comment) {
        if (execute("INSERT INTO feedbacks (reservationID, customerID, score, comment) VALUES (?, ?, ?, ?)",
                resID, cusID, score, comment))
            System.out.println("[DB] Feedback saved.");
    }

    public static void printAllFeedbacks() {
        String sql = "SELECT f.feedbackID, f.reservationID, c.fullName, f.score, f.comment " +
                     "FROM feedbacks f JOIN customers c ON f.customerID = c.customerID";
        try (Connection conn = connect();
             ResultSet rs = conn.createStatement().executeQuery(sql)) {
            System.out.println("\n========== ALL FEEDBACKS ==========");
            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                System.out.printf("ID: %d | Res: %s | Customer: %s | Score: %d/5%n",
                        rs.getInt("feedbackID"), rs.getString("reservationID"),
                        rs.getString("fullName"), rs.getInt("score"));
                System.out.println("Comment : " + rs.getString("comment"));
                System.out.println("------------------------------------");
            }
            if (!hasData) System.out.println("No feedbacks found.");
            System.out.println("====================================\n");
        } catch (SQLException e) {
            System.out.println("[DB Error] " + e.getMessage());
        }
    }
}