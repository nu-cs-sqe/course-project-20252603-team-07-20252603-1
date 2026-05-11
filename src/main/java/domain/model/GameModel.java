package domain.model;

import java.util.List;

public class GameModel {

    private final List<Player> turnOrder;
    private int currentPlayerIndex;

    public GameModel(List<Player> turnOrder) {
        this.turnOrder = turnOrder;
        this.currentPlayerIndex = 0;
    }

    public List<Player> getTurnOrder() {
        return turnOrder;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public Player getCurrentPlayer() {
        return turnOrder.get(currentPlayerIndex);
    }
}
