package ui;

import domain.model.GameSetupModel;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import ui.controller.GameSetupController;
import ui.view.HomeScreenView;
import ui.view.PlayerCountView;
import ui.view.SetupNavigator;

public class Main extends Application {

    private GameSetupModel setupModel;
    private GameSetupController setupController;
    private Scene scene;

    @Override
    public void start(Stage stage) {
        setupModel = new GameSetupModel();
        setupController = new GameSetupController();
        scene = new Scene(new StackPane(), 600, 400);

        SetupNavigator navigator = new SetupNavigator() {
            @Override
            public void goToHome() {
                scene.setRoot(new HomeScreenView(this).getRoot());
            }

            @Override
            public void goToPlayerCount() {
                scene.setRoot(new PlayerCountView(this).getRoot());
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
