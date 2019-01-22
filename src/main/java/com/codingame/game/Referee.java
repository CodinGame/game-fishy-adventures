package com.codingame.game;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.codingame.gameengine.core.AbstractPlayer.TimeoutException;
import com.codingame.gameengine.core.AbstractReferee;
import com.codingame.gameengine.core.SoloGameManager;
import com.codingame.gameengine.module.entities.Curve;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Group;
import com.codingame.gameengine.module.entities.Sprite;
import com.codingame.gameengine.module.entities.Text;
import com.google.inject.Inject;

public class Referee extends AbstractReferee {
    @Inject private SoloGameManager<Player> gameManager;
    @Inject private GraphicEntityModule graphicEntityModule;

    private Coord fishPosition;
    private Map<Coord, Integer> eggs = new HashMap<>();

    private Sprite fishSprite;
    private Map<Coord, Group> eggGroups = new HashMap<>();
    
    private int eggsCollected = 0;

    @Override
    public void init() {
        gameManager.setFrameDuration(500);
        
        // Draw background
        graphicEntityModule.createSprite().setImage(Constants.BACKGROUND_SPRITE);
        
        int eggsCount = Integer.valueOf(gameManager.getTestCaseInput().get(0));
        gameManager.getPlayer().sendInputLine(gameManager.getTestCaseInput().get(0));

        Integer[] testInputs = Arrays.stream(gameManager.getTestCaseInput().get(1).split(" "))
            .map(s -> Integer.valueOf(s))
            .toArray(size -> new Integer[size]);
        fishPosition = new Coord(testInputs[0], testInputs[1]);
        
        for (int i = 0; i < eggsCount; i++) {
            Coord position = new Coord(testInputs[i * 3 + 2], testInputs[i * 3 + 3]);
            int eggsQuantity = testInputs[i * 3 + 4];
            eggs.put(position, eggsQuantity);

            Sprite eggSprite = graphicEntityModule.createSprite().setImage(Constants.EGGS_SPRITE)
                .setAnchor(.5)
                .setZIndex(1);
            
            Text eggText = graphicEntityModule.createText(String.valueOf(eggsQuantity))
                .setFontSize(60)
                .setFillColor(0x000000)
                .setAnchor(.5)
                .setZIndex(2);
            
            Group eggGroup = graphicEntityModule.createGroup(eggSprite, eggText)
                .setX(position.x * Constants.CELL_SIZE + Constants.CELL_OFFSET)
                .setY(position.y * Constants.CELL_SIZE + Constants.CELL_OFFSET);
            
            eggGroups.put(position, eggGroup);
            
            gameManager.getPlayer().sendInputLine(position.toString() + " " + eggsQuantity);
        }

        fishSprite = graphicEntityModule.createSprite().setImage(Constants.FISH_SPRITE)
            .setX(fishPosition.x * Constants.CELL_SIZE + Constants.CELL_OFFSET)
            .setY(fishPosition.y * Constants.CELL_SIZE + Constants.CELL_OFFSET)
            .setAnchor(.5)
            .setZIndex(2);
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

        // Check if an egg is picked up
        if (eggs.containsKey(fishPosition)) {
            eggsCollected += eggs.get(fishPosition);
            eggs.remove(fishPosition);
            Group g = eggGroups.get(fishPosition);
            g.setScale(0);
        }
        
        // Check win condition
        if (fishPosition.x >= Constants.COLUMNS - 1 && eggsCollected > 0) {
            gameManager.winGame(String.format("Congrats! You collected %d eggs!", eggsCollected));
        }

        updateView();
    }

    @Override
    public void onEnd() {
        gameManager.putMetadata("eggs", String.valueOf(eggsCollected));
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
            gameManager.loseGame("Your fish swum out of the game zone.");
        }
    }
}
