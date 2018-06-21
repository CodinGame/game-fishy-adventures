import java.util.Collections;
import java.util.LinkedList;
import java.util.Scanner;

import com.codingame.game.Coord;

public class Player1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int eggsCount = scanner.nextInt();
        LinkedList<Coord> eggsPositions = new LinkedList<>();
        for (int i = 0; i < eggsCount; i++) {
            int eggX = scanner.nextInt();
            int eggY = scanner.nextInt();
            eggsPositions.add(new Coord(eggX, eggY));
        }
        Collections.sort(eggsPositions);
        while (true) {
            int fishX = scanner.nextInt();
            int fishY = scanner.nextInt();
            Coord fishPosition = new Coord(fishX, fishY);
            
            if (!eggsPositions.isEmpty()) {
                Coord nextEgg = eggsPositions.getFirst();
                if (fishPosition.equals(nextEgg)) {
                    eggsPositions.poll();
                    nextEgg = eggsPositions.getFirst();
                }
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
    
    static class Coord implements Comparable<Coord> {
        public int x, y;
        
        public Coord(int x, int y) {
            this.x = x;
            this.y = y;
        }
        
        @Override
        public boolean equals(Object obj) {
            Coord other = (Coord) obj;
            if (x != other.x)
                return false;
            if (y != other.y)
                return false;
            return true;
        }

        @Override
        public int compareTo(Coord o) {
            return Integer.compare(this.y, y);
        }
    }
}
