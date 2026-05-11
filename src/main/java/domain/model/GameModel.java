package domain.model;

import java.util.List;

public class GameModel {

    private final List<Player> turnOrder;

    public GameModel(List<Player> turnOrder) {
        this.turnOrder = turnOrder;
    }

    public List<Player> getTurnOrder() {
        return turnOrder;
    }
}
