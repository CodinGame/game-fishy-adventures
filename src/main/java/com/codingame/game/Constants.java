package com.codingame.game;

import java.util.HashMap;
import java.util.Map;

import com.codingame.gameengine.module.entities.World;

public class Constants {
    public static final int VIEWER_WIDTH = World.DEFAULT_WIDTH;
    public static final int VIEWER_HEIGHT = World.DEFAULT_HEIGHT;
    
    public static final int CELL_SIZE = 128;
    public static final int CELL_OFFSET = CELL_SIZE / 2;
    public static final int ROWS = 6;
    public static final int COLUMNS = 15;
    
    public static final String FISH_SPRITE = "fish.png";
    public static final String EGGS_SPRITE = "eggs.png";
    public static final String BACKGROUND_SPRITE = "background.png";
    
    public static final String UP_ACTION = "UP";
    public static final String DOWN_ACTION = "DOWN";
    public static final String STILL_ACTION = "STILL";
    public static final String[] ACTIONS = new String[] { UP_ACTION, DOWN_ACTION, STILL_ACTION };
    
    public static final Map<Action, Coord> ACTION_MAP = new HashMap<>();
    
    static {
        ACTION_MAP.put(Action.UP, Coord.UP);
        ACTION_MAP.put(Action.DOWN, Coord.DOWN);
        ACTION_MAP.put(Action.STILL, Coord.ZERO);
    }
}
