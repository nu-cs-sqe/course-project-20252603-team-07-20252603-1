package ui;

import domain.model.DiceRoller;
import java.util.ResourceBundle;
import ui.controller.GameLoopController;
import ui.controller.GameSetupController;


public final class ViewContext {

    private final GameSetupController setupController;
    private final GameLoopController loopController;
    private final DiceRoller diceRoller;
    private final ResourceBundle labels;

    public ViewContext(GameSetupController setupController,
                       GameLoopController loopController,
                       DiceRoller diceRoller,
                       ResourceBundle labels) {
        this.setupController = setupController;
        this.loopController = loopController;
        this.diceRoller = diceRoller;
        this.labels = labels;
    }

    public GameSetupController setup() {
        return setupController;
    }

    public GameLoopController loop() {
        return loopController;
    }

    public DiceRoller dice() {
        return diceRoller;
    }

    public ResourceBundle labels() {
        return labels;
    }
}
