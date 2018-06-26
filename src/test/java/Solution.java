import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*
 * A basic example AI for the game.
 * Goes to the closest reachable eggs.
 */

public class Solution {
    private static List<Coord> eggsPositions = new ArrayList<>();
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int eggsCount = scanner.nextInt();
        for (int i = 0; i < eggsCount; i++) {
            int eggX = scanner.nextInt();
            int eggY = scanner.nextInt();
            eggsPositions.add(new Coord(eggX, eggY));
        }

        while (true) {
            int fishX = scanner.nextInt();
            int fishY = scanner.nextInt();
            Coord fishPosition = new Coord(fishX, fishY);

            Coord nextEgg = closestReachableCoord(fishPosition);
            if (nextEgg != null) {
                if (fishY > nextEgg.y) {
                    System.out.println("UP");
                } else if (fishY < nextEgg.y) {
                    System.out.println("DOWN");
                } else {
                    System.out.println("STILL");
                }
            } else {
                System.out.println("STILL");
            }
        }
    }

    private static Coord closestReachableCoord(Coord fishPosition) {
        Coord closestReachableCord = null;
        int min = Integer.MAX_VALUE;
        for (Coord coord : reachableCoords(fishPosition)) {
            int dist = coord.x - fishPosition.x;
            if (closestReachableCord == null || min > dist) {
                min = dist;
                closestReachableCord = coord;
            }
        }
        return closestReachableCord;
    }
    
    private static List<Coord> reachableCoords(Coord fishPosition) {
        List<Coord> reachableCoords = new ArrayList<>();
        for (Coord coord : eggsPositions) {
            if (fishPosition.x < coord.x && Math.abs(fishPosition.x - coord.x) >= Math.abs(fishPosition.y - coord.y)) {
                reachableCoords.add(coord);
            }
        }
        return reachableCoords;
    }

    static class Coord {
        public int x, y;

        public Coord(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
