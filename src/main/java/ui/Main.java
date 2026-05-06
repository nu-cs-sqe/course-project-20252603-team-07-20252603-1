package ui;

import domain.model.GameSetupModel;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import ui.controller.GameSetupController;
import ui.view.HomeScreenView;
import ui.view.PlayerConfigView;
import ui.view.PlayerCountView;
import ui.view.SetupNavigator;

public class Main extends Application {

    private static final int WINDOW_WIDTH = 600;
    private static final int WINDOW_HEIGHT = 400;
    private static final String STYLESHEET = "/styles.css";

    private GameSetupModel setupModel;
    private GameSetupController setupController;
    private Scene scene;

    @Override
    public void start(Stage stage) {
        setupModel = new GameSetupModel();
        setupController = new GameSetupController();
        scene = new Scene(new StackPane(), WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.getStylesheets().add(getClass().getResource(STYLESHEET).toExternalForm());

        SetupNavigator navigator = new SetupNavigator() {
            @Override
            public void goToHome() {
                scene.setRoot(new HomeScreenView(this).getRoot());
            }

            @Override
            public void goToPlayerCount() {
                scene.setRoot(new PlayerCountView(this).getRoot());
            }

            @Override
            public void goToPlayerConfig(int count) {
                setupModel = new GameSetupModel();
                scene.setRoot(new PlayerConfigView(this, setupController, setupModel, count).getRoot());
            }
        };

        navigator.goToHome();

        stage.setTitle("Catan");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
