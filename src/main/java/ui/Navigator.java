package ui;

import domain.model.GameModel;
import domain.model.GameSetupModel;
import javafx.scene.Scene;
import ui.controller.GameSetupController;
import ui.view.GameRoundView;
import ui.view.HomeScreenView;
import ui.view.PlayerConfigView;
import ui.view.PlayerCountView;
import ui.view.RoundNavigator;
import ui.view.SetupNavigator;
import ui.view.SetupSummaryView;

public class Navigator implements SetupNavigator, RoundNavigator {
    private final Scene scene;
    private final ViewContext context;

    private GameSetupModel setupModel;
    private GameModel gameModel;

    public Navigator(Scene scene, ViewContext context) {
        this.scene = scene;
        this.context = context;
        this.setupModel = new GameSetupModel();
    }

    @Override
    public void goToHome() {
        scene.setRoot(new HomeScreenView(this, context).getRoot());
    }

    @Override
    public void goToPlayerCount() {
        scene.setRoot(new PlayerCountView(this, context).getRoot());
    }

    @Override
    public void goToPlayerConfig(int count) {
        setupModel = new GameSetupModel();
        scene.setRoot(new PlayerConfigView(this, context, setupModel, count).getRoot());
    }

    @Override
    public void goToSetupSummary() {
        GameSetupController setup = context.setup();
        setup.initializeResourceDeck(setupModel);
        setup.initializeDevelopmentCardDeck(setupModel);
        setup.determineTurnOrder(setupModel);
        scene.setRoot(new SetupSummaryView(this, context, setupModel).getRoot());
    }

    @Override
    public void startGame() {
        gameModel = new GameModel(context.setup().getTurnOrder(setupModel));
        goToGameRound();
    }

    @Override
    public void goToGameRound() {
        GameSetupController setup = context.setup();
        scene.setRoot(new GameRoundView(
                this,
                context,
                gameModel,
                setup.getResourceDeck(setupModel),
                setup.getBoard(setupModel)
        ).getRoot());
    }
}
