package ui;

import domain.model.DiceRoller;
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

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new StackPane(), WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.getStylesheets().add(getClass().getResource(STYLESHEET).toExternalForm());

        // TODO: Use dice class once implemented. For now, this allows us to test game flow without randomness.
        DiceRoller diceRoller = () -> 8;

        Navigator navigator = new Navigator(
                scene,
                new GameSetupController(),
                new GameLoopController(),
                diceRoller
        );
        navigator.goToHome();

        stage.setTitle("Catan");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
