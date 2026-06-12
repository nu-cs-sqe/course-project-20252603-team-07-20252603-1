package ui;

import domain.model.game_pieces.DiceHandler;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import ui.controller.GameLoopController;
import ui.controller.GameSetupController;

public class Main extends Application {

    private static final int WINDOW_WIDTH = 700;
    private static final int WINDOW_HEIGHT = 700;
    private static final String STYLESHEET = "/styles.css";
    private static final String BUNDLE_BASE_NAME = "labels";

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new StackPane(), WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.getStylesheets().add(getClass().getResource(STYLESHEET).toExternalForm());

        Locale locale = Locale.getDefault();
        ResourceBundle labels = ResourceBundle.getBundle(BUNDLE_BASE_NAME, locale);

        ViewContext context = new ViewContext(
                new GameSetupController(),
                new GameLoopController(),
                new DiceHandler(),
                labels
        );

        Navigator navigator = new Navigator(scene, context);
        navigator.goToHome();

        stage.setTitle(labels.getString("app.title"));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
