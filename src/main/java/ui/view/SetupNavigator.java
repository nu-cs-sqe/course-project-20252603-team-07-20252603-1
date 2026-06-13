package ui.view;

/** Navigation contract for the game-setup flow. */
public interface SetupNavigator {
  /** Navigates to the home screen. */
  void goToHome();

  /** Navigates to the player-count selection screen. */
  void goToPlayerCount();

  /** Navigates to the player configuration screen for the given number of players. */
  void goToPlayerConfig(int count);

  /** Navigates to the setup summary screen. */
  void goToSetupSummary();

  /** Finalizes setup and starts the game. */
  void startGame();
}
