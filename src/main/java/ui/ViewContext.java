package ui;

import domain.model.gamepieces.DiceHandler;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.ResourceBundle;
import ui.controller.DevCardController;
import ui.controller.GameLoopController;
import ui.controller.GameSetupController;


@SuppressFBWarnings(value = {"EI_EXPOSE_REP", "EI_EXPOSE_REP2"},
    justification = "UI classes intentionally share JavaFX nodes, controllers, and models by reference")
public final class ViewContext {

  private final GameSetupController setupController;
  private final GameLoopController loopController;
  private final DevCardController devCardController;
  private final DiceHandler diceRoller;
  private final ResourceBundle labels;

  public ViewContext(GameSetupController setupController,
                     GameLoopController loopController,
                     DevCardController devCardController,
                     DiceHandler diceRoller,
                     ResourceBundle labels) {
    this.setupController = setupController;
    this.loopController = loopController;
    this.devCardController = devCardController;
    this.diceRoller = diceRoller;
    this.labels = labels;
  }

  public GameSetupController setup() {
    return setupController;
  }

  public GameLoopController loop() {
    return loopController;
  }

  public DevCardController devCards() {
    return devCardController;
  }

  public DiceHandler dice() {
    return diceRoller;
  }

  public ResourceBundle labels() {
    return labels;
  }
}
