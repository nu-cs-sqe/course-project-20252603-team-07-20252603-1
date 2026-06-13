package ui.view;

/** Navigation callbacks used during the main game round. */
public interface RoundNavigator {
  /** Navigates to the game round view. */
  void goToGameRound();

  /** Navigates back to the home screen. */
  void goToHome();
}
