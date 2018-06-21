package com.codingame.game;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.codingame.gameengine.core.AbstractPlayer.TimeoutException;
import com.codingame.gameengine.core.AbstractReferee;
import com.codingame.gameengine.core.SoloGameManager;
import com.codingame.gameengine.module.entities.Curve;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Sprite;
import com.google.inject.Inject;

public class Referee extends AbstractReferee {
    @Inject private SoloGameManager<Player> gameManager;
    @Inject private GraphicEntityModule graphicEntityModule;

    private Coord fishPosition;
    private Coord eggPosition;

    private Sprite fishSprite;
    private Sprite eggSprite;

    @Override
    public void init() {
        gameManager.setFrameDuration(500);
        
        // Draw background
        graphicEntityModule.createSprite().setImage(Constants.BACKGROUND_SPRITE);

        Integer[] positions = Arrays.stream(gameManager.getTestCase().get(0).split(" "))
            .map(s -> Integer.valueOf(s))
            .toArray(size -> new Integer[size]);
        fishPosition = new Coord(positions[0], positions[1]);
        eggPosition = new Coord(positions[2], positions[3]);

        eggSprite = graphicEntityModule.createSprite().setImage(Constants.EGGS_SPRITE)
            .setX(eggPosition.x * Constants.CELL_SIZE + Constants.CELL_OFFSET)
            .setY(eggPosition.y * Constants.CELL_SIZE + Constants.CELL_OFFSET)
            .setAnchor(.5)
            .setZIndex(1);

        fishSprite = graphicEntityModule.createSprite().setImage(Constants.FISH_SPRITE)
            .setX(fishPosition.x * Constants.CELL_SIZE + Constants.CELL_OFFSET)
            .setY(fishPosition.y * Constants.CELL_SIZE + Constants.CELL_OFFSET)
            .setAnchor(.5)
            .setZIndex(2);

        gameManager.getPlayer().sendInputLine(eggPosition.toString());
    }

    @Override
    public void gameTurn(int turn) {
        gameManager.getPlayer().sendInputLine(fishPosition.toString());

        gameManager.getPlayer().execute();

        try {
            List<String> outputs = gameManager.getPlayer().getOutputs();

            String output = checkOutput(outputs);

            if (output != null) {
                Action action = Action.valueOf(output.toUpperCase());
                checkInvalidAction(action);
                fishPosition = fishPosition.add(Constants.ACTION_MAP.get(action)).add(Coord.RIGHT);
            }

        } catch (TimeoutException e) {
            gameManager.loseGame("Timeout!");
        }

        // Check win condition
        if (fishPosition.equals(eggPosition)) {
            gameManager.winGame("Congrats!");
            eggSprite.setScale(0);
        }

        updateView();
    }

    private void updateView() {
        fishSprite.setX(fishPosition.x * Constants.CELL_SIZE + Constants.CELL_OFFSET, Curve.LINEAR)
            .setY(fishPosition.y * Constants.CELL_SIZE + Constants.CELL_OFFSET, Curve.LINEAR);
    }

    private String checkOutput(List<String> outputs) {
        if (outputs.size() != 1) {
            gameManager.loseGame("You did not send 1 output in your turn.");
        } else {
            String output = outputs.get(0);
            if (!Arrays.asList(Constants.ACTIONS).contains(output)) {
                gameManager
                    .loseGame(
                        String.format(
                            "Expected output: %s but received %s",
                            Arrays.asList(Constants.ACTIONS).stream().collect(Collectors.joining(" | ")),
                            output
                        )
                    );
            } else {
                return output;
            }
        }
        return null;
    }

    private void checkInvalidAction(Action action) {
        if ((fishPosition.y == 0 && action == Action.UP)
            || (fishPosition.y == Constants.ROWS - 1 && action == Action.DOWN)
            || fishPosition.add(Coord.RIGHT).x > Constants.COLUMNS - 1) {
            gameManager.loseGame("Your fish swimmed out of the game zone.");
        }
    }
}
