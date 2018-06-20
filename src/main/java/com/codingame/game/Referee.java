package com.codingame.game;
import com.codingame.gameengine.core.AbstractReferee;
import com.codingame.gameengine.core.SoloGameManager;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.google.inject.Inject;

public class Referee extends AbstractReferee {
    @Inject private SoloGameManager<Player> gameManager;
    @Inject private GraphicEntityModule graphicEntityModule;

    @Override
    public void init() {
        // Initialize your game here.
    }

    @Override
    public void gameTurn(int turn) {
        // Code your game logic.
        // See README.md if you want some code to bootstrap your project.
    }
}
