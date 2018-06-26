import java.util.Scanner;

public class Solution {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int eggX = scanner.nextInt();
        int eggY = scanner.nextInt();
        while (true) {
            int fishX = scanner.nextInt();
            int fishY = scanner.nextInt();

            if (fishY > eggY) {
                System.out.println("UP");
            } else if (fishY < eggY) {
                System.out.println("DOWN");
            } else {
                System.out.println("STILL");
            }
        }
    }
}
