package com.codingame.game;

public class Coord {
    public static final Coord ZERO = new Coord(0, 0);
    public static final Coord UP = new Coord(0, -1);
    public static final Coord DOWN = new Coord(0, 1);
    public static final Coord RIGHT = new Coord(1, 0);
    
    public int x, y;
    
    public Coord(Coord c) {
        this(c.x, c.y);
    }
    
    public Coord(int x, int y) {
        this.x = x;
        this.y = y;
    };
    
    public Coord add(int x, int y) {
        return new Coord(this.x + x, this.y + y);
    }
    
    public Coord add(Coord c) {
        return add(c.x, c.y);
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + x;
        result = prime * result + y;
        return result;
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
    public String toString() {
        return x + " " + y;
    }
}
