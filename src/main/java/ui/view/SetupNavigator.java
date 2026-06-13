package ui.view;

/** Navigation callbacks used during the game setup flow. */
public interface SetupNavigator {
  /** Navigates to the home screen. */
  void goToHome();

  /** Navigates to the player count selection screen. */
  void goToPlayerCount();

  /** Navigates to the player configuration screen for the given number of players. */
  void goToPlayerConfig(int count);

  /** Navigates to the setup summary screen. */
  void goToSetupSummary();

  /** Starts the game and transitions to game play. */
  void startGame();
}
