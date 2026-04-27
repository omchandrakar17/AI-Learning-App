package database;

import session.SessionManager;
import ui.User;
import java.sql.*;

public class UserManager {

    // ================= SIGNUP =================
    public static boolean signup(String username, int age, String email, String password) {

        String sql = "INSERT INTO users(username, age, email, password) VALUES(?,?,?,?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setInt(2, age);
            pstmt.setString(3, email);
            pstmt.setString(4, password);

            int rows = pstmt.executeUpdate();

            System.out.println("User inserted: " + rows); // ✅ DEBUG

            return true;

        } catch (Exception e) {
            System.err.println("Signup failed: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // ================= LOGIN (FIXED) =================
    public static boolean login(String email, String password) {

        String sql = "SELECT id FROM users WHERE email=? AND password=?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                SessionManager.setUserId(rs.getInt("id"));
                System.out.println("Login success! User ID: " + SessionManager.getUserId());
                return true;
            }

        } catch (Exception e) {
            System.err.println("Login failed: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    // ================= CURRENT USER =================
    public static User getCurrentUser() {

        int userId = SessionManager.getUserId();
        if (userId <= 0) return null;

        String sql = "SELECT username, age, email, password FROM users WHERE id=?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getString("username"),
                        rs.getInt("age"),
                        rs.getString("email"),
                        rs.getString("password")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}