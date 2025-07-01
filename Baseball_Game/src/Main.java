import java.util.List;
import java.util.Scanner;

public class Main {
    private static volatile boolean timeOver = false;

    public static void main(String[] args) {
        final String RESET = "\u001B[0m";
        final String GREEN = "\u001B[32m";
        final String YELLOW = "\u001B[33m";

        Scanner scanner = new Scanner(System.in);
        String restart;

        do {
            timeOver = false;

            System.out.print("플레이어 이름 입력: ");
            String name = scanner.nextLine();

            int digitCount;
            while (true) {
                System.out.println("난이도를 선택하세요:");
                System.out.println("1. Easy (3자리)");
                System.out.println("2. Hard (4자리)");
                System.out.print("선택 (1 또는 2): ");
                String choice = scanner.nextLine().trim();

                if (choice.equals("1")) {
                    digitCount = 3;
                    break;
                } else if (choice.equals("2")) {
                    digitCount = 4;
                    break;
                } else {
                    System.out.println("⚠ 잘못된 입력입니다. 1 또는 2를 입력해주세요.");
                }
            }

            Player player = new Player(name);
            Game game = new Game(digitCount);

            Thread timerThread = new Thread(() -> {
                try {
                    Thread.sleep(90000);
                    timeOver = true;
                    System.out.println("\n⏰ 제한 시간 초과! 게임 오버!");
                } catch (InterruptedException e) {}
            });
            timerThread.start();

            while (true) {
                if (timeOver) break;

                System.out.print("숫자 " + digitCount + "자리 입력(1~9) : ");
                String input = scanner.nextLine();

                if (timeOver) break;

                if (!game.isValid(input)) {
                    System.out.println("⚠ 올바르지 않은 입력입니다. 다시 시도하세요.");
                    continue;
                }

                player.increaseAttempt();
                int[] result = game.check(input);

                System.out.println(
                        GREEN + "Strike = " + result[0] + RESET + " " +
                                YELLOW + "Ball = " + result[1] + RESET
                );

                if (result[0] == digitCount) {
                    System.out.println("🎉 정답입니다! \n🎯 시도 횟수: " + player.getAttempts());
                    DBManager.saveRecord(player.getName(), player.getAttempts(), digitCount);
                    timerThread.interrupt();
                    break;
                }
            }

            if (!timeOver) {
                List<String> rankings = DBManager.getTop5Rankings(digitCount);
                System.out.println("\n🏆 " + digitCount + "자리 랭킹 TOP5 🏆");
                for (String rank : rankings) {
                    System.out.println(rank);
                }
            }

            System.out.print("\n🔁 다시 시작하시겠습니까? (Y/N): ");
            restart = scanner.nextLine().trim().toUpperCase();

            System.out.print("📛 랭킹을 초기화하시겠습니까? (Y/N): ");
            String clear = scanner.nextLine().trim().toUpperCase();
            if (clear.equals("Y")) {
                DBManager.clearAllRecords();
            }

        } while (restart.equals("Y"));

        scanner.close();
        System.out.println("👋 게임을 종료합니다.");
    }
}