package ui;


import domain.model.GameModel;
import domain.model.GameSetupModel;
import domain.model.game_pieces.DiceHandler;
import javafx.scene.Scene;
import ui.controller.GameLoopController;
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
    private final GameSetupController setupController;
    private final GameLoopController loopController;
    private final DiceHandler diceRoller;

    private GameSetupModel setupModel;
    private GameModel gameModel;

    public Navigator(Scene scene,
                     GameSetupController setupController,
                     GameLoopController loopController,
                     DiceHandler diceRoller) {
        this.scene = scene;
        this.setupController = setupController;
        this.loopController = loopController;
        this.diceRoller = diceRoller;
        this.setupModel = new GameSetupModel();
    }

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

    @Override
    public void goToSetupSummary() {
        setupController.initializeResourceDeck(setupModel);
        setupController.initializeDevelopmentCardDeck(setupModel);
        setupController.determineTurnOrder(setupModel);
        scene.setRoot(new SetupSummaryView(this, setupController, setupModel).getRoot());
    }

    @Override
    public void startGame() {
        gameModel = new GameModel(setupController.getTurnOrder(setupModel));
        goToGameRound();
    }

    @Override
    public void goToGameRound() {
        scene.setRoot(new GameRoundView(
                this,
                loopController,
                gameModel,
                diceRoller,
                setupController.getResourceDeck(setupModel),
                setupController.getBoard(setupModel)
        ).getRoot());
    }
}
