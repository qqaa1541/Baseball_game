import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBManager {
    private static final String URL = "jdbc:mysql://localhost:3306/baseball_game?serverTimezone=Asia/Seoul";
    private static final String USER = "root";
    private static final String PASSWORD = "123123"; // 본인 비밀번호로 변경

    // 기록 저장
    public static void saveRecord(String playerName, int attempts, int digitCount) {
        String sql = "INSERT INTO game_record (player_name, attempts, digit_count) VALUES (?, ?, ?)";
        try (
                Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, playerName);
            stmt.setInt(2, attempts);
            stmt.setInt(3, digitCount);
            stmt.executeUpdate();
            System.out.println("✅ 기록 저장 성공!");
        } catch (SQLException e) {
            System.out.println("❌ 기록 저장 실패: " + e.getMessage());
        }
    }

    // 자리수별 랭킹 조회
    public static List<String> getTop5Rankings(int digitCount) {
        List<String> rankings = new ArrayList<>();
        String sql = "SELECT player_name, attempts, play_date FROM game_record WHERE digit_count = ? ORDER BY attempts ASC, play_date ASC LIMIT 5";

        try (
                Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, digitCount);
            ResultSet rs = stmt.executeQuery();
            int rank = 1;
            while (rs.next()) {
                String player = rs.getString("player_name");
                int attempts = rs.getInt("attempts");
                Timestamp date = rs.getTimestamp("play_date");
                String record = String.format("%d등: %s - 시도: %d, 시간: %s", rank++, player, attempts, date.toString());
                rankings.add(record);
            }
        } catch (SQLException e) {
            System.out.println("❌ 랭킹 조회 실패: " + e.getMessage());
        }
        return rankings;
    }

    // 전체 기록 초기화
    public static void clearAllRecords() {
        String sql = "DELETE FROM game_record";
        try (
                Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                Statement stmt = conn.createStatement()
        ) {
            stmt.executeUpdate(sql);
            System.out.println("🗑 전체 기록이 초기화되었습니다.");
        } catch (SQLException e) {
            System.out.println("❌ 기록 초기화 실패: " + e.getMessage());
        }
    }
}