package ui;

import domain.model.gamepieces.DiceHandler;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.Locale;
import java.util.ResourceBundle;
import ui.controller.DevCardController;
import ui.controller.GameLoopController;
import ui.controller.GameSetupController;


/** Holds shared controller and model references available to all UI views. */
@SuppressFBWarnings(value = {"EI_EXPOSE_REP", "EI_EXPOSE_REP2"},
        justification = "UI classes intentionally share JavaFX nodes, controllers, and "
                + "models by reference")
public final class ViewContext {

  private final GameSetupController setupController;
  private final GameLoopController loopController;
  private final DevCardController devCardController;
  private final DiceHandler diceRoller;
  private final ResourceBundle labels;

  /**
   * Creates a ViewContext with the given controllers, dice handler, and labels.
   *
   * @param setupController   controller for game setup logic
   * @param loopController    controller for the main game loop
   * @param devCardController controller for development card actions
   * @param diceRoller        handles dice rolling
   * @param labels            resource bundle for UI labels
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

  /** Returns the game setup controller. */
  public GameSetupController setup() {
    return setupController;
  }

  /** Returns the game loop controller. */
  public GameLoopController loop() {
    return loopController;
  }

  /** Returns the development card controller. */
  public DevCardController devCards() {
    return devCardController;
  }

  /** Returns the dice handler. */
  public DiceHandler dice() {
    return diceRoller;
  }

  /** Returns the resource bundle for UI labels. */
  public ResourceBundle labels() {
    return labels;
  }

  /**
   * Returns a copy of this context with labels in the given locale.
   *
   * @param locale the new locale
   */
  public ViewContext withLocale(Locale locale) {
    ResourceBundle newLabels = ResourceBundle.getBundle("labels", locale);
    return new ViewContext(setupController, loopController,
            devCardController, diceRoller, newLabels);
  }
}