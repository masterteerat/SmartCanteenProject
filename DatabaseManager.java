import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:SmartCanteen.db";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void initializeDB() {
        String createCustomerSQL = "CREATE TABLE IF NOT EXISTS customers (" +
                                   "customerID TEXT PRIMARY KEY, fullName TEXT NOT NULL, email TEXT UNIQUE, password TEXT)";
        String createAdminSQL = "CREATE TABLE IF NOT EXISTS admins (" +
                                "adminID TEXT PRIMARY KEY, fullName TEXT NOT NULL, email TEXT UNIQUE, password TEXT)";
        String createTableSQL = "CREATE TABLE IF NOT EXISTS tables (" +
                                "tableID TEXT PRIMARY KEY, capacity INTEGER, status TEXT)";
        String createReservationSQL = "CREATE TABLE IF NOT EXISTS reservations (" +
                                      "reservationID TEXT PRIMARY KEY, customerID TEXT, tableID TEXT, status TEXT, " +
                                      "FOREIGN KEY(customerID) REFERENCES customers(customerID), " +
                                      "FOREIGN KEY(tableID) REFERENCES tables(tableID))";
        String createFeedbackSQL = "CREATE TABLE IF NOT EXISTS feedbacks (" +
                                   "feedbackID INTEGER PRIMARY KEY AUTOINCREMENT, reservationID TEXT, customerID TEXT, score INTEGER, comment TEXT, " +
                                   "FOREIGN KEY(reservationID) REFERENCES reservations(reservationID), " +
                                   "FOREIGN KEY(customerID) REFERENCES customers(customerID))";
        
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(createCustomerSQL);
            stmt.execute(createAdminSQL);
            stmt.execute(createTableSQL);
            stmt.execute(createReservationSQL);
            stmt.execute(createFeedbackSQL);
            
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS count FROM tables");
            if (rs.getInt("count") == 0) {
                stmt.execute("INSERT INTO tables (tableID, capacity, status) VALUES ('01', 4, 'AVAILABLE')");
                stmt.execute("INSERT INTO tables (tableID, capacity, status) VALUES ('02', 2, 'AVAILABLE')");
                stmt.execute("INSERT INTO customers (customerID, fullName, email, password) VALUES ('C001', 'Bonus', 'bonus@mail.com', '1234')");
                stmt.execute("INSERT INTO customers (customerID, fullName, email, password) VALUES ('C002', 'Ploy', 'ploy@mail.com', '5678')");
                stmt.execute("INSERT INTO admins (adminID, fullName, email, password) VALUES ('A001', 'Admin Super', 'admin@mail.com', 'admin123')");
                System.out.println("[DB] Created all tables and inserted default data successfully.");
            }
        } catch (SQLException e) {
            System.out.println("[DB Error] " + e.getMessage());
        }
    }

    public static List<Table> loadTablesFromDB(Canteen canteen) {
        List<Table> list = new ArrayList<>();
        String query = "SELECT * FROM tables";
        
        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Table t = new Table(rs.getString("tableID"), rs.getInt("capacity"), canteen);
                String status = rs.getString("status");
                if (status.equals("OCCUPIED")) {
                    t.setStatus(Status.OCCUPIED);
                } else if (status.equals("RESERVED")) {
                    t.setStatus(Status.RESERVED);
                } else {
                    t.setStatus(Status.AVAILABLE);
                }
                list.add(t);
            }
        } catch (SQLException e) {
            System.out.println("[DB Error] " + e.getMessage());
        }
        return list;
    }

    public static void updateTableStatus(String tableID, String status) {
        String sql = "UPDATE tables SET status = ? WHERE tableID = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setString(2, tableID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("[DB Error] " + e.getMessage());
        }
    }

    public static boolean insertTable(Table tab) {
        String sql = "INSERT INTO tables (tableID, capacity, status) VALUES (?, ?, ?)";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tab.getTableID());
            pstmt.setInt(2, tab.getCapacity());
            pstmt.setString(3, tab.getStatus().name());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("[DB Error] Insert Table Failed: " + e.getMessage());
            return false;
        }
    }

    public static boolean deleteTable(String tableID) {
        String sql = "DELETE FROM tables WHERE tableID = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tableID);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("[DB Error] Delete Table Failed: " + e.getMessage());
            return false;
        }
    }

    public static boolean updateTable(Table tab) {
        String sql = "UPDATE tables SET capacity = ?, status = ? WHERE tableID = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, tab.getCapacity());
            pstmt.setString(2, tab.getStatus().name());
            pstmt.setString(3, tab.getTableID());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("[DB Error] Update Table Failed: " + e.getMessage());
            return false;
        }
    }

    public static void printAllFeedbacks() {
        String sql = "SELECT f.feedbackID, f.reservationID, c.fullName, f.score, f.comment " +
                     "FROM feedbacks f " +
                     "JOIN customers c ON f.customerID = c.customerID";
        
        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            
            System.out.println("\n========== 📋 ALL SYSTEM FEEDBACKS ==========");
            boolean hasData = false;
            
            // วนลูปอ่านข้อมูลทีละแถว
            while (rs.next()) {
                hasData = true;
                System.out.println("Feedback ID : " + rs.getInt("feedbackID"));
                System.out.println("Reservation : " + rs.getString("reservationID"));
                System.out.println("Customer    : " + rs.getString("fullName"));
                System.out.println("Score       : ⭐ " + rs.getInt("score") + "/5");
                System.out.println("Comment     : \"" + rs.getString("comment") + "\"");
                System.out.println("---------------------------------------------");
            }
            
            if (!hasData) {
                System.out.println("No feedbacks found in the database yet.");
            }
            System.out.println("=============================================\n");
            
        } catch (SQLException e) {
            System.out.println("[DB Error] Failed to retrieve feedbacks: " + e.getMessage());
        }
    }

    public static void insertFeedback(String resID, String cusID, int score, String comment) {
        String sql = "INSERT INTO feedbacks (reservationID, customerID, score, comment) VALUES (?, ?, ?, ?)";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, resID);
            pstmt.setString(2, cusID);
            pstmt.setInt(3, score);
            pstmt.setString(4, comment);
            pstmt.executeUpdate();
            System.out.println("[DB Success] Feedback saved to database.");
        } catch (SQLException e) {
            System.out.println("[DB Error] Insert Feedback Failed: " + e.getMessage());
        }
    }
}