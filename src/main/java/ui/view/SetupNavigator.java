package ui.view;

import java.util.Locale;

public interface SetupNavigator {
    void goToHome();
    void goToPlayerCount();
    void goToPlayerConfig(int count);
    void goToSetupSummary();
    void startGame();
    void changeLocale(Locale locale);
}
