package database;

import session.SessionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SaveManager {

    public static void saveContent(String title, String content) {
        String sql = "INSERT INTO saved_content (user_id, title, content) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, SessionManager.getUserId());
            ps.setString(2, title);
            ps.setString(3, content);
            ps.executeUpdate();
        } catch (Exception e) {
            System.err.println("Failed to save content: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static List<String> getSavedChats() {
        List<String> chats = new ArrayList<>();
        String sql = "SELECT title, content FROM saved_content WHERE user_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, SessionManager.getUserId());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String title = rs.getString("title");
                    String content = rs.getString("content");
                    chats.add("Topic: " + title + "\n\n" + content);
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to retrieve chats: " + e.getMessage());
            e.printStackTrace();
        }
        return chats;
    }

    public static int getSavedCount() {
        int userId = SessionManager.getUserId();
        if (userId <= 0) return 0;
        String sql = "SELECT COUNT(*) AS total FROM saved_content WHERE user_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        } catch (Exception e) {
            System.err.println("Error counting saved items: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }
}