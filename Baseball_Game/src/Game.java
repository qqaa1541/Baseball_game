public class Game {
    private int[] answer;
    private int digitCount;

    public Game(int digitCount) {
        this.digitCount = digitCount;
        generateAnswer();
    }

    private void generateAnswer() {
        answer = new int[digitCount];
        int i = 0;
        while (i < digitCount) {
            int num = (int)(Math.random() * 9) + 1;
            boolean duplicate = false;
            for (int j = 0; j < i; j++) {
                if (answer[j] == num) {
                    duplicate = true;
                    break;
                }
            }
            if (!duplicate) {
                answer[i] = num;
                i++;
            }
        }
    }

    public boolean isValid(String input) {
        if (input.length() != digitCount) return false;
        for (char c : input.toCharArray()) {
            if (c < '1' || c > '9') return false;
        }
        for (int i = 0; i < input.length(); i++) {
            for (int j = i + 1; j < input.length(); j++) {
                if (input.charAt(i) == input.charAt(j)) return false;
            }
        }
        return true;
    }

    public int[] check(String input) {
        int strike = 0, ball = 0;
        int[] guess = new int[digitCount];
        for (int i = 0; i < digitCount; i++) {
            guess[i] = input.charAt(i) - '0';
        }

        for (int i = 0; i < digitCount; i++) {
            if (guess[i] == answer[i]) {
                strike++;
            } else {
                for (int j = 0; j < digitCount; j++) {
                    if (guess[i] == answer[j]) {
                        ball++;
                        break;
                    }
                }
            }
        }

        return new int[]{strike, ball};
    }
}