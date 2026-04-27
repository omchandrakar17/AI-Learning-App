package database;

import session.SessionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class HistoryManager {

    public static void saveHistory(String userInput, String aiResponse) {
        String sql = "INSERT INTO history (user_id, user_input, ai_response) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, SessionManager.getUserId());
            ps.setString(2, userInput);
            ps.setString(3, aiResponse);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addHistory(String topic) {
        String sql = "INSERT INTO history (user_id, user_input, ai_response) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, SessionManager.getUserId());
            ps.setString(2, "Started Topic: " + topic);
            ps.setString(3, "Session Initialized");
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<String> getHistory() {
        List<String> historyList = new ArrayList<>();
        String sql = "SELECT user_input, ai_response FROM history WHERE user_id = ? ORDER BY timestamp DESC";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, SessionManager.getUserId());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String input = rs.getString("user_input");
                    String response = rs.getString("ai_response");
                    historyList.add("You: " + input + "\nAI: " + response);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return historyList;
    }

    public static int getHistoryCount() {
        int userId = SessionManager.getUserId();
        if (userId <= 0) return 0;
        String sql = "SELECT COUNT(*) AS total FROM history WHERE user_id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}