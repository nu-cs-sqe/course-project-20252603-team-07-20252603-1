package ui;

import domain.model.GameModel;
import domain.model.GameSetupModel;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import javafx.scene.Scene;
import ui.controller.GameSetupController;
import ui.view.GameRoundView;
import ui.view.HomeScreenView;
import ui.view.PlayerConfigView;
import ui.view.PlayerCountView;
import ui.view.RoundNavigator;
import ui.view.SetupNavigator;
import ui.view.SetupSummaryView;

@SuppressFBWarnings(value = {"EI_EXPOSE_REP", "EI_EXPOSE_REP2"},
        justification = "UI classes intentionally share JavaFX nodes, controllers, and models by reference")
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
        scene.setRoot(new SetupSummaryView(this, context, setupModel).getRoot());
    }

    @Override
    public void startGame() {
        GameSetupController setup = context.setup();
        gameModel = new GameModel(setup.getTurnOrder(setupModel), setup.getBoard(setupModel));
        context.loop().enterSetupPhase(gameModel);
        goToGameRound();
    }

    @Override
    public void goToGameRound() {
        GameSetupController setup = context.setup();
        scene.setRoot(new GameRoundView(
                this,
                context,
                gameModel,
                setup.getBoard(setupModel),
                setup.getDevelopmentCardDeck(setupModel)
        ).getRoot());
    }
}
