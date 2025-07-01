import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBTest {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/baseball_game?serverTimezone=Asia/Seoul";
        String user = "root";
        String password = "123123"; // 본인 비밀번호

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("✅ MySQL 연결 성공!");
        } catch (SQLException e) {
            System.out.println("❌ 연결 실패: " + e.getMessage());
        }
    }
}