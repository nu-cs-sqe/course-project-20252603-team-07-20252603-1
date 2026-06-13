package ui;

import domain.model.gamepieces.DiceHandler;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.ResourceBundle;
import ui.controller.DevCardController;
import ui.controller.GameLoopController;
import ui.controller.GameSetupController;


/** Shared bag of controllers, dice, and labels passed through the view hierarchy. */
@SuppressFBWarnings(value = {"EI_EXPOSE_REP", "EI_EXPOSE_REP2"},
    justification = "UI classes share JavaFX nodes, controllers, and models by reference")
public final class ViewContext {

  private final GameSetupController setupController;
  private final GameLoopController loopController;
  private final DevCardController devCardController;
  private final DiceHandler diceRoller;
  private final ResourceBundle labels;

  /**
   * Creates a ViewContext bundling all shared dependencies.
   *
   * @param setupController the game-setup controller
   * @param loopController the game-loop controller
   * @param devCardController the dev-card controller
   * @param diceRoller the dice handler
   * @param labels the localized label bundle
   */
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

  /** Returns the game-setup controller. */
  public GameSetupController setup() {
    return setupController;
  }

  /** Returns the game-loop controller. */
  public GameLoopController loop() {
    return loopController;
  }

  /** Returns the dev-card controller. */
  public DevCardController devCards() {
    return devCardController;
  }

  /** Returns the dice handler. */
  public DiceHandler dice() {
    return diceRoller;
  }

  /** Returns the localized label bundle. */
  public ResourceBundle labels() {
    return labels;
  }
}
