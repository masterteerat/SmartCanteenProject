import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:SmartCanteen.db";

    private static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

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

    // ─── INIT ───────────────────────────────────────────────────────────────
    public static void initializeDB() {
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS customers (customerID TEXT PRIMARY KEY, fullName TEXT NOT NULL, email TEXT UNIQUE, password TEXT)");
            stmt.execute("CREATE TABLE IF NOT EXISTS admins    (adminID TEXT PRIMARY KEY, fullName TEXT NOT NULL, email TEXT UNIQUE, password TEXT)");
            stmt.execute("CREATE TABLE IF NOT EXISTS tables    (tableID TEXT PRIMARY KEY, capacity INTEGER, status TEXT)");
            stmt.execute("CREATE TABLE IF NOT EXISTS reservations ("
                    + "reservationID TEXT PRIMARY KEY, "
                    + "customerID TEXT, "
                    + "tableID TEXT, "
                    + "status TEXT, "
                    + "reserveTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                    + "FOREIGN KEY(customerID) REFERENCES customers(customerID), "
                    + "FOREIGN KEY(tableID) REFERENCES tables(tableID))");
            stmt.execute("CREATE TABLE IF NOT EXISTS reservation_occupants ("
                    + "reservationID TEXT, "
                    + "customerID TEXT, "
                    + "PRIMARY KEY(reservationID, customerID), "
                    + "FOREIGN KEY(reservationID) REFERENCES reservations(reservationID), "
                    + "FOREIGN KEY(customerID) REFERENCES customers(customerID))");
            stmt.execute("CREATE TABLE IF NOT EXISTS feedbacks ("
                    + "feedbackID INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "reservationID TEXT, "
                    + "customerID TEXT, "
                    + "score INTEGER, "
                    + "comment TEXT, "
                    + "FOREIGN KEY(reservationID) REFERENCES reservations(reservationID), "
                    + "FOREIGN KEY(customerID) REFERENCES customers(customerID))");
        } catch (SQLException e) {
            System.out.println("[DB Error] " + e.getMessage());
        }
    }

    // ─── TABLE ──────────────────────────────────────────────────────────────
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

    public static double getSpecificTableAverageScore(LocalDate start, LocalDate end, String tableID) {
        String dateCondition = "";
        if (start != null && end != null) {
            dateCondition = " AND date(r.reserveTime) BETWEEN '" + start + "' AND '" + end + "'";
        }
        
        String sql = "SELECT AVG(f.score) AS avg FROM feedbacks f "
                   + "JOIN reservations r ON f.reservationID = r.reservationID "
                   + "WHERE r.tableID = '" + tableID + "'" + dateCondition;
                   
        try (Connection conn = connect();
             ResultSet rs = conn.createStatement().executeQuery(sql)) {
            return rs.getDouble("avg");
        } catch (SQLException e) {
            System.out.println("[DB Error] " + e.getMessage());
            return 0.0;
        }
    }

    public static List<String> getSpecificTableUsage(LocalDate start, LocalDate end, String tableID) {
        List<String> usageList = new ArrayList<>();
        String dateCondition = "";
        if (start != null && end != null) {
            dateCondition = " AND date(reserveTime) BETWEEN '" + start + "' AND '" + end + "'";
        }
        
        String sql = "SELECT reservationID, customerID, reserveTime, status "
                   + "FROM reservations "
                   + "WHERE tableID = '" + tableID + "'" + dateCondition
                   + " ORDER BY reserveTime DESC";
                   
        try (Connection conn = connect();
             ResultSet rs = conn.createStatement().executeQuery(sql)) {
            while (rs.next()) {
                String record = String.format("Time: %s | ResID: %s | Customer: %s | Status: %s",
                        rs.getString("reserveTime"),
                        rs.getString("reservationID"),
                        rs.getString("customerID"),
                        rs.getString("status"));
                usageList.add(record);
            }
        } catch (SQLException e) {
            System.out.println("[DB Error] " + e.getMessage());
        }
        return usageList;
    }

    // ─── CUSTOMER / ADMIN ───────────────────────────────────────────────────
    public static void insertCustomer(Customer c) {
        String sql = "INSERT OR IGNORE INTO customers (customerID, fullName, email, password) VALUES (?, ?, ?, ?)";
        try (Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getCustomerID());
            ps.setString(2, c.getFullName());
            ps.setString(3, c.getEmail());
            ps.setString(4, c.getPassword());
            ps.executeUpdate();
            System.out.println("Customer " + c.getFullName() + " saved.");
        } catch (SQLException e) {
            System.out.println("[DB Error - insertCustomer] " + e.getMessage());
        }
    }

    public static void insertAdmin(Admin a) {
        String sql = "INSERT OR IGNORE INTO admins (adminID, fullName, email, password) VALUES (?, ?, ?, ?)";
        try (Connection conn = connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, a.getAdminID());
            ps.setString(2, a.getFullName());
            ps.setString(3, a.getEmail());
            ps.setString(4, a.getPassword());
            ps.executeUpdate();
            System.out.println("Admin " + a.getFullName() + " saved.");
        } catch (SQLException e) {
            System.out.println("[DB Error - insertAdmin] " + e.getMessage());
        }
    }

    // ─── RESERVATION ────────────────────────────────────────────────────────
    public static void insertReservation(String resID, String cusID, String tableID) {
        execute("INSERT INTO reservations (reservationID, customerID, tableID, status) VALUES (?, ?, ?, ?)",
                resID, cusID, tableID, "RESERVED");
    }

    public static void updateReservationStatus(String reservationID, String status) {
        execute("UPDATE reservations SET status = ? WHERE reservationID = ?", status, reservationID);
    }

    // ─── OCCUPANT ───────────────────────────────────────────────────────────
    public static void insertOccupant(String resID, String cusID) {
        if (execute("INSERT INTO reservation_occupants VALUES (?, ?)", resID, cusID))
            System.out.println("[DB] Occupant " + cusID + " added to reservation " + resID);
    }

    public static void deleteOccupant(String resID, String cusID) {
        if (execute("DELETE FROM reservation_occupants WHERE reservationID = ? AND customerID = ?", resID, cusID))
            System.out.println("[DB] Occupant " + cusID + " removed from reservation " + resID);
    }

    // ─── FEEDBACK ───────────────────────────────────────────────────────────
    public static void insertFeedback(String resID, String cusID, int score, String comment) {
        if (execute("INSERT INTO feedbacks (reservationID, customerID, score, comment) VALUES (?, ?, ?, ?)",
                resID, cusID, score, comment))
            System.out.println("[DB] Feedback saved.");
    }

    public static void printAllFeedbacks() {
        String sql = "SELECT f.feedbackID, f.reservationID, r.tableID, c.fullName, f.score, f.comment "
                   + "FROM feedbacks f "
                   + "JOIN customers c ON f.customerID = c.customerID "
                   + "JOIN reservations r ON f.reservationID = r.reservationID";
        try (Connection conn = connect();
             ResultSet rs = conn.createStatement().executeQuery(sql)) {
            System.out.println("\n========== ALL FEEDBACKS ==========");
            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                System.out.printf("ID: %d | Res: %s | Table: %s | Customer: %s | Score: %d/5%n",
                        rs.getInt("feedbackID"), rs.getString("reservationID"),
                        rs.getString("tableID"), rs.getString("fullName"), rs.getInt("score"));
                System.out.println("Comment : " + rs.getString("comment"));
                System.out.println("------------------------------------");
            }
            if (!hasData) System.out.println("No feedbacks found.");
            System.out.println("====================================\n");
        } catch (SQLException e) {
            System.out.println("[DB Error] " + e.getMessage());
        }
    }

    // ─── REPORT ─────────────────────────────────────────────────────────────
    private static String dateFilter(LocalDate start, LocalDate end) {
        if (start == null || end == null) return "";
        return " WHERE date(reserveTime) BETWEEN '" + start + "' AND '" + end + "'";
    }

    public static int getTotalReservationsCount(LocalDate start, LocalDate end) {
        String sql = "SELECT COUNT(*) AS count FROM reservations" + dateFilter(start, end);
        try (Connection conn = connect();
             ResultSet rs = conn.createStatement().executeQuery(sql)) {
            return rs.getInt("count");
        } catch (SQLException e) {
            System.out.println("[DB Error] " + e.getMessage());
            return 0;
        }
    }

    public static double getAverageScore(LocalDate start, LocalDate end) {
        String filter = (start == null) ? ""
                : " AND date(r.reserveTime) BETWEEN '" + start + "' AND '" + end + "'";
        String sql = "SELECT AVG(f.score) AS avg FROM feedbacks f "
                   + "JOIN reservations r ON f.reservationID = r.reservationID"
                   + " WHERE 1=1" + filter;
        try (Connection conn = connect();
             ResultSet rs = conn.createStatement().executeQuery(sql)) {
            return rs.getDouble("avg");
        } catch (SQLException e) {
            System.out.println("[DB Error] " + e.getMessage());
            return 0.0;
        }
    }

    public static String getMostPopularTable(LocalDate start, LocalDate end) {
        String sql = "SELECT tableID, COUNT(*) AS cnt FROM reservations"
                   + dateFilter(start, end)
                   + " GROUP BY tableID ORDER BY cnt DESC LIMIT 1";
        try (Connection conn = connect();
             ResultSet rs = conn.createStatement().executeQuery(sql)) {
            return rs.next() ? rs.getString("tableID") : "N/A";
        } catch (SQLException e) {
            System.out.println("[DB Error] " + e.getMessage());
            return "N/A";
        }
    }

    public static String getPeakHour(LocalDate start, LocalDate end) {
        String sql = "SELECT strftime('%H', reserveTime) AS hour, COUNT(*) AS cnt "
                   + "FROM reservations" + dateFilter(start, end)
                   + " GROUP BY hour ORDER BY cnt DESC LIMIT 1";
        try (Connection conn = connect();
             ResultSet rs = conn.createStatement().executeQuery(sql)) {
            return rs.next() ? rs.getString("hour") + ":00" : "N/A";
        } catch (SQLException e) {
            System.out.println("[DB Error] " + e.getMessage());
            return "N/A";
        }
    }
}