package ui.view;

/** Navigation contract for moving between game-round and home views. */
public interface RoundNavigator {
  /** Navigates to the game round view. */
  void goToGameRound();

  /** Navigates to the home screen. */
  void goToHome();
}
