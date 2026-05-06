package ui;

import domain.model.GameSetupModel;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import ui.controller.GameSetupController;
import ui.view.HomeScreenView;
import ui.view.SceneNavigator;

public class Main extends Application {

    private GameSetupModel setupModel;
    private GameSetupController setupController;

    @Override
    public void start(Stage stage) {
        setupModel = new GameSetupModel();
        setupController = new GameSetupController();

        Scene scene = new Scene(new StackPane(), 600, 400);
        SceneNavigator navigator = scene::setRoot;

        HomeScreenView home = new HomeScreenView(navigator);
        scene.setRoot(home.getRoot());

        stage.setTitle("Catan");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
