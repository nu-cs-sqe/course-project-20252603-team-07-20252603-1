package ui.view;

import java.util.Locale;

/**
 * Defines navigation actions available during game setup.
 */
public interface SetupNavigator {

  /**
   * Navigates to the home screen.
   */
  void goToHome();

  /**
   * Navigates to the player count selection screen.
   */
  void goToPlayerCount();

  /**
   * Navigates to the player configuration screen.
   *
   * @param count the number of players
   */
  void goToPlayerConfig(int count);

  /**
   * Navigates to the setup summary screen.
   */
  void goToSetupSummary();

  /**
   * Starts the game.
   */
  void startGame();

  /**
   * Switches the application language.
   *
   * @param locale the new locale to apply
   */
  void changeLocale(Locale locale);

}