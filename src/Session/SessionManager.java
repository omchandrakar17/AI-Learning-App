package session;

public class SessionManager {

    private static int currentUserId = -1;

    public static void setUserId(int id) {
        currentUserId = id;
    }

    public static int getUserId() {
        return currentUserId;
    }

    public static void logout() {
        currentUserId = -1;
    }
}